package com.dboy.todocompose.ui.presentation.screens.list_screen.content

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.ui.presentation.navigation.Screen
import com.dboy.todocompose.ui.theme.*
import com.dboy.todocompose.utils.DateFormater
import com.dboy.todocompose.utils.SearchAppBarState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navController: NavHostController,
    isTaskSelected: Boolean,
    isSelectModeOn: Boolean,
    searchAppBarState: SearchAppBarState,
    selectTask: (Int) -> Unit
) {
    Log.i("ListContent", "TaskItem: ${toDoTask.id} - isTaskSelected: $isTaskSelected")
    Surface(modifier = Modifier.fillMaxWidth().combinedClickable(
        onClick = {
            if (isSelectModeOn) {
                selectTask(toDoTask.id)
            } else Screen.Task.goToTaskScreen(navController, toDoTask.id)
        }, onLongClick = {
            if (searchAppBarState == SearchAppBarState.OPENED) {
                Screen.Task.goToTaskScreen(navController, toDoTask.id)
            } else selectTask(toDoTask.id)
    }),
        color = if (isTaskSelected) SelectedTaskColor else MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = TASK_ITEM_ELEVATION,
        ) {
        Column(
            modifier = Modifier
                .padding(LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = toDoTask.title,
                    color = MaterialTheme.colors.taskItemTitleColor,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box {
                    Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
                        drawCircle(color = toDoTask.priority.color)
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                color = MaterialTheme.colors.taskItemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = DateFormater.getTimeStampAsString(toDoTask.timeStamp),
//                    text = toDoTask.timeStamp.toString(),
                    color = MaterialTheme.colors.taskItemTextColor,
                    style = MaterialTheme.typography.subtitle2.copy(fontSize = 12.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TaskItemPreview() {
    val navController = rememberNavController()
    val task = ToDoTask(
        title = "Testing",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ",
        priority = Priority.MEDIUM,
        timeStamp = 0L
    )
    TaskItem(toDoTask = task, navController = navController, false, false,SearchAppBarState.OPENED) {

    }
}