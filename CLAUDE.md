# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

"To Do Compose" (package `com.dboy.todocompose`) is a single-module Android app for creating and managing to-do tasks. A task has a title, description, priority, and timestamp. From the list screen the user can search tasks and sort/filter them by priority. All UI copy and most code comments are in Brazilian Portuguese; there are no other locale resources (`values-en`, etc.) — treat Portuguese as the only supported language unless asked otherwise.

The app was created in 2022 and its dependencies were modernized in August 2026 (see commits "preparing project for modernization" / "Primeira atualizacao de dependencias com sucesso"). Dependency versions are intentionally pinned as-is — don't bump them unless a task specifically requires it.

## Build / lint / test commands

Run from the repo root using the Gradle wrapper (`gradlew.bat` on Windows / `./gradlew` in Bash).

```
gradlew assembleDebug                 # build debug APK
gradlew build                         # full build (compiles, lints, runs unit tests)
gradlew lint                          # Android lint

gradlew test                          # run all JVM unit tests (app/src/test)
gradlew testDebugUnitTest             # same, debug variant
gradlew testDebugUnitTest --tests "com.dboy.todocompose.utils.DateFormaterTest"
gradlew testDebugUnitTest --tests "com.dboy.todocompose.utils.DateFormaterTest.testDate"

gradlew connectedAndroidTest          # instrumented tests (app/src/androidTest) — REQUIRES a connected device/emulator
```

There is no CI config and no linked GitHub Actions workflow in this repo.

## Architecture

Single `:app` module, MVVM, 100% Kotlin + Jetpack Compose (Compose Material **2**, not Material 3). Dependency injection via Dagger-Hilt; annotation processing via KSP (not kapt).

### Navigation & screens

One `MainActivity` hosts a single `NavHost` (`ui/presentation/navigation/SetupNavHost.kt`) with three routes defined in `Screen.kt`:

- `splash_screen` → `SplashScreen` — animated logo, delays `Constants.SPLASH_SCREEN_DELAY` (1700ms), then navigates to the list screen with `popUpTo(inclusive = true)` (splash is removed from the back stack).
- `list_screen` → `ListScreen` — the home screen: search, priority sort/filter, multi-select delete, FAB to create a task.
- `task_screen/{taskId}` → `TaskScreen` — create/edit a single task. `taskId == -1` means "new task"; any other id loads/edits an existing task.

There is a single `SharedViewModel` (Hilt `@HiltViewModel`), created once in `MainActivity` and passed down through the whole nav graph — screens do not get their own view models. All cross-screen state (task list, search state, sort state, the in-progress upsert form fields, multi-select state) lives on this one view model.

### Data layer

- `data/models/ToDoTask.kt` — Room `@Entity` (table `todo_table`): `title`, `description`, `priority: Priority`, `timeStamp: Long`, auto-generated `id`.
- `data/models/Priority.kt` — enum **in Portuguese**: `ALTA` (high), `MEDIA` (medium), `BAIXA` (low), `NENHUMA` (none). Each variant carries its indicator `Color`. Room persists enums by their `.name`.
- `data/source/ToDoDao.kt` / `ToDoDatabase.kt` — Room DAO/DB (`todo_db`, version 1, `exportSchema = false`, no migrations defined).
- `data/repository/ToDoRepository.kt` (+ `ToDoRepositoryImpl`) — thin wrapper around the DAO, `@ViewModelScoped` via Hilt.
- `data/repository/DataStoreRepository.kt` — Preferences DataStore (`todo_preferences`), persists only one value: the currently selected priority sort order (`Priority.name` as a string), read back and parsed with `Priority.valueOf(...)` on startup.
- DI wiring: `di/AppModule.kt` provides the Room DB, the repository, and `DispatcherProvider`. `di/ToDoApp.kt` is the `@HiltAndroidApp` Application class.
- `ui/presentation/DispatcherProvider` / `StandardDispatchers` — indirection over `Dispatchers.*` purely so tests can swap in `FakeDispatchers` (`app/src/test/.../FakeDispatchers.kt`, all `UnconfinedTestDispatcher`).

### Priority-based sorting queries

The raw SQL in `ToDoDao.kt` (`sortByLowPriority`, `sortByHighPriority`, `searchDatabaseLowPriorityOrder`, `searchDatabaseHighPriorityOrder`) ranks rows with a `CASE WHEN priority = 'BAIXA'/'MEDIA'/'ALTA' THEN ... END, timeStamp DESC` expression, matching the current Portuguese `Priority` enum names exactly. (Previously this used `LIKE 'H%'/'M%'/'L%'` prefixes left over from an earlier English enum — `HIGH`/`MEDIUM`/`LOW` — which silently broke high/low sorting once the enum was translated to `ALTA`/`MEDIA`/`BAIXA`/`NENHUMA`, since only `MEDIA` still matched `'M%'`. Fixed 2026-08-14.) If the `Priority` enum names ever change again, these four queries must be updated to match.

