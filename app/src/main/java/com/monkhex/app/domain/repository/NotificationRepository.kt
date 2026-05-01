package com.monkhex.app.domain.repository

interface NotificationRepository {
    suspend fun registerToken(userId: String, token: String)
    suspend fun subscribeToCoreTopics()
}

