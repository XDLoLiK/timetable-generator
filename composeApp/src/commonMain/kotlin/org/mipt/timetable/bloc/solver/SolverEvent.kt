package org.mipt.timetable.bloc.solver

import org.mipt.timetable.data.model.PackedParameters
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class SolverEvent {
    data class SubmitProblem(val params: PackedParameters) : SolverEvent()
    data class UpdateStatus(val id: Uuid) : SolverEvent()
    data class SetError(val error: String) : SolverEvent()
}
