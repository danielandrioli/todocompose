package com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dboy.todocompose.R
import com.dboy.todocompose.utils.ListTab

@Composable
fun ListTabRow(
    selectedTab: ListTab,
    onTabSelected: (ListTab) -> Unit
) {
    TabRow(
        selectedTabIndex = if (selectedTab == ListTab.TO_DO) 0 else 1,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary
    ) {
        Tab(
            selected = selectedTab == ListTab.TO_DO,
            onClick = { onTabSelected(ListTab.TO_DO) },
            text = { Text(text = stringResource(id = R.string.tab_a_fazer)) }
        )
        Tab(
            selected = selectedTab == ListTab.DONE,
            onClick = { onTabSelected(ListTab.DONE) },
            text = { Text(text = stringResource(id = R.string.tab_concluidas)) }
        )
    }
}
