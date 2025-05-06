package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.mipt.timetable.LocalNavController
import org.mipt.timetable.Route

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current

    Scaffold {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Timetable Generator",
                style = MaterialTheme.typography.h4,
            )
            Spacer(
                modifier = Modifier.height(32.dp)
            )
            Button(
                onClick = { navController.navigate(Route.INPUT) },
                modifier = Modifier.width(100.dp),
            ) {
                Text("Input")
            }
            Button(
                onClick = { navController.navigate(Route.EXPORT) },
                modifier = Modifier.width(100.dp)
            ) {
                Text("Export")
            }
        }
    }
}
