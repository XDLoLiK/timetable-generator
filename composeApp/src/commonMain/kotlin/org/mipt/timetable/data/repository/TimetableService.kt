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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    suspend fun submitProblem(params: PackedParameters): HttpResponse {
        return httpClient.post("${baseUrl}/solve") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun checkStatus(id: Uuid): HttpResponse {
        return httpClient.get("${baseUrl}/${id}/status")
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getSolution(id: Uuid): HttpResponse {
        return httpClient.get("${baseUrl}/${id}")
    }
}
