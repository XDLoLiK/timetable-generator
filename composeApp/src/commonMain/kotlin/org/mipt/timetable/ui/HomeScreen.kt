package org.mipt.timetable.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit, modifier: Modifier = Modifier
) {
    Scaffold {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = { onNavigate(Route.INPUT) },
                modifier = Modifier.width(100.dp),
            ) {
                Text("Input")
            }
            Button(
                onClick = { onNavigate(Route.EXPORT) }, modifier = Modifier.width(100.dp)
            ) {
                Text("Export")
            }
        }
    }
}
