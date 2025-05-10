package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.mipt.timetable.*
import org.mipt.timetable.bloc.settings.SettingsState
import org.mipt.timetable.bloc.solver.SolverEvent
import org.mipt.timetable.bloc.solver.SolverState
import org.mipt.timetable.data.model.PackedParameters
import org.mipt.timetable.presentation.widgets.PeriodicTimer
import org.mipt.timetable.util.exportExcel
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ExportScreen() {
    val navController = LocalNavController.current
    val solverViewModel = LocalSolverViewModel.current
    val settingsViewModel = LocalSettingsViewMoel.current
    val solverState by solverViewModel.state.collectAsState()
    val settingsState by settingsViewModel.state.collectAsState()

    ExportScreenImpl(
        settings = settingsState,
        state = solverState,
        onEvent = solverViewModel::onEvent,
        onGoBack = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun ExportScreenImpl(
    settings: SettingsState,
    state: SolverState,
    onEvent: (SolverEvent) -> Unit,
    onGoBack: () -> Unit = {},
) {
    val roomState by LocalRoomViewModel.current.state.collectAsState()
    val groupState by LocalGroupViewModel.current.state.collectAsState()
    val teacherState by LocalTeacherViewModel.current.state.collectAsState()

    var isExporting by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var exportFile by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export") },
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
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (state) {
                is SolverState.Idle -> {
                    Button(
                        onClick = {
                            onEvent(
                                SolverEvent.SubmitProblem(
                                    PackedParameters(
                                        roomState.rooms.values.toList(),
                                        teacherState.teachers.values.toList(),
                                        groupState.groups.values.toList()
                                    )
                                )
                            )
                        },
                    ) {
                        Text("Generate")
                    }
                }

                is SolverState.Solving -> {
                    PeriodicTimer(
                        interval = 2.seconds,
                        onTick = { onEvent(SolverEvent.UpdateStatus(state.uuid)) }
                    ) {
                        CircularProgressIndicator(Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generating timetable...")
                        Text("Job id is ${state.uuid}")
                    }
                }

                is SolverState.Solved -> {
                    Text(
                        "Timetable ready: ${state.result}"
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Button(
                        onClick = {
                            onEvent(
                                SolverEvent.SubmitProblem(
                                    PackedParameters(
                                        roomState.rooms.values.toList(),
                                        teacherState.teachers.values.toList(),
                                        groupState.groups.values.toList()
                                    )
                                )
                            )
                        }
                    ) {
                        Text("Regenerate")
                    }

                    val launcher = rememberFilePickerLauncher { file ->
                        exportFile = file.toString()
                        try {
                            exportExcel(exportFile, state.result)
                        } catch (e: Exception) {
                            onEvent(SolverEvent.SetError(e.toString()))
                        }
                        isExporting = false
                        showDialog = true
                    }

                    Button(
                        enabled = !isExporting,
                        onClick = {
                            isExporting = true
                            launcher.launch()
                        }
                    ) {
                        Text("Export")
                    }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Export Successful") },
                            text = { Text("File saved to: $exportFile") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialog = false
                                    }
                                ) {
                                    Text("Ok")
                                }
                            }
                        )
                    }
                }

                is SolverState.Error -> {
                    Text(
                        "Error occurred: ${state.message}",
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Button(
                        onClick = {
                            onEvent(
                                SolverEvent.SubmitProblem(
                                    PackedParameters(
                                        roomState.rooms.values.toList(),
                                        teacherState.teachers.values.toList(),
                                        groupState.groups.values.toList()
                                    )
                                )
                            )
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
