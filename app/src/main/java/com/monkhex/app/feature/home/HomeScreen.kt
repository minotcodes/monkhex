package com.monkhex.app.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.monkhex.app.core.designsystem.GlassCard
import com.monkhex.app.core.designsystem.MonkHexColors

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    HomeScreen(
        uiState = state,
        onTaskComplete = viewModel::onTaskCompleted,
        onRetry = viewModel::refreshDashboard
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onTaskComplete: (String) -> Unit,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MonkHexColors.Background,
                        Color(0xFF060A12)
                    )
                )
            )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MonkHexColors.Gold
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                HeroSection(
                    streak = uiState.userProfile?.streak ?: 0,
                    disciplineScore = uiState.userProfile?.disciplineScore ?: 0,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "TODAY'S MISSION",
                            style = MaterialTheme.typography.titleMedium,
                            color = MonkHexColors.Gold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = uiState.missionTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MonkHexColors.TextPrimary
                        )
                        Text(
                            text = uiState.missionSubtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MonkHexColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        StatsRow(
                            streak = uiState.userProfile?.streak ?: 0,
                            xp = uiState.userProfile?.xp ?: 0,
                            level = uiState.userProfile?.level ?: 1,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        TaskList(
                            tasks = uiState.tasks,
                            onTaskComplete = onTaskComplete,
                            onCustomTaskTap = {},
                            modifier = Modifier.weight(1f)
                        )
                        if (!uiState.errorMessage.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.errorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFF7D7D),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = onRetry)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroSection(
    streak: Int,
    disciplineScore: Int,
    modifier: Modifier = Modifier
) {
    val streakProgress by animateFloatAsState(
        targetValue = (streak.coerceIn(0, 30) / 30f),
        animationSpec = tween(900),
        label = "streakProgress"
    )
    val disciplineProgress by animateFloatAsState(
        targetValue = (disciplineScore.coerceIn(0, 100) / 100f),
        animationSpec = tween(900),
        label = "disciplineProgress"
    )

    Box(
        modifier = modifier
            .testTag("HeroSection")
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeOuter = Stroke(width = 22f, cap = StrokeCap.Round)
            val strokeInner = Stroke(width = 14f, cap = StrokeCap.Round)
            val sizeOuter = size.minDimension * 0.9f
            val sizeInner = size.minDimension * 0.72f

            drawArc(
                color = MonkHexColors.Elevated.copy(alpha = 0.7f),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = Offset((size.width - sizeOuter) / 2f, (size.height - sizeOuter) / 2f),
                size = Size(sizeOuter, sizeOuter),
                style = strokeOuter
            )

            drawArc(
                color = MonkHexColors.Gold,
                startAngle = 140f,
                sweepAngle = 260f * streakProgress,
                useCenter = false,
                topLeft = Offset((size.width - sizeOuter) / 2f, (size.height - sizeOuter) / 2f),
                size = Size(sizeOuter, sizeOuter),
                style = strokeOuter
            )

            drawArc(
                color = MonkHexColors.Elevated.copy(alpha = 0.6f),
                startAngle = 160f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = Offset((size.width - sizeInner) / 2f, (size.height - sizeInner) / 2f),
                size = Size(sizeInner, sizeInner),
                style = strokeInner
            )

            drawArc(
                color = MonkHexColors.Gold.copy(alpha = 0.8f),
                startAngle = 160f,
                sweepAngle = 240f * disciplineProgress,
                useCenter = false,
                topLeft = Offset((size.width - sizeInner) / 2f, (size.height - sizeInner) / 2f),
                size = Size(sizeInner, sizeInner),
                style = strokeInner
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF2D3547),
                                Color(0xFF141A28)
                            )
                        )
                    )
                    .border(1.dp, MonkHexColors.Gold.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Avatar",
                    tint = MonkHexColors.Gold,
                    modifier = Modifier.size(72.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "MONKHEX",
                color = MonkHexColors.Gold,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Text(
            text = "STREAK - $streak DAYS",
            color = MonkHexColors.TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 14.dp)
        )

        Text(
            text = "DISCIPLINE - ${disciplineScore.coerceIn(0, 100)}%",
            color = MonkHexColors.Gold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 14.dp)
        )
    }
}

