package com.dboy.todocompose.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.theme.CheckPriority
import com.dboy.todocompose.ui.theme.LARGE_PADDING
import com.dboy.todocompose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.dboy.todocompose.ui.theme.Typography

@Composable
fun PriorityItem(
    priority: Priority, modifier: Modifier = Modifier,
    priorityText: String = "",
    isSelected: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
            drawCircle(color = priority.color)
        }
        Spacer(modifier = Modifier.width(LARGE_PADDING))
        Text(
            text = priority.name + " " + priorityText,
            style = Typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check, contentDescription = stringResource(id = R.string.check),
                tint = MaterialTheme.colors.CheckPriority, modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)
            )
        }
    }
}

@Composable
@Preview
fun PreviewPriorityItem() {
    PriorityItem(priority = Priority.HIGH, isSelected = true)
}