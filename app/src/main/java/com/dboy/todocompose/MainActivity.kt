package com.dboy.todocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dboy.todocompose.ui.presentation.navigation.SetupNavHost
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.ui.theme.ToDoComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme {
                val endActivity: () -> Unit = {this.finish()}

                navHostController = rememberNavController()
                SetupNavHost(navController = navHostController, sharedViewModel, endActivity)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}