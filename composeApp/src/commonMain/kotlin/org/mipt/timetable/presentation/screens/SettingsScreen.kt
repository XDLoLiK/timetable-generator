package org.mipt.timetable.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mipt.timetable.LocalNavController
import org.mipt.timetable.bloc.settings.SettingsEvent
import org.mipt.timetable.bloc.settings.SettingsState
import org.mipt.timetable.bloc.settings.SettingsViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = SettingsViewModel(),
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsState()

    SettingsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onGoBack = { navController.popBackStack() }
    )
}

@Composable
private fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    onGoBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {
            Text(
                "Server URL",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp).align(Alignment.Start)
            )

            OutlinedTextField(
                value = when (state) {
                    is SettingsState.Saved -> state.settings.serverUrl
                    is SettingsState.Unsaved -> state.changed.serverUrl
                },
                onValueChange = { onEvent(SettingsEvent.UpdateServerUrl(url = it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.align(Alignment.End)) {
                Button(
                    enabled = state is SettingsState.Unsaved,
                    onClick = { onEvent(SettingsEvent.Save()) }
                ) {
                    Text("Save")
                }
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                Button(
                    onClick = {
                        onEvent(SettingsEvent.Reset())
                    }
                ) {
                    Text("Reset")
                }
            }
        }
    }
}
