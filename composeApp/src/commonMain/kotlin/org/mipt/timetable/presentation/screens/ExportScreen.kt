package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mipt.timetable.*
import org.mipt.timetable.bloc.solver.SolverEvent
import org.mipt.timetable.bloc.solver.SolverState
import org.mipt.timetable.data.model.PackedParameters
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ExportScreen() {
    val navController = LocalNavController.current

    val roomState by LocalRoomViewModel.current.state.collectAsState()
    val groupState by LocalGroupViewModel.current.state.collectAsState()
    val teacherState by LocalTeacherViewModel.current.state.collectAsState()

    val solverViewModel = LocalSolverViewModel.current
    val solverState by solverViewModel.state.collectAsState()

    var filePath by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("Timetable.xlsx") }
    var isExporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Button(
                onClick = { navController.popBackStack() },
            ) {
                Text("Back to menu")
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (solverState) {
                is SolverState.Idle -> {
                    Text(
                        "Export to Excel",
                        style = MaterialTheme.typography.h3
                    )

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
                        onClick =
                            {
                                solverViewModel.onEvent(
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
                                        navController.popBackStack()
                                    }
                                ) {
                                    Text("Ok")
                                }
                            }
                        )
                    }
                }
                is SolverState.Solving -> {
                    CircularProgressIndicator(Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Exporting...")
                    Text("Your uuid is ${(solverState as SolverState.Solving).uuid.toString()}")
                }
                is SolverState.Error -> {
                    Text("Error code: ${solverState.toString()}")
                }
            }
        }
    }
}
