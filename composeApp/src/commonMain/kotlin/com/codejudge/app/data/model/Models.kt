package com.codejudge.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Problem(
    val id: Long,
    @SerialName("timeLimit") val timeLimitMs: Int,
    @SerialName("memoryLimit") val memoryLimitKb: Int,
    val title: String,
    val description: String,
    val difficulty: String,
    val inputFormat: String? = "",
    val outputFormat: String? = "",
    val sampleTestCases: List<TestCase> = emptyList()
)

@Serializable
data class TestCase(
    val input: String,
    val expectedOutput: String,
    val isSample: Boolean = true
)

@Serializable
data class SubmissionRequest(
    val problemId: Long,
    val sourceCode: String,
    val languageId: Int
)

@Serializable
data class SubmissionResponse(
    val id: Long,
    val verdict: String,
    @SerialName("executionTime") val executionTimeMs: Double? = null,
    @SerialName("memoryUsed") val memoryUsedKb: Int? = null,
    val compileOutput: String? = null,
    val errorMessage: String? = null,
    val testCaseResults: List<TestCaseResult> = emptyList()
)

@Serializable
data class TestCaseResult(
    val testCaseNumber: Int,
    val passed: Boolean,
    @SerialName("executionTime") val executionTimeMs: Double? = null,
    @SerialName("memoryUsed") val memoryUsedKb: Int? = null,
    val actualOutput: String? = null,
    val expectedOutput: String? = null,
    val hidden: Boolean = false
)

enum class Language(val id: Int, val displayName: String) {
    PYTHON(71, "Python 3"),
    JAVA(62, "Java"),
    CPP(54, "C++"),
    JAVASCRIPT(63, "JavaScript"),
    C(50, "C")
}
