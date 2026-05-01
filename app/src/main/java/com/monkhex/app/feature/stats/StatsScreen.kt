package com.monkhex.app.feature.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.monkhex.app.core.designsystem.MonkHexColors

@Composable
fun StatsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Stats analytics module ready for expansion.",
            style = MaterialTheme.typography.titleMedium,
            color = MonkHexColors.TextPrimary
        )
    }
}
