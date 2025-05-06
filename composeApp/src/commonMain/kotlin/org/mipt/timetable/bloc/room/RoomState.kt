package org.mipt.timetable.bloc.room

import org.mipt.timetable.data.model.Room
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class RoomState(val rooms: Map<Uuid, Room> = mapOf())
