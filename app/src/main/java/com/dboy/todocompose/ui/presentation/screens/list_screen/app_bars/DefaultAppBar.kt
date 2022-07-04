package com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.components.PriorityItem
import com.dboy.todocompose.ui.theme.LARGE_PADDING
import com.dboy.todocompose.ui.theme.Typography
import com.dboy.todocompose.ui.theme.topAppBarBackgroundColor
import com.dboy.todocompose.ui.theme.topAppBarContentColor


@Composable
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAll: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.default_bar_title),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            DefaultAppBarActions(onSearchClicked, onSortClicked, onDeleteAll)
        })
}

@Composable
fun DefaultAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAll: () -> Unit
) {
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAll = onDeleteAll)
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
    onSortClicked: (Priority) -> Unit
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
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(onClick = {
                onSortClicked(Priority.HIGH)
                expanded = false
            }) {
                PriorityItem(priority = Priority.HIGH)
            }
            DropdownMenuItem(onClick = {
                onSortClicked(Priority.NONE)
                expanded = false
            }) {
                PriorityItem(priority = Priority.NONE)
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAll: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = stringResource(id = R.string.icon_dropdown),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            modifier = Modifier.width(180.dp),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }) {
            DropdownMenuItem(onClick = {
                onDeleteAll()
                expanded = false
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.icon_delete_list)
                )
                Spacer(modifier = Modifier.width(LARGE_PADDING))
                Text(
                    text = stringResource(id = R.string.icon_delete_list),
                    style = Typography.subtitle1
                )
            }

        }
    }
}

@Composable
@Preview
private fun PreviewDefaultListAppBar() {

}