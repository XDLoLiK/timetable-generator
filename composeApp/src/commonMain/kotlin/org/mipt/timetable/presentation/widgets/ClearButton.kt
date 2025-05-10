package org.mipt.timetable.presentation.widgets

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*

@Composable
fun ClearButtonWithConfirmation(
    onConfirmed: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showDialog = true
        },
    ) {
        Icon(
            Icons.Default.Clear,
            contentDescription = "Clear"
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Action") },
            text = { Text("Are you sure you want to clear this tab?") },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmed()
                        showDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
