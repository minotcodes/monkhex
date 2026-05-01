package com.monkhex.app

import com.google.common.truth.Truth.assertThat
import com.monkhex.app.core.algorithm.PenaltyCalculator
import com.monkhex.app.domain.model.CommitmentPlan
import com.monkhex.app.domain.model.CommitmentPlanType
import com.monkhex.app.domain.model.CommitmentStatus
import org.junit.Test

class PenaltyCalculatorTest {

    @Test
    fun `applies base and consecutive penalties with refund floor`() {
        val basePlan = CommitmentPlan(
            userId = "u1",
            planType = CommitmentPlanType.DAYS_14,
            planAmount = 299,
            startDate = "2026-04-01",
            missedDays = 0,
            consecutiveMisses = 0,
            penalty = 0,
            refundAmount = 299,
            status = CommitmentStatus.ACTIVE
        )

        val (afterFirst, firstLedger) = PenaltyCalculator.applyDailyResult(basePlan, missedToday = true)
        assertThat(firstLedger.penaltyAppliedToday).isEqualTo(10)
        assertThat(afterFirst.penalty).isEqualTo(10)

        val (afterSecond, secondLedger) = PenaltyCalculator.applyDailyResult(afterFirst, missedToday = true)
        assertThat(secondLedger.penaltyAppliedToday).isEqualTo(10)
        assertThat(afterSecond.penalty).isEqualTo(20)

        val (afterThird, thirdLedger) = PenaltyCalculator.applyDailyResult(afterSecond, missedToday = true)
        assertThat(thirdLedger.penaltyAppliedToday).isEqualTo(35) // 15 + 20 consecutive bonus
        assertThat(afterThird.penalty).isEqualTo(55)
        assertThat(afterThird.refundAmount).isEqualTo(244)
    }
}

