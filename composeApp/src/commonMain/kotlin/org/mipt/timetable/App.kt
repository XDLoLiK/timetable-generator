package org.mipt.timetable

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mipt.timetable.bloc.group.GroupViewModel
import org.mipt.timetable.bloc.room.RoomViewModel
import org.mipt.timetable.bloc.solver.SolverViewModel
import org.mipt.timetable.bloc.teacher.TeacherViewModel
import org.mipt.timetable.bloc.timetable.TimetableViewModel
import org.mipt.timetable.presentation.screens.ExportScreen
import org.mipt.timetable.presentation.screens.HomeScreen
import org.mipt.timetable.presentation.screens.InputScreen

object AppLogger {
    val logger = Logger.withTag("TimetableGenerator")
}

val LocalNavController = staticCompositionLocalOf<NavHostController> { error("NavController is not provided") }
val LocalRoomViewModel = staticCompositionLocalOf<RoomViewModel> { error("RoomViewModel is not provided") }
val LocalGroupViewModel = staticCompositionLocalOf<GroupViewModel> { error("GroupViewModel is not provided") }
val LocalTeacherViewModel = staticCompositionLocalOf<TeacherViewModel> { error("TeacherViewModel is not provided") }
val LocalTimetableViewModel = staticCompositionLocalOf<TimetableViewModel> {
    error("TimetableViewModel is not provided")
}
val LocalSolverViewModel = staticCompositionLocalOf<SolverViewModel> { error("SolverViewModel is not provided") }

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val roomViewModel = RoomViewModel()
    val groupViewModel = GroupViewModel()
    val teacherViewModel = TeacherViewModel()
    val timetableViewModel = TimetableViewModel()
    val solverViewModel = SolverViewModel()

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalRoomViewModel provides roomViewModel,
        LocalGroupViewModel provides groupViewModel,
        LocalTeacherViewModel provides teacherViewModel,
        LocalTimetableViewModel provides timetableViewModel,
        LocalSolverViewModel provides solverViewModel,
    ) {
        MaterialTheme {
            NavHost(
                navController = LocalNavController.current,
                startDestination = Route.HOME,
            ) {
                composable(Route.HOME) { HomeScreen() }
                composable(Route.INPUT) { InputScreen() }
                composable(Route.EXPORT) { ExportScreen() }
            }
        }
    }
}
