package org.mipt.timetable.presentation.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mipt.timetable.AppLogger
import org.mipt.timetable.bloc.room.RoomEvent
import org.mipt.timetable.bloc.room.RoomState
import org.mipt.timetable.bloc.room.RoomViewModel
import org.mipt.timetable.data.model.Room
import org.mipt.timetable.data.model.WeekDay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun RoomInputTab(
    viewModel: RoomViewModel = RoomViewModel(),
) {
    val state by viewModel.state.collectAsState()

    RoomInputTabImpl(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun RoomInputTabImpl(
    state: RoomState,
    onEvent: (RoomEvent) -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Rooms",
                style = MaterialTheme.typography.h6
            )
            IconButton(
                onClick = {
                    onEvent(RoomEvent.AddRoom(roomId = Uuid.random(), Room()))
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add room"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(state.rooms.toList()) {
                RoomItem(
                    id = it.first,
                    room = it.second,
                    onEvent
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
private fun RoomItem(
    id: Uuid,
    room: Room,
    onEvent: (RoomEvent) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(16.dp)),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = room.name,
                    onValueChange = {
                        onEvent(
                            RoomEvent.UpdateName(
                                roomId = id,
                                name = it
                            )
                        )
                    },
                    label = { Text("Room Name") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        onEvent(RoomEvent.RemoveRoom(roomId = id))
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Room"
                    )
                }
            }

            Text(
                "Available Timeslots:",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            WeekDay.entries.forEach { day ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        day.name,
                        modifier = Modifier.width(100.dp)
                    )
                    Row {
                        repeat(7) { slot ->
                            Checkbox(
                                modifier = Modifier.padding(horizontal = 2.dp),
                                checked = room.timeslots[day]?.get(slot) ?: false,
                                onCheckedChange = {
                                    val timeslots = room.timeslots.toMutableMap().apply {
                                        val daySlots = get(day)
                                        if (daySlots != null) {
                                            set(day, daySlots.toMutableList().apply {
                                                try {
                                                    set(slot, it)
                                                } catch (e: IndexOutOfBoundsException) {
                                                    AppLogger.logger.e(
                                                        "Error updating timeslot: ${room.name}, $day, $slot, $it",
                                                        throwable = e
                                                    )
                                                }
                                            })
                                        }
                                    }
                                    onEvent(
                                        RoomEvent.UpdateTimeslots(
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
        }
    }
}
