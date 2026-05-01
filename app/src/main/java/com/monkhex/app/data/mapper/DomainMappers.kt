package com.monkhex.app.data.mapper

import com.monkhex.app.data.remote.model.AiSuggestionDocument
import com.monkhex.app.data.remote.model.AiSuggestionTaskDocument
import com.monkhex.app.data.remote.model.CommitmentDocument
import com.monkhex.app.data.remote.model.TaskDocument
import com.monkhex.app.data.remote.model.UserProfileDocument
import com.monkhex.app.data.remote.model.UserTaskDocument
import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.AiSuggestionCache
import com.monkhex.app.domain.model.AiSuggestionTask
import com.monkhex.app.domain.model.CommitmentPlan
import com.monkhex.app.domain.model.CommitmentPlanType
import com.monkhex.app.domain.model.CommitmentStatus
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.TaskDifficulty
import com.monkhex.app.domain.model.TaskSource
import com.monkhex.app.domain.model.UserProfile
import com.monkhex.app.domain.model.UserTask
import java.time.Instant

fun UserProfileDocument.toDomain(): UserProfile = UserProfile(
    userId = userId,
    email = email,
    mode = mode,
    addictionType = addictionType,
    category = AddictionCategory.fromFirestoreValue(category),
    level = level,
    xp = xp,
    streak = streak,
    disciplineScore = disciplineScore
)

fun UserProfile.toDocument(): UserProfileDocument = UserProfileDocument(
    userId = userId,
    email = email,
    mode = mode,
    addictionType = addictionType,
    category = category.firestoreValue,
    level = level,
    xp = xp,
    streak = streak,
    disciplineScore = disciplineScore
)

fun TaskDocument.toDomain(): Task = Task(
    taskId = taskId,
    title = title,
    description = description,
    category = AddictionCategory.fromFirestoreValue(category),
    difficulty = TaskDifficulty.fromString(difficulty),
    xpReward = xpReward,
    active = active
)

fun Task.toDocument(): TaskDocument = TaskDocument(
    taskId = taskId,
    title = title,
    description = description,
    category = category.firestoreValue,
    difficulty = difficulty.name,
    xpReward = xpReward,
    active = active
)

fun UserTaskDocument.toDomain(): UserTask = UserTask(
    userId = userId,
    date = date,
    selectedTaskIds = selectedTaskIds,
    completedTaskIds = completedTaskIds,
    source = TaskSource.fromFirestoreValue(source)
)

fun UserTask.toDocument(): UserTaskDocument = UserTaskDocument(
    userId = userId,
    date = date,
    selectedTaskIds = selectedTaskIds,
    completedTaskIds = completedTaskIds,
    source = source.firestoreValue
)

fun CommitmentDocument.toDomain(): CommitmentPlan = CommitmentPlan(
    userId = userId,
    planType = CommitmentPlanType.fromDays(planType),
    planAmount = planAmount,
    startDate = startDate,
    missedDays = missedDays,
    consecutiveMisses = consecutiveMisses,
    penalty = penalty,
    refundAmount = refundAmount,
    status = CommitmentStatus.entries.firstOrNull { it.name.equals(status, ignoreCase = true) }
        ?: CommitmentStatus.ACTIVE
)

fun CommitmentPlan.toDocument(): CommitmentDocument = CommitmentDocument(
    userId = userId,
    planType = planType.days,
    planAmount = planAmount,
    startDate = startDate,
    missedDays = missedDays,
    consecutiveMisses = consecutiveMisses,
    penalty = penalty,
    refundAmount = refundAmount,
    status = status.name
)

fun AiSuggestionDocument.toDomain(): AiSuggestionCache = AiSuggestionCache(
    userId = userId,
    date = date,
    promptHash = promptHash,
    tasks = tasks.map { it.toDomain() },
    createdAt = Instant.ofEpochMilli(createdAtEpochMillis),
    expiresAt = Instant.ofEpochMilli(expiresAtEpochMillis)
)

fun AiSuggestionTaskDocument.toDomain(): AiSuggestionTask = AiSuggestionTask(
    title = title,
    description = description,
    difficulty = TaskDifficulty.fromString(difficulty),
    xpReward = xpReward
)

fun AiSuggestionCache.toDocument(): AiSuggestionDocument = AiSuggestionDocument(
    userId = userId,
    date = date,
    promptHash = promptHash,
    tasks = tasks.map { it.toDocument() },
    createdAtEpochMillis = createdAt.toEpochMilli(),
    expiresAtEpochMillis = expiresAt.toEpochMilli()
)

fun AiSuggestionTask.toDocument(): AiSuggestionTaskDocument = AiSuggestionTaskDocument(
    title = title,
    description = description,
    difficulty = difficulty.name,
    xpReward = xpReward
)
