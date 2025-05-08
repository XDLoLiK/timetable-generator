package org.mipt.timetable.bloc.solver

import org.mipt.timetable.data.model.PackedParameters
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class SolverEvent {
    data class SubmitProblem(val params: PackedParameters) : SolverEvent()

    @OptIn(ExperimentalUuidApi::class)
    data class UpdateStatus(val id: Uuid) : SolverEvent()
    data class SetServerUrl(val url: String) : SolverEvent()
}
