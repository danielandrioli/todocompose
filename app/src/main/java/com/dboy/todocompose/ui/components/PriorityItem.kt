package com.dboy.todocompose.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.theme.LARGE_PADDING
import com.dboy.todocompose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.dboy.todocompose.ui.theme.Typography

@Composable
fun PriorityItem(priority: Priority, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
            drawCircle(color = priority.color)
        }
        Spacer(modifier = Modifier.width(LARGE_PADDING))
        Text(
            text = priority.name,
            style = Typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
@Preview
fun PreviewPriorityItem(){
    PriorityItem(priority = Priority.HIGH)
}