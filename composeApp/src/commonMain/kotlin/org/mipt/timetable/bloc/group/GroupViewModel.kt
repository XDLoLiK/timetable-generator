package org.mipt.timetable.bloc.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mipt.timetable.Database
import org.mipt.timetable.bloc.settings.SettingsState
import org.mipt.timetable.util.createDatabase
import org.mipt.timetable.util.getGroups
import org.mipt.timetable.util.syncGroups
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GroupViewModel(private val settingsFlow: StateFlow<SettingsState>) : ViewModel() {
    private var _db: Database? = null
    private val _state = MutableStateFlow(GroupState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsFlow.collect {
                if (it is SettingsState.Saved) {
                    _db = createDatabase(it.settings.dbDir)
                    _state.update { GroupState(_db!!.getGroups()) }
                }
            }
        }
    }

    fun onEvent(event: GroupEvent) {
        when (event) {
            is GroupEvent.AddGroup -> onAddGroup(event)
            is GroupEvent.RemoveGroup -> onRemoveGroup(event)
            is GroupEvent.UpdateName -> onUpdateName(event)
            is GroupEvent.AddClass -> onAddClass(event)
            is GroupEvent.RemoveClass -> onRemoveClass(event)
            is GroupEvent.ClearGroups -> onClearGroups(event)
        }
        _db?.syncGroups(_state.value.groups)
    }

    private fun onAddGroup(event: GroupEvent.AddGroup) {
        _state.update {
            it.copy(
                groups = it.groups.toMutableMap().apply {
                    put(event.groupId, event.group)
                }
            )
        }
    }

    private fun onRemoveGroup(event: GroupEvent.RemoveGroup) {
        _state.update {
            it.copy(
                groups = it.groups.toMutableMap().apply {
                    remove(event.groupId)
                }
            )
        }
    }

    private fun onUpdateName(event: GroupEvent.UpdateName) {
        _state.update {
            it.copy(
                groups = it.groups.toMutableMap().apply {
                    val group = get(event.groupId)
                    if (group != null) {
                        put(event.groupId, group.copy(name = event.name))
                    }
                }
            )
        }
    }

    private fun onAddClass(event: GroupEvent.AddClass) {
        _state.update {
            it.copy(
                groups = it.groups.toMutableMap().apply {
                    val group = get(event.groupId)
                    if (group != null) {
                        put(event.groupId, group.copy(classHours = group.classHours.toMutableMap().apply {
                            put(event.classData.first, event.classData.second)
                        }))
                    }
                }
            )
        }
    }

    private fun onRemoveClass(event: GroupEvent.RemoveClass) {
        _state.update {
            it.copy(
                groups = it.groups.toMutableMap().apply {
                    val group = get(event.groupId)
                    if (group != null) {
                        put(event.groupId, group.copy(classHours = group.classHours.toMutableMap().apply {
                            remove(event.className)
                        }))
                    }
                }
            )
        }
    }

    private fun onClearGroups(event: GroupEvent.ClearGroups) {
        _state.update { GroupState() }
    }
}
