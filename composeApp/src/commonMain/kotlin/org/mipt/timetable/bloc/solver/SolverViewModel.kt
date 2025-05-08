package org.mipt.timetable.bloc.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mipt.timetable.data.repository.TimetableService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SolverViewModel : ViewModel() {
    private val _service: TimetableService = TimetableService("http://0.0.0.0:8080")
    private val _state = MutableStateFlow<SolverState>(SolverState.Idle())
    val state = _state.asStateFlow()

    fun onEvent(event: SolverEvent) {
        when (event) {
            is SolverEvent.SubmitProblem -> onSubmitProblem(event)
            is SolverEvent.SetServerUrl -> onSetServerUrl(event)
        }
    }

    private fun onSubmitProblem(event: SolverEvent.SubmitProblem) {
        viewModelScope.launch {
            val response = _service.sendJson(event.params)

            when (response.status) {
                HttpStatusCode.Created -> _state.update {
                    SolverState.Solving(Uuid.fromByteArray(response.bodyAsBytes()))
                }

                else -> _state.update { SolverState.Error(response.status) }
            }
        }
    }

    private fun onSetServerUrl(event: SolverEvent.SetServerUrl) {
        _service.setUrl(event.url)
    }
}
