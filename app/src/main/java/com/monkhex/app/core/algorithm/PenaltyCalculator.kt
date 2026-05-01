package com.monkhex.app.core.algorithm

import com.monkhex.app.domain.model.CommitmentPlan
import com.monkhex.app.domain.model.PenaltyLedger
import kotlin.math.max

object PenaltyCalculator {
    /**
     * Core penalty system:
     * - First 2 misses: INR 10 each
     * - Miss 3 onward: INR 15 each
     * - 3 consecutive misses: extra INR 20 on that day
     */
    fun applyDailyResult(plan: CommitmentPlan, missedToday: Boolean): Pair<CommitmentPlan, PenaltyLedger> {
        val (penaltyAppliedToday, updatedMissedDays, updatedConsecutiveMisses) = if (missedToday) {
            val basePenalty = if (plan.missedDays < 2) 10 else 15
            val nextConsecutiveMisses = plan.consecutiveMisses + 1
            val streakPenalty = if (nextConsecutiveMisses == 3) 20 else 0
            Triple(basePenalty + streakPenalty, plan.missedDays + 1, nextConsecutiveMisses)
        } else {
            Triple(0, plan.missedDays, 0)
        }

        val totalPenalty = plan.penalty + penaltyAppliedToday
        val refundAmount = max(0, plan.planAmount - totalPenalty)

        val updatedPlan = plan.copy(
            missedDays = updatedMissedDays,
            consecutiveMisses = updatedConsecutiveMisses,
            penalty = totalPenalty,
            refundAmount = refundAmount
        )

        return updatedPlan to PenaltyLedger(
            userId = plan.userId,
            penaltyAppliedToday = penaltyAppliedToday,
            totalPenalty = totalPenalty,
            refundAmount = refundAmount,
            missedDays = updatedMissedDays,
            consecutiveMisses = updatedConsecutiveMisses
        )
    }
}

