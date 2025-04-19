package org.mipt.timetable.ui

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ExportScreen(
    onNavigateBack: () -> Unit, modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            Button(
                onClick = onNavigateBack,
            ) {
                Text("Back to menu")
            }
        },
    ) {

    }
}
