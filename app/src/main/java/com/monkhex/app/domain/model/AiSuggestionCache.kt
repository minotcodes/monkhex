package com.monkhex.app.domain.model

import java.time.Instant

data class AiSuggestionTask(
    val title: String,
    val description: String,
    val difficulty: TaskDifficulty,
    val xpReward: Int
)

data class AiSuggestionCache(
    val userId: String,
    val date: String,
    val promptHash: String,
    val tasks: List<AiSuggestionTask>,
    val createdAt: Instant,
    val expiresAt: Instant
)

