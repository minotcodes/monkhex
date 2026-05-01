package com.monkhex.app.domain.model

enum class CommitmentPlanType(val days: Int, val amountInr: Int) {
    DAYS_7(days = 7, amountInr = 199),
    DAYS_14(days = 14, amountInr = 299),
    DAYS_30(days = 30, amountInr = 499);

    companion object {
        fun fromDays(days: Int): CommitmentPlanType = entries.firstOrNull { it.days == days } ?: DAYS_14
    }
}

