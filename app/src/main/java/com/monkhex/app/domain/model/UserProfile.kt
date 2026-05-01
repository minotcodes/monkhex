package com.monkhex.app.domain.model

data class UserProfile(
    val userId: String,
    val email: String = "",
    val mode: String = "adaptive_moderate",
    val addictionType: String,
    val category: AddictionCategory,
    val level: Int,
    val xp: Int,
    val streak: Int,
    val disciplineScore: Int
)

