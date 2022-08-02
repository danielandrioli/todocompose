package com.dboy.todocompose.ui.presentation.screens.task_screen.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.components.PriorityItem
import com.dboy.todocompose.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import com.dboy.todocompose.ui.theme.SMALL_PADDING
import com.dboy.todocompose.ui.theme.Shapes
import com.dboy.todocompose.ui.theme.ToDoComposeTheme

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val rotation: Float by animateFloatAsState(
        targetValue =
        if (expanded) 180f else 0f
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(PRIORITY_DROP_DOWN_HEIGHT)
            .clickable { expanded = true }
            .border(
                shape = Shapes.small,
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            .padding(start = SMALL_PADDING),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PriorityItem(priority = priority, Modifier.weight(8f), stringResource(R.string.priority))
        IconButton(
            onClick = {
                expanded = !expanded
            },
            modifier = Modifier
                .alpha(0.6f)
                .rotate(rotation)
                .weight(1.5f)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.icon_dropdown)
            )
        }
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(0.91f),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onPrioritySelected(Priority.LOW)
            }) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onPrioritySelected(Priority.MEDIUM)
            }) {
                PriorityItem(priority = Priority.MEDIUM)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onPrioritySelected(Priority.HIGH)
            }) {
                PriorityItem(priority = Priority.HIGH)
            }
        }
    }
}

@Composable
@Preview
fun PreviewPriorityDropDown() {
    ToDoComposeTheme {
        PriorityDropDown(priority = Priority.HIGH, onPrioritySelected = {})
    }
}