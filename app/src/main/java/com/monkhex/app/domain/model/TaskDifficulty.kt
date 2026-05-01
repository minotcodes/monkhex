package com.monkhex.app.domain.model

enum class TaskDifficulty(val level: Int) {
    EASY(1),
    MODERATE(3),
    HARD(5);

    companion object {
        fun fromLevel(level: Int): TaskDifficulty = when {
            level <= 2 -> EASY
            level <= 4 -> MODERATE
            else -> HARD
        }

        fun fromString(value: String): TaskDifficulty = entries.firstOrNull {
            it.name.equals(value, ignoreCase = true)
        } ?: MODERATE
    }
}

