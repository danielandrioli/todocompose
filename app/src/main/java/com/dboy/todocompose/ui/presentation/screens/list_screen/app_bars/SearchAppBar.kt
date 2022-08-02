package com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.dboy.todocompose.R
import com.dboy.todocompose.ui.theme.TOP_APP_BAR_HEIGHT
import com.dboy.todocompose.ui.theme.topAppBarBackgroundColor
import com.dboy.todocompose.ui.theme.topAppBarContentColor

@Composable
fun SearchAppbar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT), //According to MaterialDesign, the height is 56dp.
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        val focusRequester = remember { FocusRequester() }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.6f),
                    text = stringResource(id = R.string.search_placeholder)
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    modifier = Modifier.alpha(0.5f),
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_placeholder),
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            },
            trailingIcon = {
                IconButton(onClick = onCloseClicked) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_action),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
        LaunchedEffect(true) {
            focusRequester.requestFocus()
            //É necessário chamar no LaunchedEffect para evitar múltiplas requisições de foco
        }
    }
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchAppbar(text = "Aew", onTextChange = {}, onCloseClicked = {})
}