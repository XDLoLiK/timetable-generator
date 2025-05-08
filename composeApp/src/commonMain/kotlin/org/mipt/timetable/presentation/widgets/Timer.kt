package org.mipt.timetable.presentation.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerWidget(
    delay: Duration,
    onTick: () -> Unit,
    content: @Composable () -> Unit  // Your UI content
) {
    // Track if the timer should be active
    val isActive = remember { mutableStateOf(true) }

    // LaunchedEffect for the timer coroutine
    LaunchedEffect(isActive.value) {
        while (isActive.value) {
            delay(delay)
            onTick()
        }
    }

    // DisposableEffect to handle cleanup when leaving composition
    DisposableEffect(Unit) {
        onDispose {
            isActive.value = false
        }
    }

    // Your UI content
    content()
}