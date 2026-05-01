package com.monkhex.app.domain.usecase

import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.UserProfile
import com.monkhex.app.domain.repository.TaskRepository
import com.monkhex.app.domain.repository.UserRepository
import javax.inject.Inject
import kotlin.math.min

class CompleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val updateXpAndLevelUseCase: UpdateXpAndLevelUseCase
) {
    suspend operator fun invoke(userId: String, date: String, task: Task): UserProfile? {
        val before = taskRepository.getUserTaskForDate(userId, date)
        taskRepository.markTaskCompleted(userId, date, task.taskId)
        val after = taskRepository.getUserTaskForDate(userId, date)

        val profile = userRepository.getUserProfile(userId) ?: return null
        val xpResult = updateXpAndLevelUseCase(profile.xp, task.xpReward)

        val justCompletedAllTasks = before != null &&
            after != null &&
            before.completedTaskIds.size < before.selectedTaskIds.size &&
            after.completedTaskIds.size >= after.selectedTaskIds.size

        val updatedProfile = profile.copy(
            xp = xpResult.xp,
            level = xpResult.level,
            streak = if (justCompletedAllTasks) profile.streak + 1 else profile.streak,
            disciplineScore = min(100, profile.disciplineScore + (task.xpReward / 4))
        )
        userRepository.upsertUserProfile(updatedProfile)
        return updatedProfile
    }
}

