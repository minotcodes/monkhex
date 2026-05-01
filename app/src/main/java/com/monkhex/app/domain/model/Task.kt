package com.monkhex.app.domain.model

data class Task(
    val taskId: String,
    val title: String,
    val description: String,
    val category: AddictionCategory,
    val difficulty: TaskDifficulty,
    val xpReward: Int,
    val active: Boolean = true,
    val generatedByAi: Boolean = false
)

