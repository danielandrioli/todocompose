package com.dboy.todocompose.ui.presentation.screens.task_screen.content

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.theme.LARGE_PADDING
import com.dboy.todocompose.utils.clearFocusOnKeyboardDismiss

@Composable
fun UpsertTaskContent(
    taskTitle: String,
    taskDescription: String,
    taskPriority: Priority,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onFocusChangeToEditMode: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState() //há vários estados a serem coletados, como click, drag...

    if (isFocused) {
        onFocusChangeToEditMode(true)
//        Log.i("TaskScreen", "Focused")
    } else {
        onFocusChangeToEditMode(false)
//        Log.i("TaskScreen", "Not focused")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().clearFocusOnKeyboardDismiss(),
            interactionSource = interactionSource,
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
            modifier = Modifier
                .fillMaxSize().clearFocusOnKeyboardDismiss(),
            interactionSource = interactionSource,
            value = taskDescription,
            onValueChange = onDescriptionChange,
            label = {
                Text(text = stringResource(id = R.string.description))
            },
            textStyle = MaterialTheme.typography.body1,
        )
    }
}