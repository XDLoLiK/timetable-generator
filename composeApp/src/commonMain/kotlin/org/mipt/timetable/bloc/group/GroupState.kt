package org.mipt.timetable.bloc.group

import org.mipt.timetable.data.model.Group
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class GroupState(
    val groups: Map<Uuid, Group> = mapOf()
)
