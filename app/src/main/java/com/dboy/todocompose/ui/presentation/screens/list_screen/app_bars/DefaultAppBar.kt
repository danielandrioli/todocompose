package com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.components.PriorityItem
import com.dboy.todocompose.ui.theme.topAppBarBackgroundColor
import com.dboy.todocompose.ui.theme.topAppBarContentColor


@Composable
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    selectedPriorityOrder: Priority
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.default_bar_title),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            DefaultAppBarActions(onSearchClicked, onSortClicked, selectedPriorityOrder)
        })
}

@Composable
fun DefaultAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    selectedPriorityOrder: Priority
) {
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked, selectedPriorityOrder = selectedPriorityOrder)
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = onSearchClicked) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.icon_search),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit,
    selectedPriorityOrder: Priority
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.icon_filter_list),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded, onDismissRequest = {
            expanded = false
        }) {
            DropdownMenuItem(onClick = {
                onSortClicked(Priority.LOW)
                expanded = false
            }) {
                PriorityItem(priority = Priority.LOW, isSelected = Priority.LOW == selectedPriorityOrder)
            }
            DropdownMenuItem(onClick = {
                onSortClicked(Priority.HIGH)
                expanded = false
            }) {
                PriorityItem(priority = Priority.HIGH, isSelected = Priority.HIGH == selectedPriorityOrder)
            }
            DropdownMenuItem(onClick = {
                onSortClicked(Priority.NONE)
                expanded = false
            }) {
                PriorityItem(priority = Priority.NONE, isSelected = Priority.NONE == selectedPriorityOrder)
            }
        }
    }
}

@Composable
@Preview
private fun PreviewDefaultListAppBar() {

}