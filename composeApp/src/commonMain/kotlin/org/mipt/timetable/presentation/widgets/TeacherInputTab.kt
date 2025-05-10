package org.mipt.timetable.presentation.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mipt.timetable.AppLogger
import org.mipt.timetable.bloc.teacher.TeacherEvent
import org.mipt.timetable.bloc.teacher.TeacherState
import org.mipt.timetable.bloc.teacher.TeacherViewModel
import org.mipt.timetable.data.model.Teacher
import org.mipt.timetable.data.model.WeekDay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun TeacherInputTab(
    viewModel: TeacherViewModel,
) {
    val state by viewModel.state.collectAsState()

    TeacherInputTabImpl(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun TeacherInputTabImpl(
    state: TeacherState,
    onEvent: (TeacherEvent) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Teachers",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(0.8f)
            )

            IconButton(
                onClick = {
                    onEvent(
                        TeacherEvent.AddTeacher(
                            teacherId = Uuid.random(),
                            Teacher()
                        )
                    )
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add teacher"
                )
            }

            ClearButtonWithConfirmation {
                onEvent(TeacherEvent.ClearTeachers())
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(state.teachers.toList()) {
                TeacherItem(
                    id = it.first,
                    teacher = it.second,
                    onEvent = onEvent,
                )
            }
            item {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun TeacherItem(
    id: Uuid,
    teacher: Teacher,
    onEvent: (TeacherEvent) -> Unit,
) {
    var className by remember { mutableStateOf("") }
    var whitelistedGroup by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(16.dp)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = teacher.name,
                    onValueChange = {
                        onEvent(
                            TeacherEvent.UpdateName(
                                teacherId = id,
                                name = it
                            )
                        )
                    },
                    label = { Text("Teacher Name") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        onEvent(TeacherEvent.RemoveTeacher(teacherId = id))
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Teacher"
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                "Available Timeslots:",
                style = MaterialTheme.typography.h6
            )

            WeekDay.entries.forEach { day ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        day.name,
                        modifier = Modifier.width(100.dp)
                    )

                    Row {
                        repeat(7) { slot ->
                            Checkbox(
                                checked = teacher.timeslots[day]?.get(slot) ?: false,
                                modifier = Modifier.padding(horizontal = 2.dp),
                                onCheckedChange = {
                                    val timeslots = teacher.timeslots.toMutableMap().apply {
                                        val daySlots = get(day)
                                        if (daySlots != null) {
                                            set(day, daySlots.toMutableList().apply {
                                                try {
                                                    set(slot, it)
                                                } catch (e: IndexOutOfBoundsException) {
                                                    AppLogger.logger.e(
                                                        "Error updating timeslot: ${teacher.name}, $day, $slot, $it",
                                                        throwable = e
                                                    )
                                                }
                                            })
                                        }
                                    }
                                    onEvent(
                                        TeacherEvent.UpdateTimeslots(
                                            roomId = id,
                                            timeslots = timeslots
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                "Classes:",
                style = MaterialTheme.typography.h6
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = className,
                    onValueChange = { className = it },
                    label = { Text("Class Name") },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        if (className.isNotBlank()) {
                            onEvent(
                                TeacherEvent.AddClass(
                                    teacherId = id,
                                    className
                                )
                            )
                            className = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add")
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Column {
                teacher.classes.forEach { className ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(className)
                        IconButton(
                            onClick = { onEvent(TeacherEvent.RemoveClass(teacherId = id, className)) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remove Class"
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                "Group Whitelist (optional):",
                style = MaterialTheme.typography.h6
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = whitelistedGroup,
                    onValueChange = { whitelistedGroup = it },
                    label = { Text("Group Name") },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        if (whitelistedGroup.isNotBlank()) {
                            onEvent(TeacherEvent.AddGroupToWhitelist(teacherId = id, whitelistedGroup))
                            whitelistedGroup = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add")
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Column {
                teacher.groupWhitelist.forEach { groupName ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(groupName)
                        IconButton(
                            onClick = {
                                onEvent(
                                    TeacherEvent.RemoveGroupFromWhitelist(
                                        teacherId = id,
                                        groupName
                                    )
                                )
                            },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remove Group"
                            )
                        }
                    }
                }
            }
        }
    }
}
