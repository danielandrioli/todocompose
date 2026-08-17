# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

"EduTask" (package `com.dboy.todocompose`, project folder `todocompose` — unchanged on purpose, see rename note below) is a single-module Android app for creating and managing to-do tasks. A task has a title, description, priority, and timestamp. From the list screen the user can search tasks and sort/filter them by priority. All UI copy and most code comments are in Brazilian Portuguese; there are no other locale resources (`values-en`, etc.) — treat Portuguese as the only supported language unless asked otherwise.

The app was created in 2022 as a Jetpack Compose learning project under the name "To Do Compose", and its dependencies were modernized in August 2026 (see commits "preparing project for modernization" / "Primeira atualizacao de dependencias com sucesso"). Dependency versions are intentionally pinned as-is — don't bump them unless a task specifically requires it.

Renamed "To Do Compose" → "EduTask" on 2026-08-17, since the app moved from a personal study project to something used by students and teachers. Only the display name and in-app text changed (`app_name` in `strings.xml`, `settings.gradle`'s `rootProject.name`, the `Theme.ToDoCompose` style → `Theme.EduTask`, and the `ToDoComposeTheme(...)` composable → `EduTaskTheme(...)`); the project folder, Gradle module path, and package (`com.dboy.todocompose`) were deliberately left as-is. The three logo assets (`logo_dark.xml`, `logo_light.xml`, `ic_launcher_foreground.xml`) had their lettering swapped from "To"/"Do" to "Edu"/"Task" — same badge shape, same colors, letters redrawn in Arial Black (chosen after a lighter "extra-bold" pass looked too thin for the extra characters) and converted to Android path data via a matplotlib/svgpath2mpl script (not hand-authored, unlike this repo's usual small single-path icon convention). The five pairs of legacy pre-Android-8 launcher PNGs under `mipmap-*dpi/` (`ic_launcher.png` / `ic_launcher_round.png`) were also regenerated to match — each file's own existing pin-badge position/scale was measured from its pixels and preserved exactly, only the letter glyphs were redrawn, so square vs. round icons keep their pre-existing (and mutually different) crops.

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
- `list_screen` → `ListScreen` — the home screen: two tabs ("A fazer" / "Concluídas", see "List tabs" below), search, priority sort/filter, multi-select delete, FAB to create a task.
- `task_screen/{taskId}` → `TaskScreen` — create/edit a single task. `taskId == -1` means "new task"; any other id loads/edits an existing task.

There is a single `SharedViewModel` (Hilt `@HiltViewModel`), created once in `MainActivity` and passed down through the whole nav graph — screens do not get their own view models. All cross-screen state (task list, search state, sort state, the in-progress upsert form fields, multi-select state) lives on this one view model.

### Data layer

