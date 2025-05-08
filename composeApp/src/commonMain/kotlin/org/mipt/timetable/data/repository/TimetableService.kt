package org.mipt.timetable.data.repository

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.mipt.timetable.AppLogger
import org.mipt.timetable.data.model.ArrangedClass
import org.mipt.timetable.data.model.PackedParameters

sealed class ClassRequestStatus {
    data class Ready(val classData: ArrangedClass) : ClassRequestStatus()
    data class Error(val message: String) : ClassRequestStatus()
    class InProgress : ClassRequestStatus()
    class ReachedEnd : ClassRequestStatus()
}

class TimetableService(
    val baseUrl: String = ""
) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun sendJson(params: PackedParameters): HttpResponse {
        return httpClient.post("${baseUrl}/solve") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
    }

    suspend fun requestNextClass(): ClassRequestStatus = coroutineScope {
        val response = async {
            httpClient.get(baseUrl) {
                header("Get-Next-Class", "true")
            }
        }.await()

        if (response.status != HttpStatusCode.OK) {
            return@coroutineScope ClassRequestStatus.Error(
                "Server returned ${response.status} status code"
            )
        }

        if (response.headers["Reached-End"] == "true") {
            return@coroutineScope ClassRequestStatus.ReachedEnd()
        }

        if (response.headers["Next-Class-Ready"] == "false") {
            return@coroutineScope ClassRequestStatus.InProgress()
        }

        val body = async {
            response.bodyAsText()
        }.await()
        try {
            val classData = Json.decodeFromString<ArrangedClass>(body)
            ClassRequestStatus.Ready(classData)
        } catch (e: IllegalArgumentException) {
            AppLogger.logger.w(
                "Failed to parse json: $body",
                throwable = e
            )
            ClassRequestStatus.Error("Invalid server response format")
        }
    }
}
