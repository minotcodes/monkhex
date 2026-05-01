package com.monkhex.app.data.remote.model

import java.time.Instant

data class UserProfileDocument(
    val userId: String = "",
    val email: String = "",
    val mode: String = "adaptive_moderate",
    val addictionType: String = "Social media addiction",
    val category: String = "digital",
    val level: Int = 18,
    val xp: Int = 2450,
    val streak: Int = 12,
    val disciplineScore: Int = 78
)

data class TaskDocument(
    val taskId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "digital",
    val difficulty: String = "MODERATE",
    val xpReward: Int = 25,
    val active: Boolean = true
)

data class UserTaskDocument(
    val userId: String = "",
    val date: String = "",
    val selectedTaskIds: List<String> = emptyList(),
    val completedTaskIds: List<String> = emptyList(),
    val source: String = "static"
)

data class CommitmentDocument(
    val userId: String = "",
    val planType: Int = 14,
    val planAmount: Int = 299,
    val startDate: String = "",
    val missedDays: Int = 0,
    val consecutiveMisses: Int = 0,
    val penalty: Int = 0,
    val refundAmount: Int = 299,
    val status: String = "ACTIVE"
)

data class AiSuggestionDocument(
    val userId: String = "",
    val date: String = "",
    val promptHash: String = "",
    val tasks: List<AiSuggestionTaskDocument> = emptyList(),
    val createdAtEpochMillis: Long = Instant.now().toEpochMilli(),
    val expiresAtEpochMillis: Long = Instant.now().plusSeconds(24 * 60 * 60).toEpochMilli()
)

data class AiSuggestionTaskDocument(
    val title: String = "",
    val description: String = "",
    val difficulty: String = "MODERATE",
    val xpReward: Int = 25
)

