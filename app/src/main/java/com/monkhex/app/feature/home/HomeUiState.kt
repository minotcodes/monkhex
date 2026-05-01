package com.monkhex.app.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.monkhex.app.domain.model.CommitmentPlan
import com.monkhex.app.domain.model.UserProfile

enum class BottomTab {
    HOME,
    STATS,
    CHALLENGES,
    PROFILE
}

data class MissionTaskUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val xpReward: Int,
    val icon: ImageVector,
    val isCompleted: Boolean = false
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile? = null,
    val commitmentPlan: CommitmentPlan? = null,
    val missionTitle: String = "BUILD YOURSELF.",
    val missionSubtitle: String = "Choose. Commit. Conquer.",
    val tasks: List<MissionTaskUi> = emptyList(),
    val selectedTab: BottomTab = BottomTab.HOME,
    val errorMessage: String? = null
)

internal fun mapIcon(title: String): ImageVector {
    val lower = title.lowercase()
    return when {
        "social" in lower || "phone" in lower || "app" in lower -> Icons.Rounded.PhoneAndroid
        "workout" in lower || "exercise" in lower -> Icons.Rounded.FitnessCenter
        "read" in lower || "book" in lower -> Icons.Rounded.Bolt
        "water" in lower || "hydration" in lower -> Icons.Rounded.WaterDrop
        else -> Icons.Rounded.Bolt
    }
}
