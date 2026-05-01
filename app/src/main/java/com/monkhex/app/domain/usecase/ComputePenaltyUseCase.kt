package com.monkhex.app.domain.usecase

import com.monkhex.app.core.algorithm.PenaltyCalculator
import com.monkhex.app.domain.model.CommitmentPlan
import com.monkhex.app.domain.model.PenaltyLedger
import javax.inject.Inject

class ComputePenaltyUseCase @Inject constructor() {
    operator fun invoke(plan: CommitmentPlan, missedToday: Boolean): Pair<CommitmentPlan, PenaltyLedger> {
        return PenaltyCalculator.applyDailyResult(plan, missedToday)
    }
}

