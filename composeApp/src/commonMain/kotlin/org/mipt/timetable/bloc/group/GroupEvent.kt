package org.mipt.timetable.bloc.group

import org.mipt.timetable.data.model.Group
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class GroupEvent {
    data class AddGroup(val groupId: Uuid, val group: Group) : GroupEvent()
    data class RemoveGroup(val groupId: Uuid) : GroupEvent()
    data class UpdateName(val groupId: Uuid, val name: String) : GroupEvent()
    data class AddClass(val groupId: Uuid, val classData: Pair<String, Int>) : GroupEvent()
    data class RemoveClass(val groupId: Uuid, val className: String) : GroupEvent()
    class ClearGroups() : GroupEvent()
}
