package com.monkhex.app.feature.commitment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.monkhex.app.core.designsystem.MonkHexColors

@Composable
fun ChallengesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Commitment challenges are active from dashboard tracking.",
            style = MaterialTheme.typography.titleMedium,
            color = MonkHexColors.TextPrimary
        )
    }
}
