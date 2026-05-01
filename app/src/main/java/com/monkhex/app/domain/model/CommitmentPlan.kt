package com.monkhex.app.domain.model

data class CommitmentPlan(
    val userId: String,
    val planType: CommitmentPlanType,
    val planAmount: Int,
    val startDate: String,
    val missedDays: Int,
    val consecutiveMisses: Int,
    val penalty: Int,
    val refundAmount: Int,
    val status: CommitmentStatus
)