- `data/models/ToDoTask.kt` — Room `@Entity` (table `todo_table`): `title`, `description`, `priority: Priority`, `timeStamp: Long`, auto-generated `id`, `isDone: Boolean` (defaults `false`; added 2026-08-15 for the task-completion feature — see "Task completion ('done')" below). `isDone` is trailing with a default specifically so existing positional/named call sites (including `ToDoDaoTest.kt`'s positional constructor call) keep compiling if more fields are ever added the same way.
- `data/models/Priority.kt` — enum **in Portuguese**: `ALTA` (high), `MEDIA` (medium), `BAIXA` (low), `NENHUMA` (none). Each variant carries its indicator `Color`. Room persists enums by their `.name`. **UI display note:** the sort dropdown (`DefaultAppBar.kt`) shows the `NENHUMA` option as "DATA" (since that order is actually by timestamp) — this is a display-only override via `PriorityItem`'s `displayName` param; the enum constant itself is still `NENHUMA` everywhere else (DB, DataStore, SQL).
- `data/source/ToDoDao.kt` / `ToDoDatabase.kt` — Room DAO/DB (`todo_db`, version **2**, `exportSchema = false`). `MIGRATION_1_2` (companion object of `ToDoDatabase`, wired in via `AppModule.provideDatabase().addMigrations(...)`) adds the `isDone` column with `ALTER TABLE todo_table ADD COLUMN isDone INTEGER NOT NULL DEFAULT 0`. If you add more columns, follow the same pattern (bump version, add a new `Migration`) — there's still no `fallbackToDestructiveMigration`, so a missed migration crashes on upgrade for existing installs.
- `ToDoDao.getSingleTask(id)` returns `Flow<ToDoTask?>` (nullable) — **not** `Flow<ToDoTask>`. It was non-null until 2026-08-15, which crashed the app (`IllegalStateException: query result was empty...`) whenever the currently-open task got deleted while its `Flow` was still being collected (see next bullet for why that collection outlives the screen). Keep it nullable; `SharedViewModel._task` / `.task` are typed `ToDoTask?` to match.
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
- Search results are split into three separate `mutableStateListOf` buckets (`nonePriorityTasksSearch`, `lowPriorityTasksToHighSearch`, `highPriorityTasksToLowSearch`) chosen based on `mPriority.value` — `ListContent.kt` picks which list to render based on both `mPriority` and whether the search bar is open with non-empty text, then filters that list by `isDone` to match `selectedListTab` (see "List tabs" below). None of these six sources (the three search buckets plus `nonePriorityTaskList`/`lowPriorityTasksOrderList`/`highPriorityTasksOrderList`) are themselves isDone-aware, nor are the underlying DAO queries — the tab filter is applied client-side in `ListContent.kt` on top of whatever list this bullet's logic already picked.
- `selectedListTab: MutableState<ListTab>` — which of the two list tabs is active (`ListTab.TO_DO` default, or `ListTab.DONE`); see "List tabs" below.
- `upsertTaskTitle/Description/Priority/Id/Done` — the in-progress create/edit form fields, shared between `TaskScreen` and `SharedViewModel` (no separate view model per screen). `upsertTaskDone` was added alongside `ToDoTask.isDone`.
- `editMode` — true once any field in `UpsertTaskContent` gains focus; used by `UpsertTaskAppBar` to decide whether to show the save icon vs the delete icon (the "done" icon is independent of `editMode` — see below).
- `selectMode` / `selectedTasks` — multi-select state on the list screen, toggled by long-press.
- `getSingleTaskJob: Job?` — the job backing `getSingleTaskFromDb(id)` is cancelled and reassigned every call. This matters because `SharedViewModel` is never recreated between screens (single VM for the whole nav graph): without cancelling the previous job, opening task A then task B would leave *two* concurrent collectors of `repository.getSingleTask(...)` writing into the same `_task`, and the stale one from task A would keep running (and could crash, per the `getSingleTask` nullability note above) even after leaving that screen. If you add other "load a single thing into shared state" functions, follow this same cancel-before-launch pattern.

### Task screen save/delete rules (`TaskScreen.kt`)

These rules are implemented identically in three places: the app bar's `NO_ACTION`/`UPSERT` handlers and the `BackHandler` — keep them in sync if you touch one:

- Leaving a **new** task (`taskId == -1`) with both title and description empty discards it silently (nothing is ever inserted).
- Leaving an **existing** task with both fields emptied **deletes** it from the DB.
- Otherwise, `compareAndSaveIfModified()` only writes to the DB if title/description/priority/isDone actually changed vs. the task as it was opened (`openedTask`, compared with `timeStamp` zeroed out) — this avoids bumping `timeStamp` (and therefore its position in the "most recently edited" order) for untouched tasks. Being a full `data class` diff, this picks up any new `ToDoTask` field automatically as long as the field is wired into `getCurrentTask()`/`updateTextFields()`/`cleanCurrentTextFields()`.
- `DateFormater.getTimeStampAsLong()` captures full date+time (despite similarly-named `getTimeStampAsString()` only *displaying* the date) — it's what drives "most recently edited task moves to the top." `getTimeStampAsString()` formats with `Locale("pt", "BR")` (pattern `dd 'de' MMM 'de' yyyy`, e.g. "14 de ago. de 2026") — it used to use the device's default locale, which showed English dates ("Aug 14, 2026") on non-pt-BR devices; fixed 2026-08-14.
- **`Action.DONE`** (toggling task completion) does **not** go through `compareAndSaveIfModified()` — `toggleTaskDone()` flips `upsertTaskDone` and calls `upSertTask(getCurrentTask())` directly, unconditionally, so it always persists immediately (and always bumps `timeStamp`, same as any other edit) regardless of whether title/description were touched.

### Task completion ("done")

Added 2026-08-15. A task can be marked/unmarked done only from `TaskScreen` (not from the list):

- `SharedViewModel.toggleTaskDone()` flips `upsertTaskDone` and persists via `upSertTask` (see above). `TaskScreen` handles `Action.DONE` by calling it and showing a toast ("Tarefa concluída!"/"Tarefa reaberta!") — no `popBackStack()`, the user stays on the screen to see the change.
- App bar icon (`DoneAction` in `TaskAppBarActions.kt`, wired into `UpsertTaskAppBar.kt`'s `actions` row): shows `Icons.Filled.Done` when the task is **not** done (tap to mark done), or the custom drawable `R.drawable.ic_undo` (a hand-authored circular-arrow "undo" vector, `res/drawable/ic_undo.xml`) when it **is** done (tap to reopen). Both states are tinted with the plain `topAppBarContentColor` — **not** `taskDoneColor` — per explicit user preference (an earlier green-tinted version was rejected in favor of matching the other app bar icons).
- `DoneAction` renders whenever `!newTask` (a task that doesn't exist in the DB yet has nothing to mark done), in **both** edit and view mode, always positioned as the *first* action — i.e. to the left of whichever save/delete icon is showing. This ordering was explicitly requested; if the `actions { }` block in `UpsertTaskAppBar.kt` is touched again, keep `DoneAction` first.
- Visual indicators of a done task (separate from the app bar icon, and these *do* use `taskDoneColor`, the green theme token in `Color.kt`): in `TaskItem.kt` (list row) the title gets `TextDecoration.LineThrough` + `alpha(0.5f)`, the description gets `alpha(0.5f)`, and the priority-color dot is replaced by a green `Icons.Filled.CheckCircle`. In `UpsertTaskContent.kt` a green "Concluída" pill shows below the title field. Done tasks are **not** filtered/hidden/reordered anywhere — they stay in place in the normal list, only their look changes.
- The save icon (`UpsertAction`, shown when `editMode || newTask`) uses another custom drawable, `R.drawable.ic_save` (floppy disk), not `Icons.Filled.Check` — same reasoning as `ic_undo` below.
- **Icon convention:** the app depends only on `androidx.compose.material:material-icons-core` (see `app/build.gradle`), not `material-icons-extended` — that dependency is intentionally *not* added (large library for a couple of icons). Core only ships a small curated icon set (`Check`, `Done`, `CheckCircle`, `Delete`, `ArrowBack`, etc. are in it; `Save`, `Undo` are not). When a needed icon isn't in core, the established pattern is a small hand-authored vector drawable under `res/drawable/` (following `ic_filter_list.xml`'s style: 24dp, single `<path>`, `android:tint="@color/white"`) rendered via `painterResource(...)` + `Icon(painter = ..., tint = ...)`, rather than pulling in the extended icons library. `ic_save.xml` and `ic_undo.xml` follow this pattern.

### List tabs ("A fazer" / "Concluídas")

Added 2026-08-17. `ListScreen` shows two tabs, "A fazer" (not-done tasks, left, default-selected) and "Concluídas" (done tasks, right) — filtering is purely by `ToDoTask.isDone`, done tasks are never hidden or reordered, they just live in the other tab.

- `utils/ListTab.kt` — plain UI-only enum, `ListTab.TO_DO` / `ListTab.DONE`. Not persisted (no DataStore/DB involvement) — always resets to `TO_DO` on process death, unlike `mPriority` which is restored from DataStore.
- `ui/presentation/screens/list_screen/app_bars/ListTabRow.kt` — a Material2 `TabRow` (default underline indicator, not a custom-drawn one) with `backgroundColor = MaterialTheme.colors.surface` and `contentColor = MaterialTheme.colors.primary`. It deliberately does **not** reuse `topAppBarBackgroundColor`/`topAppBarContentColor`: in the light theme `topAppBarBackgroundColor` *is* `Indigo`, same as `MaterialTheme.colors.primary`, so a primary-colored indicator would be invisible against a same-colored background. Sitting on `colors.surface` instead (white in light theme, dark surface in dark theme) keeps the indicator visible in both themes. Both tabs use the same accent color when selected (not a green `taskDoneColor` accent on "Concluídas") — this was an explicit choice to keep the tab row visually simple/consistent rather than mixing two "selected" colors in one row.
- `ListScreen.kt` renders `ListTabRow` inside the `Scaffold`'s `topBar` slot, in a `Column` together with `ListAppBar(...)` — i.e. pinned below whichever app bar variant (`DefaultAppBar`/`SearchAppbar`/`SelectTasksAppBar`) is currently showing, not inside the scrollable content. This is why the tab row stays visible and clickable even while the search bar is open or multi-select is active.
- `SharedViewModel.selectListTab(tab)` is the single entry point for switching tabs (called from `ListTabRow`'s `onTabSelected`) and does three things: sets `selectedListTab`, closes the search bar via `cleanSearchBar()` if it was open, and cancels multi-select (`selectMode.value = false; selectedTasks.clear()`) if it was active. Both side effects were explicit product decisions (not just convenience): switching tabs while searching would otherwise leave a stale/mismatched search open, and switching tabs mid-selection would otherwise leave `selectedTasks` pointing at task ids that may no longer be visible in the new tab.
- `ListContent.kt` applies the tab filter as the very last step when computing `taskList`, via `.filter { it.isDone == (selectedListTab == ListTab.DONE) }`, after already picking (by priority / search-vs-not, per the `mPriority`/`searchAppBarState` logic described above). This is intentionally the same client-side-`filter` approach as the rest of `ListContent.kt`'s list selection — no DAO/repository changes were made for this feature, so all six upstream list sources remain isDone-agnostic; if you ever add DB-level isDone filtering, this `.filter` call is what it would replace.
- The empty state shown when a tab has zero tasks is the same one used everywhere else (`R.string.empty_list` / `nothing.png`) — there's no tab-specific empty-state copy (e.g. "Concluídas" empty still says "Sua lista de tarefas está vazia."), per explicit product decision to keep the existing empty state as-is rather than add tab-specific strings.

### Shared UI components

- `ui/components/DeleteTaskBottomSheet.kt` — one bottom-sheet component reused for both single-task delete confirmation (`TaskScreen`) and bulk multi-select delete confirmation (`ListScreen`).
- `ui/components/PriorityItem.kt` — priority dot + label, reused in the sort dropdown (`DefaultAppBar`), the priority picker (`PriorityDropDown`), and implicitly styled via `TaskItem`'s own `Canvas` dot. Its `displayName` param (defaults to `priority.name`) is what lets the sort dropdown show "DATA" instead of "NENHUMA" without touching the enum (see Priority note above).
- Theme tokens (colors, spacing, shapes) live under `ui/theme/`; most colors are defined as `Colors.xyz` extension properties that branch on `isSystemInDarkTheme()` rather than being baked into the light/dark `Colors` objects directly. `taskDoneColor` (green, light `#2E7D32` / dark `#4CAF50`) is the done-task indicator color — used for the list/content indicators above, deliberately *not* reused for the app bar's done/undo icon.

## Testing conventions

- Unit tests (`app/src/test`) use JUnit4 + Truth + Robolectric + `kotlinx-coroutines-test`, with `FakeDispatchers` and `FakeToDoRepository` swapped in via constructor injection (no DI framework needed at this layer). Note `FakeToDoRepository`'s priority-related search/sort methods are `TODO()` stubs — not implemented.
- Instrumented tests (`app/src/androidTest`) use Hilt Android Testing (`HiltTestRunner`, `@HiltAndroidTest`) with an in-memory Room DB provided by `TestAppModule` (qualifier `@Named("test_db")`), and require a connected device/emulator to run.
- `SharedViewModelTest` currently has all its test methods commented out — treat it as scaffolding, not as passing coverage, if you're asked to touch view-model tests.
