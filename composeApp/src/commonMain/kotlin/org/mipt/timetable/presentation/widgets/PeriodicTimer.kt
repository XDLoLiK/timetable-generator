package org.mipt.timetable.presentation.widgets

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlin.time.Duration

@Composable
fun PeriodicTimer(
    interval: Duration,
    onTick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(isActive) {
        while (isActive) {
            delay(interval)
            onTick()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            isActive = false
        }
    }

    content()
}
