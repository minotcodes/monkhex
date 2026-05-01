package com.monkhex.app.domain.repository

interface AuthRepository {
    suspend fun signInAnonymouslyIfNeeded(): String
    fun currentUserId(): String?
}

