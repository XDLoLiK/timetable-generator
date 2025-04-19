package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mipt.timetable.LocalNavController
import org.mipt.timetable.Route

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current

    HomeScreenImpl(
        onNavigate = { navController.navigate(it) }
    )
}

@Composable
private fun HomeScreenImpl(
    onNavigate: (String) -> Unit = {},
) {
    Scaffold(
        floatingActionButton = {
            IconButton(onClick = { onNavigate(Route.SETTINGS) }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    ) {
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
                onClick = { onNavigate(Route.INPUT) },
                modifier = Modifier.width(100.dp),
            ) {
                Text("Input")
            }
            Button(
                onClick = { onNavigate(Route.EXPORT) },
                modifier = Modifier.width(100.dp)
            ) {
                Text("Export")
            }
        }
    }
}
