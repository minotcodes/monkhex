package com.monkhex.app.domain.model

enum class TaskSource(val firestoreValue: String) {
    STATIC("static"),
    AI("ai");

    companion object {
        fun fromFirestoreValue(value: String): TaskSource = entries.firstOrNull {
            it.firestoreValue.equals(value, ignoreCase = true)
        } ?: STATIC
    }
}

