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
import org.mipt.timetable.bloc.group.GroupEvent
import org.mipt.timetable.bloc.group.GroupState
import org.mipt.timetable.bloc.group.GroupViewModel
import org.mipt.timetable.data.model.Group
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun GroupInputTab(
    viewModel: GroupViewModel,
) {
    val state by viewModel.state.collectAsState()

    GroupInputTabImpl(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun GroupInputTabImpl(
    state: GroupState,
    onEvent: (GroupEvent) -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Groups",
                style = MaterialTheme.typography.h6
            )
            IconButton(
                onClick = {
                    onEvent(GroupEvent.AddGroup(groupId = Uuid.random(), Group()))
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add group"
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
            items(state.groups.toList()) {
                GroupItem(
                    id = it.first,
                    group = it.second,
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
private fun GroupItem(
    id: Uuid,
    group: Group,
    onEvent: (GroupEvent) -> Unit,
) {
    var className by remember { mutableStateOf("") }
    var classHours by remember { mutableStateOf("") }

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
                    value = group.name,
                    onValueChange = { onEvent(GroupEvent.UpdateName(groupId = id, name = it)) },
                    label = { Text("Group Name") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        onEvent(GroupEvent.RemoveGroup(groupId = id))
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Group"
                    )
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

                OutlinedTextField(
                    value = classHours,
                    onValueChange = { classHours = it },
                    label = { Text("Hours") },
                    modifier = Modifier.width(100.dp)
                )

                Button(
                    onClick = {
                        if (className.isNotBlank() && classHours.toIntOrNull() != null) {
                            onEvent(GroupEvent.AddClass(groupId = id, className to classHours.toInt()))
                            className = ""
                            classHours = ""
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
                group.classHours.forEach { (className, hours) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(className)
                        Text("$hours hours")
                        IconButton(
                            onClick = { onEvent(GroupEvent.RemoveClass(groupId = id, className)) },
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
        }
    }
}
