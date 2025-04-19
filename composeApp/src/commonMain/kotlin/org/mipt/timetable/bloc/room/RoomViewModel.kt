package org.mipt.timetable.bloc.room

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class RoomViewModel : ViewModel() {
    private val _state = MutableStateFlow(RoomState())
    val state = _state.asStateFlow()

    fun onEvent(event: RoomEvent) {
        when (event) {
            is RoomEvent.AddRoom -> onAddRoom(event)
            is RoomEvent.RemoveRoom -> onRemoveRoom(event)
            is RoomEvent.UpdateName -> onUpdateName(event)
            is RoomEvent.UpdateTimeslots -> onUpdateTimeslots(event)
        }
    }

    private fun onAddRoom(event: RoomEvent.AddRoom) {
        _state.update {
            it.copy(
                rooms = it.rooms.toMutableMap().apply {
                    put(event.roomId, event.room)
                }
            )
        }
    }

    private fun onRemoveRoom(event: RoomEvent.RemoveRoom) {
        _state.update {
            it.copy(
                rooms = it.rooms.toMutableMap().apply {
                    remove(event.roomId)
                }
            )
        }
    }

    private fun onUpdateName(event: RoomEvent.UpdateName) {
        _state.update {
            it.copy(
                rooms = it.rooms.toMutableMap().apply {
                    val room = get(event.roomId)
                    if (room != null) {
                        put(event.roomId, room.copy(name = event.name))
                    }
                }
            )
        }
    }

    private fun onUpdateTimeslots(event: RoomEvent.UpdateTimeslots) {
        _state.update {
            it.copy(
                rooms = it.rooms.toMutableMap().apply {
                    val room = get(event.roomId)
                    if (room != null) {
                        put(event.roomId, room.copy(timeslots = event.timeslots))
                    }
                }
            )
        }
    }
}
