package com.monkhex.app.domain.repository

import com.monkhex.app.domain.model.AiSuggestionCache
import com.monkhex.app.domain.model.UserProfile

interface SuggestionRepository {
    suspend fun getCachedSuggestion(userId: String, date: String): AiSuggestionCache?
    suspend fun generateSuggestion(userProfile: UserProfile, date: String): AiSuggestionCache?
    suspend fun cacheSuggestion(cache: AiSuggestionCache)
}

