package org.mipt.timetable.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun InputScreen(
    onNavigateBack: () -> Unit, modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {
            Button(
                onClick = onNavigateBack,
            ) {
                Text("Back to menu")
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = activeTab) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                ) {
                    Text("Rooms")
                }
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                ) {
                    Text("Groups")
                }
                Tab(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                ) {
                    Text("Teachers")
                }
            }

            when (activeTab) {
                0 -> Text("Rooms")
                1 -> Text("Groups")
                2 -> Text("Teachers")
            }
        }
    }
}
