package com.monkhex.app

import com.google.common.truth.Truth.assertThat
import com.monkhex.app.core.algorithm.TaskSelectionEngine
import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.TaskDifficulty
import com.monkhex.app.domain.model.TaskSource
import com.monkhex.app.domain.model.UserTask
import org.junit.Test

class TaskSelectionEngineTest {

    private val engine = TaskSelectionEngine()

    @Test
    fun `selects 6 unique tasks using single weighted pipeline`() {
        val tasks = (1..12).map { index ->
            Task(
                taskId = "task_$index",
                title = "Task $index",
                description = "Description $index",
                category = AddictionCategory.DIGITAL,
                difficulty = when {
                    index % 3 == 0 -> TaskDifficulty.HARD
                    index % 2 == 0 -> TaskDifficulty.MODERATE
                    else -> TaskDifficulty.EASY
                },
                xpReward = 20 + index
            )
        }
        val history = listOf(
            UserTask(
                userId = "u1",
                date = "2026-04-16",
                selectedTaskIds = listOf("task_1", "task_2", "task_3"),
                completedTaskIds = listOf("task_1"),
                source = TaskSource.STATIC
            ),
            UserTask(
                userId = "u1",
                date = "2026-04-15",
                selectedTaskIds = listOf("task_4", "task_5"),
                completedTaskIds = emptyList(),
                source = TaskSource.STATIC
            )
        )

        val selected = engine.selectTasks(
            taskPool = tasks,
            userLevel = 18,
            history = history,
            requestedCount = 6,
            randomSeed = "u1-2026-04-17"
        )

        assertThat(selected).hasSize(6)
        assertThat(selected.map { it.taskId }.toSet()).hasSize(6)
    }
}

