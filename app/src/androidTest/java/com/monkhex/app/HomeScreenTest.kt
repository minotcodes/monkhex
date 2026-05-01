package com.monkhex.app

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhoneAndroid
import com.monkhex.app.core.designsystem.MonkHexTheme
import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.UserProfile
import com.monkhex.app.feature.home.HomeScreen
import com.monkhex.app.feature.home.HomeUiState
import com.monkhex.app.feature.home.MissionTaskUi
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun dashboardRendersCoreSections() {
        val state = HomeUiState(
            isLoading = false,
            userProfile = UserProfile(
                userId = "u1",
                addictionType = "Social media addiction",
                category = AddictionCategory.DIGITAL,
                level = 18,
                xp = 2450,
                streak = 12,
                disciplineScore = 78
            ),
            tasks = listOf(
                MissionTaskUi("1", "Stay Off Social Media", "For next 3 hours", 40, Icons.Rounded.PhoneAndroid)
            )
        )

        composeRule.setContent {
            MonkHexTheme {
                HomeScreen(
                    uiState = state,
                    onTaskComplete = {},
                    onRetry = {}
                )
            }
        }

        composeRule.onNodeWithTag("HeroSection").assertExists()
        composeRule.onNodeWithTag("StatsRow").assertExists()
        composeRule.onNodeWithTag("TaskList").assertExists()
    }
}
