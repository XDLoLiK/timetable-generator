package org.mipt.timetable.bloc.group

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GroupViewModel : ViewModel() {
    private val _state = MutableStateFlow(GroupState())
    val state = _state.asStateFlow()

    fun onEvent(event: GroupEvent) {
        when (event) {
            is GroupEvent.AddGroup -> onAddGroup(event)
            is GroupEvent.RemoveGroup -> onRemoveGroup(event)
            is GroupEvent.UpdateName -> onUpdateName(event)
            is GroupEvent.AddClass -> onAddClass(event)
            is GroupEvent.RemoveClass -> onRemoveClass(event)
        }
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
}
