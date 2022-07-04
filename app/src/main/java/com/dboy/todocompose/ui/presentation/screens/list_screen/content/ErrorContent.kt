package com.dboy.todocompose.ui.presentation.screens.list_screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.dboy.todocompose.R
import com.dboy.todocompose.ui.theme.EMPTY_LIST_ICON_SIZE
import com.dboy.todocompose.ui.theme.ToDoComposeTheme

@Composable
fun ErrorContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(EMPTY_LIST_ICON_SIZE),
            tint = MaterialTheme.colors.onSurface,
            painter = painterResource(id = R.drawable.ic_sentiment_dissatisfied),
            contentDescription = stringResource(
                id = R.string.sad_face_icon
            )
        )
        Text(
            text = stringResource(id = R.string.error_message),
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.h6.fontSize
        )
    }
}

@Preview
@Composable
fun PreviewErrorContent() {
    ToDoComposeTheme {
        ErrorContent()
    }
}