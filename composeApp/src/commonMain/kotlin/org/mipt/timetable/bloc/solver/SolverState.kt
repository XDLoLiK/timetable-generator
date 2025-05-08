package org.mipt.timetable.bloc.solver

import io.ktor.http.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class SolverState() {
    class Idle() : SolverState()
    data class Solving(val uuid: Uuid) : SolverState()
    data class Error(val response: HttpStatusCode) : SolverState()
}
