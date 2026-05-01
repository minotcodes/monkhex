package com.monkhex.app.data.remote.gemini

import com.monkhex.app.BuildConfig
import com.monkhex.app.domain.model.AiSuggestionTask
import com.monkhex.app.domain.model.TaskDifficulty
import com.monkhex.app.domain.model.UserProfile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiSuggestionRemoteDataSource @Inject constructor(
    private val geminiApi: GeminiApi,
    private val moshi: Moshi
) {
    suspend fun generateSuggestions(userProfile: UserProfile): List<AiSuggestionTask> {
        if (BuildConfig.USE_FAKE_AI || BuildConfig.GEMINI_API_KEY.isBlank()) {
            return fakeSuggestions()
        }

        val prompt = """
            You are MonkHex discipline assistant.
            Generate exactly 6 daily tasks as a JSON array for:
            - addictionType: "${userProfile.addictionType}"
            - category: "${userProfile.category.firestoreValue}"
            - level: ${userProfile.level}
            Constraints:
            - actionable and measurable
            - title <= 4 words
            - description <= 8 words
            - difficulty in [EASY, MODERATE, HARD]
            - xpReward between 20 and 60
            Return strict JSON only. No markdown. No explanations.
            Shape:
            [
              {"title":"...", "description":"...", "difficulty":"MODERATE", "xpReward":30}
            ]
        """.trimIndent()

        return runCatching {
            val response = geminiApi.generateContent(
                model = "gemini-1.5-flash",
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = GeminiGenerateContentRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = prompt))
                        )
                    )
                )
            )
            val raw = response.candidates
                .firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                .orEmpty()
            parseSuggestions(raw)
        }.getOrElse {
            fakeSuggestions()
        }
    }

    private fun parseSuggestions(raw: String): List<AiSuggestionTask> {
        val cleaned = raw
            .replace("```json", "")
            .replace("```", "")
            .trim()

        val listType = Types.newParameterizedType(List::class.java, AiSuggestionTaskJson::class.java)
        val adapter: JsonAdapter<List<AiSuggestionTaskJson>> = moshi.adapter(listType)
        val parsed = adapter.fromJson(cleaned).orEmpty()

        return parsed.map {
            AiSuggestionTask(
                title = it.title.ifBlank { "Focus Sprint" },
                description = it.description.ifBlank { "Stay focused for 20 minutes" },
                difficulty = TaskDifficulty.fromString(it.difficulty),
                xpReward = it.xpReward.coerceIn(20, 60)
            )
        }
    }

    private fun fakeSuggestions(): List<AiSuggestionTask> = listOf(
        AiSuggestionTask(
            title = "No Scroll Window",
            description = "Stay off social apps 3 hours",
            difficulty = TaskDifficulty.MODERATE,
            xpReward = 40
        ),
        AiSuggestionTask(
            title = "Focused Workout",
            description = "20-minute bodyweight routine",
            difficulty = TaskDifficulty.MODERATE,
            xpReward = 35
        ),
        AiSuggestionTask(
            title = "Read Deep Work",
            description = "Read 15 pages with notes",
            difficulty = TaskDifficulty.EASY,
            xpReward = 30
        ),
        AiSuggestionTask(
            title = "Hydration Target",
            description = "Drink 8 glasses of water",
            difficulty = TaskDifficulty.EASY,
            xpReward = 20
        ),
        AiSuggestionTask(
            title = "Mind Reset",
            description = "10-minute breathing and journaling",
            difficulty = TaskDifficulty.MODERATE,
            xpReward = 32
        ),
        AiSuggestionTask(
            title = "Distraction Audit",
            description = "Track every urge for 2 hours",
            difficulty = TaskDifficulty.HARD,
            xpReward = 50
        )
    )

    private data class AiSuggestionTaskJson(
        val title: String = "",
        val description: String = "",
        val difficulty: String = "MODERATE",
        val xpReward: Int = 30
    )
}

