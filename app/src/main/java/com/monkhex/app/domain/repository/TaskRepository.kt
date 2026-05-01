package com.monkhex.app.domain.repository

import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.UserTask

interface TaskRepository {
    suspend fun getTasksByCategory(category: AddictionCategory): List<Task>
    suspend fun getTasksByIds(taskIds: List<String>): List<Task>
    suspend fun getUserTaskHistory(userId: String, lookbackDays: Int): List<UserTask>
    suspend fun getUserTaskForDate(userId: String, date: String): UserTask?
    suspend fun saveUserTask(userTask: UserTask)
    suspend fun markTaskCompleted(userId: String, date: String, taskId: String)
}

