package com.monkhex.app

import com.google.common.truth.Truth.assertThat
import com.monkhex.app.data.mapper.toDocument
import com.monkhex.app.data.mapper.toDomain
import com.monkhex.app.data.remote.model.TaskDocument
import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.TaskDifficulty
import org.junit.Test

class FirestoreMapperContractTest {

    @Test
    fun `task mapper remains stable across round trip`() {
        val domain = Task(
            taskId = "digital_01",
            title = "Stay Off Social Media",
            description = "No social apps for 3 hours",
            category = AddictionCategory.DIGITAL,
            difficulty = TaskDifficulty.MODERATE,
            xpReward = 40
        )

        val document = domain.toDocument()
        val roundTrip = TaskDocument(
            taskId = document.taskId,
            title = document.title,
            description = document.description,
            category = document.category,
            difficulty = document.difficulty,
            xpReward = document.xpReward,
            active = document.active
        ).toDomain()

        assertThat(roundTrip.taskId).isEqualTo(domain.taskId)
        assertThat(roundTrip.category).isEqualTo(domain.category)
        assertThat(roundTrip.difficulty).isEqualTo(domain.difficulty)
    }
}

