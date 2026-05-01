package com.monkhex.app.data.remote.gemini

data class GeminiGenerateContentRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig = GeminiGenerationConfig()
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiGenerationConfig(
    val temperature: Float = 0.35f,
    val responseMimeType: String = "application/json"
)

data class GeminiGenerateContentResponse(
    val candidates: List<GeminiCandidate> = emptyList()
)

data class GeminiCandidate(
    val content: GeminiContentResponse = GeminiContentResponse()
)

data class GeminiContentResponse(
    val parts: List<GeminiPartResponse> = emptyList()
)

data class GeminiPartResponse(
    val text: String = ""
)

