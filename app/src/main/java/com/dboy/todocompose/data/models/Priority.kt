package com.dboy.todocompose.data.models

import androidx.compose.ui.graphics.Color
import com.dboy.todocompose.ui.theme.*

enum class Priority(val color: Color) {
    ALTA(HighPriorityColor),
    MEDIA(MediumPriorityColor),
    BAIXA(LowPriorityColor),
    NENHUMA(NoPriorityColor)
}
