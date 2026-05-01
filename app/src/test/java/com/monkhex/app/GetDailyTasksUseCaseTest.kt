package com.monkhex.app

import com.google.common.truth.Truth.assertThat
import com.monkhex.app.core.algorithm.TaskSelectionEngine
import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.AiSuggestionCache
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.TaskDifficulty
import com.monkhex.app.domain.model.UserProfile
import com.monkhex.app.domain.model.UserTask
import com.monkhex.app.domain.repository.SuggestionRepository
import com.monkhex.app.domain.repository.TaskRepository
import com.monkhex.app.domain.repository.UserRepository
import com.monkhex.app.domain.usecase.GetDailyTasksUseCase
import com.monkhex.app.domain.usecase.GetOrGenerateAiSuggestionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDate

class GetDailyTasksUseCaseTest {

    @Test
    fun `daily generation is idempotent for same user and date`() = runBlocking {
        val userRepo = FakeUserRepository()
        val taskRepo = FakeTaskRepository()
        val suggestionRepo = FakeSuggestionRepository()
        val useCase = GetDailyTasksUseCase(
            userRepository = userRepo,
            taskRepository = taskRepo,
            getOrGenerateAiSuggestionsUseCase = GetOrGenerateAiSuggestionsUseCase(suggestionRepo),
            taskSelectionEngine = TaskSelectionEngine()
        )

        val date = LocalDate.of(2026, 4, 17)
        val first = useCase("u1", date).map { it.taskId }
        val second = useCase("u1", date).map { it.taskId }

        assertThat(first).isEqualTo(second)
        assertThat(first).hasSize(6)
    }
}

private class FakeUserRepository : UserRepository {
    private val profile = UserProfile(
        userId = "u1",
        addictionType = "Social media addiction",
        category = AddictionCategory.DIGITAL,
        level = 18,
        xp = 2450,
        streak = 12,
        disciplineScore = 78
    )

    override fun observeUserProfile(userId: String): Flow<UserProfile?> = flowOf(profile)

    override suspend fun getUserProfile(userId: String): UserProfile? = profile

    override suspend fun upsertUserProfile(profile: UserProfile) = Unit
}

private class FakeTaskRepository : TaskRepository {
    private val tasks = (1..10).map { index ->
        Task(
            taskId = "digital_$index",
            title = "Task $index",
            description = "Description $index",
            category = AddictionCategory.DIGITAL,
            difficulty = if (index % 2 == 0) TaskDifficulty.MODERATE else TaskDifficulty.EASY,
            xpReward = 20 + index
        )
    }
    private val daily = mutableMapOf<String, UserTask>()

    override suspend fun getTasksByCategory(category: AddictionCategory): List<Task> = tasks

    override suspend fun getTasksByIds(taskIds: List<String>): List<Task> = tasks.filter { it.taskId in taskIds }

    override suspend fun getUserTaskHistory(userId: String, lookbackDays: Int): List<UserTask> = emptyList()

    override suspend fun getUserTaskForDate(userId: String, date: String): UserTask? = daily["${userId}_$date"]

    override suspend fun saveUserTask(userTask: UserTask) {
        daily["${userTask.userId}_${userTask.date}"] = userTask
    }

    override suspend fun markTaskCompleted(userId: String, date: String, taskId: String) = Unit
}

private class FakeSuggestionRepository : SuggestionRepository {
    override suspend fun getCachedSuggestion(userId: String, date: String): AiSuggestionCache? = null
    override suspend fun generateSuggestion(userProfile: UserProfile, date: String): AiSuggestionCache? = null
    override suspend fun cacheSuggestion(cache: AiSuggestionCache) = Unit
}

