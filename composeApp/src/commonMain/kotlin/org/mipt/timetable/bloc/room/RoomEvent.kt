package org.mipt.timetable.bloc.room

import org.mipt.timetable.data.model.Room
import org.mipt.timetable.data.model.WeekDay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class RoomEvent {
    data class AddRoom(val roomId: Uuid, val room: Room) : RoomEvent()
    data class RemoveRoom(val roomId: Uuid) : RoomEvent()
    data class UpdateName(val roomId: Uuid, val name: String) : RoomEvent()
    data class UpdateTimeslots(
        val roomId: Uuid,
        val timeslots: Map<WeekDay, List<Boolean>>
    ) : RoomEvent()
}
