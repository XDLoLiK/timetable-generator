package org.mipt.timetable.bloc.solver

import io.ktor.http.*
import org.mipt.timetable.data.model.ArrangedClass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class SolverState() {
    class Idle() : SolverState()
    data class Solving(val uuid: Uuid) : SolverState()
    data class Solved(val result: List<ArrangedClass>) : SolverState()
    data class Error(val message: String) : SolverState()
}
