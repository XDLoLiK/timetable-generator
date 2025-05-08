package org.mipt.timetable

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mipt.timetable.bloc.group.GroupViewModel
import org.mipt.timetable.bloc.room.RoomViewModel
import org.mipt.timetable.bloc.settings.SettingsViewModel
import org.mipt.timetable.bloc.solver.SolverViewModel
import org.mipt.timetable.bloc.teacher.TeacherViewModel
import org.mipt.timetable.bloc.timetable.TimetableViewModel
import org.mipt.timetable.presentation.screens.ExportScreenRoot
import org.mipt.timetable.presentation.screens.HomeScreenRoot
import org.mipt.timetable.presentation.screens.InputScreenRoot
import org.mipt.timetable.presentation.screens.SettingsScreenRoot

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
val LocalSettingsViewMoel = staticCompositionLocalOf<SettingsViewModel> { error("SettingsViewModel is not provided") }

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val roomViewModel = remember { RoomViewModel() }
    val groupViewModel = remember { GroupViewModel() }
    val teacherViewModel = remember { TeacherViewModel() }
    val timetableViewModel = remember { TimetableViewModel() }
    val solverViewModel = remember { SolverViewModel() }
    val settingsViewModel = remember { SettingsViewModel() }

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalRoomViewModel provides roomViewModel,
        LocalGroupViewModel provides groupViewModel,
        LocalTeacherViewModel provides teacherViewModel,
        LocalTimetableViewModel provides timetableViewModel,
        LocalSolverViewModel provides solverViewModel,
        LocalSettingsViewMoel provides settingsViewModel,
    ) {
        MaterialTheme {
            NavHost(
                navController = LocalNavController.current,
                startDestination = Route.HOME,
            ) {
                composable(Route.HOME) { HomeScreenRoot() }
                composable(Route.INPUT) { InputScreenRoot() }
                composable(Route.EXPORT) {
                    ExportScreenRoot(
                        solverViewModel = solverViewModel,
                        settingsViewModel = settingsViewModel,
                    )
                }
                composable(Route.SETTINGS) { SettingsScreenRoot(settingsViewModel) }
            }
        }
    }
}