@Composable
fun StatsRow(
    streak: Int,
    xp: Int,
    level: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.testTag("StatsRow"),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.LocalFireDepartment,
            title = "STREAK",
            value = streak.toString(),
            subtitle = "Days"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.Star,
            title = "XP",
            value = "%,d".format(xp),
            subtitle = "Power"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.EmojiEvents,
            title = "LEVEL",
            value = level.toString(),
            subtitle = "Rank"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String
) {
    GlassCard(
        modifier = modifier
            .height(116.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MonkHexColors.Gold,
                modifier = Modifier.size(20.dp)
            )
            Text(title, color = MonkHexColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
            Text(value, color = MonkHexColors.TextPrimary, style = MaterialTheme.typography.titleLarge)
            Text(subtitle, color = MonkHexColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun TaskList(
    tasks: List<MissionTaskUi>,
    onTaskComplete: (String) -> Unit,
    onCustomTaskTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.testTag("TaskList"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
            TaskItem(
                task = task,
                onClick = { onTaskComplete(task.id) },
                visible = true,
                animationDelay = index * 35
            )
        }
        item("custom_mission") {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(82.dp)
                    .clickable(onClick = onCustomTaskTap),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MonkHexColors.Background.copy(alpha = 0.55f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add mission",
                            tint = MonkHexColors.TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Add Your Own Mission",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MonkHexColors.TextPrimary
                        )
                        Text(
                            text = "Custom Task",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MonkHexColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: MissionTaskUi,
    onClick: () -> Unit,
    visible: Boolean,
    animationDelay: Int
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.985f else 1f,
        animationSpec = tween(130),
        label = "taskScale"
    )
    val glowPulse by animateFloatAsState(
        targetValue = if (task.isCompleted) 0.35f else 0.18f,
        animationSpec = tween(300),
        label = "taskGlow"
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(350, delayMillis = animationDelay)) +
            slideInVertically(animationSpec = tween(350, delayMillis = animationDelay), initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically()
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .scale(scale)
                .border(
                    width = 1.dp,
                    color = MonkHexColors.Cyan.copy(alpha = glowPulse),
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable(
                    enabled = !task.isCompleted,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MonkHexColors.Elevated.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = task.icon,
                        contentDescription = task.title,
                        tint = MonkHexColors.Gold
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MonkHexColors.TextPrimary
                    )
                    Text(
                        text = task.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MonkHexColors.TextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "+${task.xpReward} XP",
                    style = MaterialTheme.typography.titleMedium,
                    color = MonkHexColors.Gold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (task.isCompleted) MonkHexColors.Gold.copy(alpha = 0.2f) else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = if (task.isCompleted) MonkHexColors.Gold else MonkHexColors.TextSecondary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Triple(BottomTab.HOME, "HOME", Icons.Rounded.Home),
        Triple(BottomTab.STATS, "STATS", Icons.Rounded.BarChart),
        Triple(BottomTab.CHALLENGES, "CHALLENGES", Icons.Rounded.EmojiEvents),
        Triple(BottomTab.PROFILE, "PROFILE", Icons.Rounded.Person)
    )

    GlassCard(
        modifier = modifier
            .testTag("BottomNavBar")
            .fillMaxWidth()
            .height(78.dp),
        shape = RoundedCornerShape(34.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (tab, label, icon) ->
                val selected = tab == selectedTab
                val alpha by animateFloatAsState(
                    targetValue = if (selected) 1f else 0.55f,
                    animationSpec = tween(220),
                    label = "$label-alpha"
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (selected) MonkHexColors.Gold.copy(alpha = alpha) else MonkHexColors.TextSecondary.copy(alpha = alpha)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selected) MonkHexColors.Gold.copy(alpha = alpha) else MonkHexColors.TextSecondary.copy(alpha = alpha)
                    )
                }
            }
        }
    }
}
