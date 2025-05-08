package org.mipt.timetable.bloc.solver

import org.mipt.timetable.data.model.PackedParameters

sealed class SolverEvent {
    data class SubmitProblem(val params: PackedParameters) : SolverEvent()
}
