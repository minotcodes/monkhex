package com.monkhex.app.core.algorithm

import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.UserTask
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class TaskSelectionEngine {

    fun selectTasks(
        taskPool: List<Task>,
        userLevel: Int,
        history: List<UserTask>,
        requestedCount: Int = 6,
        randomSeed: String
    ): List<Task> {
        if (taskPool.isEmpty()) return emptyList()
        if (taskPool.size <= requestedCount) return taskPool

        val targetDifficulty = adaptiveDifficulty(userLevel)
        val frequencyMap = history
            .flatMap { it.selectedTaskIds }
            .groupingBy { it }
            .eachCount()

        val recentlyUsedTaskIds = history
            .take(3)
            .flatMap { it.selectedTaskIds }
            .toSet()

        val random = Random(randomSeed.hashCode())
        val scored = taskPool.map { task ->
            val difficultyFit = 1f - (abs(task.difficulty.level - targetDifficulty).toFloat() / 4f)
            val frequency = frequencyMap[task.taskId] ?: 0
            val noveltyFit = max(0f, 1f - (frequency * 0.15f))
            val recentPenalty = if (recentlyUsedTaskIds.contains(task.taskId)) 0.35f else 0f
            val randomBoost = random.nextFloat() * 0.25f

            val finalScore = max(
                0.05f,
                (difficultyFit * 0.55f) + (noveltyFit * 0.30f) + (randomBoost * 0.15f) - recentPenalty
            )
            WeightedTask(task = task, weight = finalScore)
        }

        return weightedSampleWithoutReplacement(scored, requestedCount, random)
    }

    private fun adaptiveDifficulty(level: Int): Int = min(5, max(1, 2 + (level / 8)))

    private fun weightedSampleWithoutReplacement(
        weightedTasks: List<WeightedTask>,
        targetCount: Int,
        random: Random
    ): List<Task> {
        val mutable = weightedTasks.toMutableList()
        val selected = mutableListOf<Task>()

        while (selected.size < targetCount && mutable.isNotEmpty()) {
            val totalWeight = mutable.sumOf { it.weight.toDouble() }.toFloat().coerceAtLeast(0.01f)
            var pick = random.nextFloat() * totalWeight
            var index = 0

            while (index < mutable.size && pick > mutable[index].weight) {
                pick -= mutable[index].weight
                index++
            }

            val safeIndex = index.coerceAtMost(mutable.lastIndex)
            selected += mutable[safeIndex].task
            mutable.removeAt(safeIndex)
        }
        return selected
    }

    private data class WeightedTask(
        val task: Task,
        val weight: Float
    )
}

