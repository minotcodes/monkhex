package com.monkhex.app.domain.model

enum class AddictionCategory(val firestoreValue: String) {
    DIGITAL("digital"),
    MENTAL("mental"),
    PHYSICAL("physical");

    companion object {
        fun fromFirestoreValue(value: String): AddictionCategory = entries.firstOrNull {
            it.firestoreValue.equals(value, ignoreCase = true)
        } ?: DIGITAL
    }
}

