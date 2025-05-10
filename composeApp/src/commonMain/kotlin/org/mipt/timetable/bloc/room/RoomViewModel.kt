package org.mipt.timetable.bloc.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mipt.timetable.Database
import org.mipt.timetable.bloc.settings.SettingsState
import org.mipt.timetable.util.*
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class RoomViewModel(private val settingsFlow: StateFlow<SettingsState>) : ViewModel() {
    private var _db: Database? = null
    private val _state = MutableStateFlow(RoomState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsFlow.collect {
                if (it is SettingsState.Saved) {
                    _db = createDatabase(it.settings.dbDir)
                    _state.update { RoomState(_db!!.getRooms()) }
                }
            }
        }
    }

    fun onEvent(event: RoomEvent) {
        when (event) {
            is RoomEvent.AddRoom -> onAddRoom(event)
            is RoomEvent.RemoveRoom -> onRemoveRoom(event)
            is RoomEvent.UpdateName -> onUpdateName(event)
            is RoomEvent.UpdateTimeslots -> onUpdateTimeslots(event)
            is RoomEvent.ClearRooms -> onClearRooms(event)
        }
        _db?.syncRooms(_state.value.rooms)
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

    private fun onClearRooms(event: RoomEvent.ClearRooms) {
        _state.update { RoomState() }
    }
}
