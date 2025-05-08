package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.mipt.timetable.*
import org.mipt.timetable.bloc.settings.SettingsState
import org.mipt.timetable.bloc.solver.SolverEvent
import org.mipt.timetable.bloc.solver.SolverState
import org.mipt.timetable.data.model.PackedParameters
import org.mipt.timetable.presentation.widgets.TimerWidget
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ExportScreenRoot() {
    val navController = LocalNavController.current
    val solverViewModel = LocalSolverViewModel.current
    val settingsViewModel = LocalSettingsViewMoel.current
    val solverState by solverViewModel.state.collectAsState()
    val settingsState by settingsViewModel.state.collectAsState()

    ExportScreen(
        solverState = solverState,
        settingsState = settingsState,
        onEvent = solverViewModel::onEvent,
        onGoBack = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun ExportScreen(
    solverState: SolverState,
    settingsState: SettingsState,
    onEvent: (SolverEvent) -> Unit,
    onGoBack: () -> Unit = {},
) {
    val roomState by LocalRoomViewModel.current.state.collectAsState()
    val groupState by LocalGroupViewModel.current.state.collectAsState()
    val teacherState by LocalTeacherViewModel.current.state.collectAsState()

    var filePath by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("Timetable.xlsx") }
    val isExporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf(false) }
    var exportFile by remember { mutableStateOf("") }

    val serverUrl = when (settingsState) {
        is SettingsState.Saved -> settingsState.settings.serverUrl
        is SettingsState.Unsaved -> settingsState.settings.serverUrl
    }
    LaunchedEffect(serverUrl) {
        onEvent(SolverEvent.SetServerUrl(serverUrl))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = contentColorFor(MaterialTheme.colors.primary),
                elevation = 12.dp,
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
            when (solverState) {
                is SolverState.Idle -> {
                    Text(
                        "Export to Excel",
                        style = MaterialTheme.typography.h3
                    )

                    val launcher = rememberFilePickerLauncher { file ->
                        exportFile = file.toString()
                    }
                    Button(
                        onClick = { launcher.launch() },
                        enabled = !isExporting,
                        modifier = Modifier.padding(start = 8.dp),
                    ) {
                        Text("Choose File")
                    }

                    OutlinedTextField(
                        value = filePath,
                        onValueChange = { filePath = it },
                        label = { Text("Save to folder") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = fileName,
                        onValueChange = { fileName = it },
                        label = { Text("File name") },
                        modifier = Modifier.fillMaxWidth()
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
                        },
                        enabled = filePath.isNotBlank() && !isExporting,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Export")
                    }

                    if (exportSuccess) {
                        AlertDialog(
                            onDismissRequest = { exportSuccess = false },
                            title = { Text("Export Successful") },
                            text = { Text("File saved to: ${filePath + fileName}") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        exportSuccess = false
                                        onGoBack()
                                    }
                                ) {
                                    Text("Ok")
                                }
                            }
                        )
                    }
                }

                is SolverState.Solving -> {
                    val id = solverState.uuid

                    TimerWidget(delay = 2.seconds, onTick = { onEvent(SolverEvent.UpdateStatus(id)) }) {
                        CircularProgressIndicator(Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Solving...")
                        Text("Your uuid is $id")
                    }
                }

                is SolverState.Solved -> {
                    Text("Solved!")
                    Text("Your solution is ${solverState.result}")
                }

                is SolverState.Error -> {
                    Text("Error code: $solverState")
                }
            }
        }
    }
}
