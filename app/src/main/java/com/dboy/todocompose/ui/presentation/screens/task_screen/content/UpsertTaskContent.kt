package com.dboy.todocompose.ui.presentation.screens.task_screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.theme.LARGE_PADDING

@Composable
fun UpsertTaskContent(
    taskTitle: String,
    taskDescription: String,
    taskPriority: Priority,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = taskTitle,
            onValueChange = onTitleChange,
            label = {
                Text(text = stringResource(id = R.string.title))
            },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        PriorityDropDown(priority = taskPriority, onPrioritySelected = onPriorityChange)

        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = taskDescription,
            onValueChange = onDescriptionChange,
            label = {
                Text(text = stringResource(id = R.string.description))
            },
            textStyle = MaterialTheme.typography.body1,
        )
    }
}