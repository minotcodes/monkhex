package com.monkhex.app.domain.usecase

import javax.inject.Inject
import kotlin.math.max

data class XpLevelResult(
    val xp: Int,
    val level: Int,
    val nextLevelXp: Int
)

class UpdateXpAndLevelUseCase @Inject constructor() {
    operator fun invoke(currentXp: Int, deltaXp: Int): XpLevelResult {
        val updatedXp = max(0, currentXp + deltaXp)
        val level = resolveLevel(updatedXp)
        val nextLevelXp = xpThreshold(level + 1)
        return XpLevelResult(
            xp = updatedXp,
            level = level,
            nextLevelXp = nextLevelXp
        )
    }

    fun resolveLevel(xp: Int): Int {
        var level = 1
        while (xp >= xpThreshold(level + 1) && level < 100) {
            level++
        }
        return level
    }

    /**
     * Non-linear progression curve tuned for discipline apps:
     * early levels move faster, late levels demand consistency.
     */
    fun xpThreshold(level: Int): Int {
        val safeLevel = max(1, level)
        return (safeLevel * safeLevel * 6) + (safeLevel * 20)
    }
}

