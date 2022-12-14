package com.dboy.todocompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SelectedTaskColor = Color(0xA103DAC5)
val Indigo = Color(0xFF303F9F)
val IndigoDark = Color(0xFF263598)
val IndigoOutro = Color(0xFF3F51B5)
val SecondaryGrey = Color(0xFFC5CAE9)


val EerieBlack = Color(0xFF151719)
val AnotherBlack = Color(0xFF232629)
val SonicSilver = Color(0xFF707070)

val lightGray = Color(0xFFD8D0D0)


val HighPriorityColor = Color(0xFFBE180B)
val MediumPriorityColor = Color(0xFFDA9203)
val LowPriorityColor = Color(0xFF8C9AE5)
val NoPriorityColor = lightGray



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
    get() = if (isSystemInDarkTheme()) lightGray else Color.White

val Colors.topAppBarBackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Indigo else Color.Black

val Colors.deleteButton: Color
    @Composable
    get() = Color(0xFF2055E1)

val Colors.cancelButton: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFD6D8DD) else  Color(0xFF353639)

val Colors.CheckPriority: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Green else Color(0xFF194B1B)

val Colors.SplashScreenBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) background else Indigo