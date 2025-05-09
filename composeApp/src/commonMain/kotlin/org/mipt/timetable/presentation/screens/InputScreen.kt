package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mipt.timetable.LocalGroupViewModel
import org.mipt.timetable.LocalNavController
import org.mipt.timetable.LocalRoomViewModel
import org.mipt.timetable.LocalTeacherViewModel
import org.mipt.timetable.presentation.widgets.GroupInputTab
import org.mipt.timetable.presentation.widgets.RoomInputTab
import org.mipt.timetable.presentation.widgets.TeacherInputTab

private object InputTab {
    const val ROOMS = 0
    const val GROUPS = 1
    const val TEACHERS = 2
}

@Composable
fun InputScreen() {
    val navController = LocalNavController.current

    InputScreenImpl(
        onGoBack = { navController.popBackStack() }
    )
}

@Composable
private fun InputScreenImpl(
    onGoBack: () -> Unit = {},
) {
    var activeTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = contentColorFor(MaterialTheme.colors.primary),
                elevation = 8.dp,
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to menu"
                        )
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = activeTab,
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = contentColorFor(MaterialTheme.colors.surface),
            ) {
                Tab(
                    selected = activeTab == InputTab.ROOMS,
                    onClick = { activeTab = InputTab.ROOMS },
                ) {
                    Text("Rooms")
                }
                Tab(
                    selected = activeTab == InputTab.GROUPS,
                    onClick = { activeTab = InputTab.GROUPS },
                ) {
                    Text("Groups")
                }
                Tab(
                    selected = activeTab == InputTab.TEACHERS,
                    onClick = { activeTab = InputTab.TEACHERS },
                ) {
                    Text("Teachers")
                }
            }

            when (activeTab) {
                0 -> RoomInputTab(viewModel = LocalRoomViewModel.current)
                1 -> GroupInputTab(viewModel = LocalGroupViewModel.current)
                2 -> TeacherInputTab(viewModel = LocalTeacherViewModel.current)
            }
        }
    }
}
