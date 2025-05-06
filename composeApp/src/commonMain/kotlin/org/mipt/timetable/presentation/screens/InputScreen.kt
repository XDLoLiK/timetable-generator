package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.mipt.timetable.LocalGroupViewModel
import org.mipt.timetable.LocalNavController
import org.mipt.timetable.LocalRoomViewModel
import org.mipt.timetable.LocalTeacherViewModel
import org.mipt.timetable.presentation.widgets.GroupInputTabRoot
import org.mipt.timetable.presentation.widgets.RoomInputTabRoot
import org.mipt.timetable.presentation.widgets.TeacherInputTabRoot

private object InputTab {
    const val ROOMS = 0
    const val GROUPS = 1
    const val TEACHERS = 2
}

@Composable
fun InputScreen() {
    var activeTab by remember { mutableStateOf(0) }
    val navController = LocalNavController.current
    val roomViewModel = LocalRoomViewModel.current
    val groupViewModel = LocalGroupViewModel.current
    val teacherViewModel = LocalTeacherViewModel.current

    Scaffold(
        floatingActionButton = {
            Button(
                onClick = { navController.popBackStack() },
            ) {
                Text("Back to menu")
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = activeTab) {
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
                0 -> RoomInputTabRoot(viewModel = roomViewModel)
                1 -> GroupInputTabRoot(viewModel = groupViewModel)
                2 -> TeacherInputTabRoot(viewModel = teacherViewModel)
            }
        }
    }
}
