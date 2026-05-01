package com.monkhex.app

import com.google.common.truth.Truth.assertThat
import com.monkhex.app.core.algorithm.AddictionCategoryMapper
import com.monkhex.app.domain.model.AddictionCategory
import org.junit.Test

class AddictionCategoryMapperTest {

    @Test
    fun `all supported addictions map into one of 3 categories`() {
        val addictions = listOf(
            "Social media addiction",
            "Instagram addiction",
            "TikTok/Reels addiction",
            "YouTube addiction",
            "Shorts addiction",
            "Phone addiction",
            "Notification addiction",
            "Gaming addiction",
            "Mobile gaming addiction",
            "PC/console gaming addiction",
            "Internet addiction",
            "Doomscrolling",
            "OTT binge watching",
            "Netflix binge addiction",
            "Anime binge addiction",
            "Web series addiction",
            "Online streaming addiction",
            "Chatting addiction",
            "Messaging addiction",
            "Meme consumption addiction",
            "Adult content addiction",
            "Self-stimulation addiction",
            "Fantasy addiction",
            "Escapism addiction",
            "Daydreaming addiction",
            "Dopamine chasing addiction",
            "Instant gratification addiction",
            "Overthinking addiction",
            "Negative thinking loops",
            "Validation addiction (likes/comments)",
            "Attention addiction",
            "Comparison addiction",
            "Stimulation addiction (constant stimulation need)",
            "Junk food addiction",
            "Fast food addiction",
            "Sugar addiction",
            "Snacking addiction",
            "Overeating addiction",
            "Emotional eating",
            "Soda addiction",
            "Caffeine addiction",
            "Energy drink addiction",
            "Nail biting",
            "Skin picking",
            "Hair pulling (mild cases)",
            "Mirror checking addiction",
            "Late night phone addiction",
            "Staying up late addiction",
            "Irregular sleep cycle addiction",
            "Comfort scrolling before sleep",
            "Music dependency (constant listening)",
            "Background noise dependency",
            "Headphone dependency",
            "Feed refreshing addiction",
            "Checking likes addiction",
            "Checking messages repeatedly",
            "App switching addiction",
            "Tab switching addiction",
            "Random browsing addiction",
            "Video binge loops",
            "Achievement chasing addiction",
            "Streak dependency addiction",
            "Reward loop addiction",
            "Procrastination loop addiction",
            "Avoidance addiction",
            "Comfort zone addiction",
            "Distraction addiction",
            "Stimulation dependency"
        )

        addictions.forEach { addiction ->
            val category = AddictionCategoryMapper.map(addiction)
            assertThat(category).isIn(AddictionCategory.entries)
            assertThat(AddictionCategoryMapper.isSupported(addiction)).isTrue()
        }
    }
}

