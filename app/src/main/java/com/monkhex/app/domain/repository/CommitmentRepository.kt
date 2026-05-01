package com.monkhex.app.domain.repository

import com.monkhex.app.domain.model.CommitmentPlan
import com.monkhex.app.domain.model.PenaltyLedger
import kotlinx.coroutines.flow.Flow

interface CommitmentRepository {
    fun observeCommitmentPlan(userId: String): Flow<CommitmentPlan?>
    suspend fun getCommitmentPlan(userId: String): CommitmentPlan?
    suspend fun upsertCommitmentPlan(plan: CommitmentPlan)
    suspend fun computeAndApplyPenalty(userId: String, missedToday: Boolean): PenaltyLedger?
}

