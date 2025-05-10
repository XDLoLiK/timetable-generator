package org.mipt.timetable.util

import app.cash.sqldelight.db.SqlDriver
import kotlinx.serialization.json.Json
import org.mipt.timetable.Database
import org.mipt.timetable.data.model.Group
import org.mipt.timetable.data.model.Room
import org.mipt.timetable.data.model.Teacher
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

expect fun createDriver(dbDir: String): SqlDriver

fun createDatabase(dbDir: String): Database {
    val driver = createDriver(dbDir)
    return Database(driver)
}

@OptIn(ExperimentalUuidApi::class)
fun Database.getGroups(): Map<Uuid, Group> {
    return databaseQueries.allGroups().executeAsList().map { entry ->
        Uuid.parse(entry.id) to Json.decodeFromString<Group>(entry.data_)
    }.toMap()
}

@OptIn(ExperimentalUuidApi::class)
fun Database.getRooms(): Map<Uuid, Room> {
    return databaseQueries.allRooms().executeAsList().map { entry ->
        Uuid.parse(entry.id) to Json.decodeFromString<Room>(entry.data_)
    }.toMap()
}

@OptIn(ExperimentalUuidApi::class)
fun Database.getTeachers(): Map<Uuid, Teacher> {
    return databaseQueries.allTeachers().executeAsList().map { entry ->
        Uuid.parse(entry.id) to Json.decodeFromString<Teacher>(entry.data_)
    }.toMap()
}

@OptIn(ExperimentalUuidApi::class)
fun Database.syncGroups(groups: Map<Uuid, Group>) {
    getGroups().keys.union(groups.keys).forEach { id ->
        val entry = groups[id]

        if (entry != null) {
            databaseQueries.insertGroup(id.toString(), Json.encodeToString(entry))
        } else {
            databaseQueries.deleteGroup(id.toString())
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
fun Database.syncRooms(rooms: Map<Uuid, Room>) {
    getRooms().keys.union(rooms.keys).forEach { id ->
        val entry = rooms[id]

        if (entry != null) {
            databaseQueries.insertRoom(id.toString(), Json.encodeToString(entry))
        } else {
            databaseQueries.deleteRoom(id.toString())
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
fun Database.syncTeachers(teachers: Map<Uuid, Teacher>) {
    getTeachers().keys.union(teachers.keys).forEach { id ->
        val entry = teachers[id]

        if (entry != null) {
            databaseQueries.insertTeacher(id.toString(), Json.encodeToString(entry))
        } else {
            databaseQueries.deleteTeacher(id.toString())
        }
    }
}
