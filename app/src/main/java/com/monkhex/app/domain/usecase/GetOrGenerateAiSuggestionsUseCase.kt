package com.monkhex.app.domain.usecase

import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.model.UserProfile
import com.monkhex.app.domain.repository.SuggestionRepository
import java.time.Instant
import javax.inject.Inject

class GetOrGenerateAiSuggestionsUseCase @Inject constructor(
    private val suggestionRepository: SuggestionRepository
) {
    suspend operator fun invoke(
        userProfile: UserProfile,
        date: String,
        aiEnabled: Boolean
    ): List<Task> {
        if (!aiEnabled) return emptyList()

        val cached = suggestionRepository.getCachedSuggestion(userProfile.userId, date)
        if (cached != null && cached.expiresAt.isAfter(Instant.now())) {
            return cached.tasks.mapIndexed { index, suggestion ->
                Task(
                    taskId = "ai-${date}-${index}-${suggestion.title.hashCode()}",
                    title = suggestion.title,
                    description = suggestion.description,
                    category = userProfile.category,
                    difficulty = suggestion.difficulty,
                    xpReward = suggestion.xpReward.coerceIn(15, 80),
                    generatedByAi = true
                )
            }
        }

        val generated = suggestionRepository.generateSuggestion(userProfile, date) ?: return emptyList()
        suggestionRepository.cacheSuggestion(generated)

        return generated.tasks.mapIndexed { index, suggestion ->
            Task(
                taskId = "ai-${date}-${index}-${suggestion.title.hashCode()}",
                title = suggestion.title,
                description = suggestion.description,
                category = userProfile.category,
                difficulty = suggestion.difficulty,
                xpReward = suggestion.xpReward.coerceIn(15, 80),
                generatedByAi = true
            )
        }
    }
}

