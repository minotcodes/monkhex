package com.monkhex.app

import com.google.common.truth.Truth.assertThat
import com.monkhex.app.domain.usecase.UpdateXpAndLevelUseCase
import org.junit.Test

class UpdateXpAndLevelUseCaseTest {

    private val useCase = UpdateXpAndLevelUseCase()

    @Test
    fun `level progression is monotonic and deterministic`() {
        val a = useCase(currentXp = 100, deltaXp = 50)
        val b = useCase(currentXp = 150, deltaXp = 50)
        val c = useCase(currentXp = 200, deltaXp = 100)

        assertThat(a.xp).isEqualTo(150)
        assertThat(b.xp).isEqualTo(200)
        assertThat(c.xp).isEqualTo(300)

        assertThat(a.level).isAtMost(b.level)
        assertThat(b.level).isAtMost(c.level)

        val replay = useCase(currentXp = 200, deltaXp = 100)
        assertThat(replay.level).isEqualTo(c.level)
        assertThat(replay.nextLevelXp).isEqualTo(c.nextLevelXp)
    }
}

