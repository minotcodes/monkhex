package com.monkhex.app.domain.model

data class PenaltyLedger(
    val userId: String,
    val penaltyAppliedToday: Int,
    val totalPenalty: Int,
    val refundAmount: Int,
    val missedDays: Int,
    val consecutiveMisses: Int
)

