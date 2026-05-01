package com.monkhex.app.domain.usecase

import com.monkhex.app.BuildConfig
import com.monkhex.app.core.algorithm.AddictionCategoryMapper
import com.monkhex.app.core.algorithm.TaskSelectionEngine
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.TaskSource
import com.monkhex.app.domain.model.UserTask
import com.monkhex.app.domain.repository.TaskRepository
import com.monkhex.app.domain.repository.UserRepository
import java.time.LocalDate
import javax.inject.Inject

class GetDailyTasksUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
    private val getOrGenerateAiSuggestionsUseCase: GetOrGenerateAiSuggestionsUseCase,
    private val taskSelectionEngine: TaskSelectionEngine
) {
    suspend operator fun invoke(
        userId: String,
        date: LocalDate = LocalDate.now()
    ): List<Task> {
        val dateIso = date.toString()
        val userProfile = userRepository.getUserProfile(userId) ?: return emptyList()
        val mappedCategory = AddictionCategoryMapper.map(userProfile.addictionType)
        if (mappedCategory != userProfile.category) {
            userRepository.upsertUserProfile(userProfile.copy(category = mappedCategory))
        }

        val staticTasks = taskRepository.getTasksByCategory(mappedCategory)
        val aiTasks = getOrGenerateAiSuggestionsUseCase(
            userProfile = userProfile.copy(category = mappedCategory),
            date = dateIso,
            aiEnabled = BuildConfig.AI_ENABLED
        )
        val mergedPool = (staticTasks + aiTasks).distinctBy { it.title.lowercase() }

        val existing = taskRepository.getUserTaskForDate(userId, dateIso)
        if (existing != null) {
            val byId = mergedPool.associateBy { it.taskId }
            val restored = existing.selectedTaskIds.mapNotNull { byId[it] }
            if (restored.isNotEmpty()) return restored
        }

        val history = taskRepository.getUserTaskHistory(userId, lookbackDays = 14)
        val selected = taskSelectionEngine.selectTasks(
            taskPool = mergedPool,
            userLevel = userProfile.level,
            history = history,
            requestedCount = 6,
            randomSeed = "$userId-$dateIso"
        )

        taskRepository.saveUserTask(
            UserTask(
                userId = userId,
                date = dateIso,
                selectedTaskIds = selected.map { it.taskId },
                completedTaskIds = existing?.completedTaskIds.orEmpty(),
                source = if (selected.any { it.generatedByAi }) TaskSource.AI else TaskSource.STATIC
            )
        )
        return selected
    }
}