### SharedViewModel state shape

`ui/presentation/view_model/SharedViewModel.kt` exposes state as a mix of `mutableStateOf`/`mutableStateListOf` (for Compose) and `StateFlow` (for repository-backed streams):

- `nonePriorityTaskList: SnapshotStateList<ToDoTask>` — manually populated by collecting `repository.getAllTasks()`; `taskRequisitionState` (`RequestState`) only signals success/error/loading, it does **not** hold the data itself. This split is deliberate — see the comment in the file: holding the list directly on the `RequestState` sealed class caused a Compose recomposition bug where `LazyColumn` sometimes didn't refresh after a delete even though the DB write succeeded.
- `lowPriorityTasksOrderList` / `highPriorityTasksOrderList` — `StateFlow`s wired directly to `repository.sortByLowPriority()` / `sortByHighPriority()` via `stateIn(..., SharingStarted.Eagerly, ...)`.
- `mPriority: StateFlow<Priority>` — the currently selected sort order, restored from `DataStoreRepository.readSortState` on init and updated via `persistSortState()`.
- Search results are split into three separate `mutableStateListOf` buckets (`nonePriorityTasksSearch`, `lowPriorityTasksToHighSearch`, `highPriorityTasksToLowSearch`) chosen based on `mPriority.value` — `ListContent.kt` picks which list to render based on both `mPriority` and whether the search bar is open with non-empty text.
- `upsertTaskTitle/Description/Priority/Id` — the in-progress create/edit form fields, shared between `TaskScreen` and `SharedViewModel` (no separate view model per screen).
- `editMode` — true once any field in `UpsertTaskContent` gains focus; used by `UpsertTaskAppBar` to decide whether to show the save (check) icon vs the delete icon.
- `selectMode` / `selectedTasks` — multi-select state on the list screen, toggled by long-press.

### Task screen save/delete rules (`TaskScreen.kt`)

These rules are implemented identically in three places: the app bar's `NO_ACTION`/`UPSERT` handlers and the `BackHandler` — keep them in sync if you touch one:

- Leaving a **new** task (`taskId == -1`) with both title and description empty discards it silently (nothing is ever inserted).
- Leaving an **existing** task with both fields emptied **deletes** it from the DB.
- Otherwise, `compareAndSaveIfModified()` only writes to the DB if title/description/priority actually changed vs. the task as it was opened (`openedTask`, compared with `timeStamp` zeroed out) — this avoids bumping `timeStamp` (and therefore its position in the "most recently edited" order) for untouched tasks.
- `DateFormater.getTimeStampAsLong()` captures full date+time (despite similarly-named `getTimeStampAsString()` only *displaying* the date) — it's what drives "most recently edited task moves to the top."

### Shared UI components

- `ui/components/DeleteTaskBottomSheet.kt` — one bottom-sheet component reused for both single-task delete confirmation (`TaskScreen`) and bulk multi-select delete confirmation (`ListScreen`).
- `ui/components/PriorityItem.kt` — priority dot + label, reused in the sort dropdown (`DefaultAppBar`), the priority picker (`PriorityDropDown`), and implicitly styled via `TaskItem`'s own `Canvas` dot.
- Theme tokens (colors, spacing, shapes) live under `ui/theme/`; most colors are defined as `Colors.xyz` extension properties that branch on `isSystemInDarkTheme()` rather than being baked into the light/dark `Colors` objects directly.

## Testing conventions

- Unit tests (`app/src/test`) use JUnit4 + Truth + Robolectric + `kotlinx-coroutines-test`, with `FakeDispatchers` and `FakeToDoRepository` swapped in via constructor injection (no DI framework needed at this layer). Note `FakeToDoRepository`'s priority-related search/sort methods are `TODO()` stubs — not implemented.
- Instrumented tests (`app/src/androidTest`) use Hilt Android Testing (`HiltTestRunner`, `@HiltAndroidTest`) with an in-memory Room DB provided by `TestAppModule` (qualifier `@Named("test_db")`), and require a connected device/emulator to run.
- `SharedViewModelTest` currently has all its test methods commented out — treat it as scaffolding, not as passing coverage, if you're asked to touch view-model tests.
