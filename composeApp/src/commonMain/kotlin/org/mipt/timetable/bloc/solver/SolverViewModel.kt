package org.mipt.timetable.bloc.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mipt.timetable.data.model.SolverStatus
import org.mipt.timetable.data.repository.TimetableService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SolverViewModel : ViewModel() {
    private val _state = MutableStateFlow<SolverState>(SolverState.Idle())
    private val _service: TimetableService = TimetableService("http://0.0.0.0:8080")
    val state = _state.asStateFlow()

    fun onEvent(event: SolverEvent) {
        when (event) {
            is SolverEvent.SubmitProblem -> onSubmitProblem(event)
            is SolverEvent.UpdateStatus -> onUpdateStatus(event)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onSubmitProblem(event: SolverEvent.SubmitProblem) {
        viewModelScope.launch {
            val response = _service.submitProblem(event.params);

            _state.update {
                if (response.status.isSuccess()) {
                    SolverState.Solving(Uuid.parse(response.body<String>()))
                } else {
                    SolverState.Error(response.status)
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onUpdateStatus(event: SolverEvent.UpdateStatus) {
        viewModelScope.launch {
            val id = event.id
            val statusResponse = _service.checkStatus(id)

            if (statusResponse.status.isSuccess()) {
                val status = statusResponse.body<SolverStatus>();

                when (status) {
                    SolverStatus.NOT_SOLVING -> {
                        val resultResponse = _service.getSolution(id)

                        _state.update {
                            if (resultResponse.status.isSuccess()) {
                                SolverState.Solved(resultResponse.body())
                            } else {
                                SolverState.Error(resultResponse.status)
                            }
                        }
                    }

                    else -> {}
                }
            } else {
                _state.update {
                    SolverState.Error(statusResponse.status)
                }
            }
        }
    }
}
