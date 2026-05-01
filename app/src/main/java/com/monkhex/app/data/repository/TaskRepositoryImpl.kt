package com.monkhex.app.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.monkhex.app.data.mapper.toDocument
import com.monkhex.app.data.mapper.toDomain
import com.monkhex.app.data.remote.model.TaskDocument
import com.monkhex.app.data.remote.model.UserTaskDocument
import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.TaskDifficulty
import com.monkhex.app.domain.model.UserTask
import com.monkhex.app.domain.repository.TaskRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {

    private val tasksCollection = firestore.collection("tasks")
    private val userTasksCollection = firestore.collection("user_tasks")

    override suspend fun getTasksByCategory(category: AddictionCategory): List<Task> {
        val remote = tasksCollection
            .whereEqualTo("category", category.firestoreValue)
            .whereEqualTo("active", true)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(TaskDocument::class.java)?.copy(taskId = it.id)?.toDomain() }

        return if (remote.isNotEmpty()) remote else fallbackTaskCatalog(category)
    }

    override suspend fun getTasksByIds(taskIds: List<String>): List<Task> {
        if (taskIds.isEmpty()) return emptyList()
        val remote = mutableListOf<Task>()
        taskIds.chunked(10).forEach { chunk ->
            val docs = tasksCollection.whereIn(FieldPath.documentId(), chunk).get().await()
            remote += docs.documents.mapNotNull {
                it.toObject(TaskDocument::class.java)?.copy(taskId = it.id)?.toDomain()
            }
        }
        val fallback = fallbackTaskCatalog(AddictionCategory.DIGITAL) +
            fallbackTaskCatalog(AddictionCategory.MENTAL) +
            fallbackTaskCatalog(AddictionCategory.PHYSICAL)
        val all = (remote + fallback.filter { task -> taskIds.contains(task.taskId) })
            .distinctBy { it.taskId }
        return taskIds.mapNotNull { id -> all.firstOrNull { it.taskId == id } }
    }

    override suspend fun getUserTaskHistory(userId: String, lookbackDays: Int): List<UserTask> {
        return userTasksCollection
            .whereEqualTo("userId", userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(lookbackDays.toLong())
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(UserTaskDocument::class.java)?.toDomain() }
    }

    override suspend fun getUserTaskForDate(userId: String, date: String): UserTask? {
        val id = "${userId}_$date"
        return userTasksCollection.document(id).get().await()
            .toObject(UserTaskDocument::class.java)
            ?.toDomain()
    }

    override suspend fun saveUserTask(userTask: UserTask) {
        val id = "${userTask.userId}_${userTask.date}"
        userTasksCollection.document(id).set(userTask.toDocument(), SetOptions.merge()).await()
    }

    override suspend fun markTaskCompleted(userId: String, date: String, taskId: String) {
        val id = "${userId}_$date"
        val ref = userTasksCollection.document(id)
        runCatching {
            ref.update("completedTaskIds", FieldValue.arrayUnion(taskId)).await()
        }.onFailure {
            val fallback = UserTaskDocument(
                userId = userId,
                date = date,
                selectedTaskIds = emptyList(),
                completedTaskIds = listOf(taskId),
                source = "static"
            )
            ref.set(fallback, SetOptions.merge()).await()
        }
    }

    private fun fallbackTaskCatalog(category: AddictionCategory): List<Task> {
        return when (category) {
            AddictionCategory.DIGITAL -> listOf(
                Task("digital_01", "Stay Off Social Media", "No social apps for next 3 hours", category, TaskDifficulty.MODERATE, 40),
                Task("digital_02", "Inbox Silence", "Disable non-essential notifications", category, TaskDifficulty.EASY, 25),
                Task("digital_03", "Single App Focus", "Use only one app at a time", category, TaskDifficulty.MODERATE, 32),
                Task("digital_04", "No Shorts Window", "Avoid shorts/reels till evening", category, TaskDifficulty.HARD, 45),
                Task("digital_05", "Device-Free Meal", "Eat without phone for one meal", category, TaskDifficulty.EASY, 20),
                Task("digital_06", "Deep Work Block", "45-minute distraction-free block", category, TaskDifficulty.MODERATE, 38),
                Task("digital_07", "Uninstall Trigger App", "Remove one trigger app today", category, TaskDifficulty.HARD, 55)
            )

            AddictionCategory.MENTAL -> listOf(
                Task("mental_01", "Breathing Reset", "10-minute mindful breathing", category, TaskDifficulty.EASY, 22),
                Task("mental_02", "Thought Journal", "Write top 3 thought loops", category, TaskDifficulty.MODERATE, 30),
                Task("mental_03", "No Fantasy Sprint", "Redirect urges for 20 minutes", category, TaskDifficulty.MODERATE, 34),
                Task("mental_04", "Reality Anchor", "Grounding practice 5-4-3-2-1", category, TaskDifficulty.EASY, 24),
                Task("mental_05", "Single Priority Focus", "Complete one avoided task", category, TaskDifficulty.HARD, 48),
                Task("mental_06", "Urge Delay", "Delay gratification for 30 minutes", category, TaskDifficulty.MODERATE, 36)
            )

            AddictionCategory.PHYSICAL -> listOf(
                Task("physical_01", "Morning Workout", "20-minute bodyweight session", category, TaskDifficulty.MODERATE, 35),
                Task("physical_02", "Hydration Mission", "Drink 8 glasses today", category, TaskDifficulty.EASY, 20),
                Task("physical_03", "Zero Sugar Window", "Avoid sugar for 6 hours", category, TaskDifficulty.MODERATE, 30),
                Task("physical_04", "Healthy Plate", "One clean meal, no junk", category, TaskDifficulty.EASY, 24),
                Task("physical_05", "Sleep Cutoff", "No phone after 10:30 PM", category, TaskDifficulty.HARD, 44),
                Task("physical_06", "Snack Replacement", "Replace snack with fruit", category, TaskDifficulty.EASY, 18)
            )
        }
    }
}
