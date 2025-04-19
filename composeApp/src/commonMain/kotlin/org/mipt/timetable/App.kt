package org.mipt.timetable

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mipt.timetable.ui.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController, startDestination = "Home"
        ) {
            composable("Home") {
                HomeScreen()
            }

            composable("InputScreen") {

            }

            composable("ExportScreen") {
                
            }
        }
    }
}
