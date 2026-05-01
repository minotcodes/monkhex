package com.monkhex.app.domain.repository

import com.monkhex.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUserProfile(userId: String): Flow<UserProfile?>
    suspend fun getUserProfile(userId: String): UserProfile?
    suspend fun upsertUserProfile(profile: UserProfile)
}

