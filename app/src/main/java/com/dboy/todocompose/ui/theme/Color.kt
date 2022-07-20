package com.dboy.todocompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Charcoal = Color(0xFF41515A)
val SonicSilver = Color(0xFF707070)
val DavysGrey = Color(0xFF535555)
val EerieBlack = Color(0xFF151719)
val IlluminatingEmerald = Color(0xFF428F77)
val lightGray = Color(0xFFD8D0D0)



val HighPriorityColor = Color(0xFFBE180B)
val MediumPriorityColor = Color(0xFFDA9203)
val LowPriorityColor = Color(0xFF72ADA8)
val NoPriorityColor = Color(0xFFFFFFFF)

val Colors.taskItemTextColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

val Colors.taskItemTitleColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.Black else Color.White

val Colors.taskItemBackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else Color.DarkGray

val Colors.topAppBarContentColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else lightGray

val Colors.topAppBarBackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) SonicSilver else Color.Black

val Colors.deleteButton: Color
    @Composable
    get() = Color(0xFF2055E1)

val Colors.cancelButton: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFD6D8DD) else  Color(0xFF353639)