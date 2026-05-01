package com.monkhex.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.monkhex.app.data.mapper.toDocument
import com.monkhex.app.data.mapper.toDomain
import com.monkhex.app.data.remote.model.AiSuggestionDocument
import com.monkhex.app.data.remote.gemini.GeminiSuggestionRemoteDataSource
import com.monkhex.app.domain.model.AiSuggestionCache
import com.monkhex.app.domain.model.UserProfile
import com.monkhex.app.domain.repository.SuggestionRepository
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuggestionRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val geminiSuggestionRemoteDataSource: GeminiSuggestionRemoteDataSource
) : SuggestionRepository {

    private val suggestionsCollection = firestore.collection("ai_suggestions")

    override suspend fun getCachedSuggestion(userId: String, date: String): AiSuggestionCache? {
        val docId = "${userId}_$date"
        val suggestion = suggestionsCollection.document(docId).get().await()
            .toObject(AiSuggestionDocument::class.java)
            ?.toDomain()
        if (suggestion == null) return null
        return if (suggestion.expiresAt.isAfter(Instant.now())) suggestion else null
    }

    override suspend fun generateSuggestion(userProfile: UserProfile, date: String): AiSuggestionCache? {
        val suggestions = geminiSuggestionRemoteDataSource.generateSuggestions(userProfile)
        if (suggestions.isEmpty()) return null
        val now = Instant.now()
        return AiSuggestionCache(
            userId = userProfile.userId,
            date = date,
            promptHash = "${userProfile.addictionType}-${userProfile.level}-$date".hashCode().toString(),
            tasks = suggestions,
            createdAt = now,
            expiresAt = now.plus(1, ChronoUnit.DAYS)
        )
    }

    override suspend fun cacheSuggestion(cache: AiSuggestionCache) {
        val docId = "${cache.userId}_${cache.date}"
        suggestionsCollection.document(docId).set(cache.toDocument()).await()
    }
}
