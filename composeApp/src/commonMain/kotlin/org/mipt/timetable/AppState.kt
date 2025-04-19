package org.mipt.timetable

import org.mipt.timetable.data.GroupData
import org.mipt.timetable.data.RoomData
import org.mipt.timetable.data.TeacherData
import org.mipt.timetable.data.TimetableData

data class AppState(
    var teachers: MutableList<TeacherData> = mutableListOf(),
    var groups: MutableList<GroupData> = mutableListOf(),
    var rooms: MutableList<RoomData> = mutableListOf(),
    var timetable: TimetableData = TimetableData(),
)
