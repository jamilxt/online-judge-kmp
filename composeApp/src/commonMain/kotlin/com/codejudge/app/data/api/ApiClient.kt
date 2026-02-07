package com.codejudge.app.data.api

import com.codejudge.app.data.model.Problem
import com.codejudge.app.data.model.SubmissionRequest
import com.codejudge.app.data.model.SubmissionResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient(
    private val baseUrl: String = "http://10.0.2.2:8081" // Android emulator localhost
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getProblems(): List<Problem> {
        return client.get("$baseUrl/api/problems").body()
    }

    suspend fun getProblem(id: Long): Problem {
        return client.get("$baseUrl/api/problems/$id").body()
    }

    suspend fun submitCode(request: SubmissionRequest): SubmissionResponse {
        return client.post("$baseUrl/api/submissions") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getSubmission(id: Long): SubmissionResponse {
        return client.get("$baseUrl/api/submissions/$id").body()
    }
}

// Platform-specific base URL provider
expect fun getBaseUrl(): String
