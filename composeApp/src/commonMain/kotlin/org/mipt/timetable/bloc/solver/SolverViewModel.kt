package org.mipt.timetable.bloc.solver

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mipt.timetable.bloc.settings.SettingsState
import org.mipt.timetable.bloc.settings.SettingsViewModel
import org.mipt.timetable.data.model.SolverStatus
import org.mipt.timetable.data.repository.TimetableService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SolverViewModel(private val settingsFlow: StateFlow<SettingsState>) : ViewModel() {
    private var _service: TimetableService? = null
    private val _state = MutableStateFlow<SolverState>(SolverState.Idle())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsFlow.collect {
                if (it is SettingsState.Saved) {
                    _service = TimetableService(it.settings.serverUrl)
                }
            }
        }
    }

    fun onEvent(event: SolverEvent) {
        when (event) {
            is SolverEvent.SubmitProblem -> onSubmitProblem(event)
            is SolverEvent.UpdateStatus -> onUpdateStatus(event)
            is SolverEvent.SetError -> onSetError(event)
        }
    }

    private fun onSubmitProblem(event: SolverEvent.SubmitProblem) {
        viewModelScope.launch {
            val response = _service!!.submitProblem(event.params)

            _state.update {
                if (response.status.isSuccess()) {
                    SolverState.Solving(Uuid.parse(response.body<String>()))
                } else {
                    SolverState.Error(response.status.toString())
                }
            }
        }
    }

    private fun onUpdateStatus(event: SolverEvent.UpdateStatus) {
        viewModelScope.launch {
            val id = event.id
            val statusResponse = _service!!.checkStatus(id)

            if (statusResponse.status.isSuccess()) {
                val status = statusResponse.body<SolverStatus>()

                when (status) {
                    SolverStatus.NOT_SOLVING -> {
                        val resultResponse = _service!!.getSolution(id)

                        _state.update {
                            if (resultResponse.status.isSuccess()) {
                                SolverState.Solved(resultResponse.body())
                            } else {
                                SolverState.Error(resultResponse.status.toString())
                            }
                        }
                    }

                    else -> {}
                }
            } else {
                _state.update {
                    SolverState.Error(statusResponse.status.toString())
                }
            }
        }
    }


    private fun onSetError(event: SolverEvent.SetError) {
        _state.update {
            SolverState.Error(event.error)
        }
    }
}
