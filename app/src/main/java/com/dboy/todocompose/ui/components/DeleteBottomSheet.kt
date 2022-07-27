package com.dboy.todocompose.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dboy.todocompose.R
import com.dboy.todocompose.ui.theme.cancelButton
import com.dboy.todocompose.ui.theme.deleteButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteTaskBottomSheet(
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    sheetText: String,
    onConfirmDeletionClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .height(210.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = sheetText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.cancelButton),
                        modifier = Modifier.width(140.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }
                        }) {
                        Text(text = stringResource(id = R.string.cancel), fontSize = 18.sp)
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.deleteButton),
                        modifier = Modifier.width(140.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = onConfirmDeletionClick) {
                        Text(text = stringResource(id = R.string.delete), fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }, content = content)
}