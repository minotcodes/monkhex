package com.monkhex.app.core.algorithm

import com.monkhex.app.domain.model.AddictionCategory

object AddictionCategoryMapper {

    private val digitalAddictions = setOf(
        "social media addiction",
        "instagram addiction",
        "tiktok/reels addiction",
        "youtube addiction",
        "shorts addiction",
        "phone addiction",
        "notification addiction",
        "gaming addiction",
        "mobile gaming addiction",
        "pc/console gaming addiction",
        "internet addiction",
        "doomscrolling",
        "ott binge watching",
        "netflix binge addiction",
        "anime binge addiction",
        "web series addiction",
        "online streaming addiction",
        "chatting addiction",
        "messaging addiction",
        "meme consumption addiction",
        "adult content addiction",
        "validation addiction (likes/comments)",
        "attention addiction",
        "comparison addiction",
        "late night phone addiction",
        "comfort scrolling before sleep",
        "music dependency (constant listening)",
        "background noise dependency",
        "headphone dependency",
        "feed refreshing addiction",
        "checking likes addiction",
        "checking messages repeatedly",
        "app switching addiction",
        "tab switching addiction",
        "random browsing addiction",
        "video binge loops",
        "achievement chasing addiction",
        "streak dependency addiction",
        "reward loop addiction",
        "procrastination loop addiction",
        "avoidance addiction",
        "comfort zone addiction",
        "distraction addiction"
    )

    private val mentalAddictions = setOf(
        "fantasy addiction",
        "escapism addiction",
        "daydreaming addiction",
        "dopamine chasing addiction",
        "instant gratification addiction",
        "overthinking addiction",
        "negative thinking loops",
        "stimulation addiction (constant stimulation need)",
        "stimulation dependency"
    )

    private val physicalAddictions = setOf(
        "self-stimulation addiction",
        "junk food addiction",
        "fast food addiction",
        "sugar addiction",
        "snacking addiction",
        "overeating addiction",
        "emotional eating",
        "soda addiction",
        "caffeine addiction",
        "energy drink addiction",
        "nail biting",
        "skin picking",
        "hair pulling (mild cases)",
        "mirror checking addiction",
        "staying up late addiction",
        "irregular sleep cycle addiction"
    )

    val supportedAddictions: Set<String> = digitalAddictions + mentalAddictions + physicalAddictions

    fun map(addictionType: String): AddictionCategory {
        val normalized = addictionType.trim().lowercase()
        return when {
            digitalAddictions.contains(normalized) -> AddictionCategory.DIGITAL
            mentalAddictions.contains(normalized) -> AddictionCategory.MENTAL
            physicalAddictions.contains(normalized) -> AddictionCategory.PHYSICAL
            else -> AddictionCategory.DIGITAL
        }
    }

    fun isSupported(addictionType: String): Boolean = supportedAddictions.contains(addictionType.trim().lowercase())
}

