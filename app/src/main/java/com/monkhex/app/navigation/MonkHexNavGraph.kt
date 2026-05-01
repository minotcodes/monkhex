package com.monkhex.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.monkhex.app.core.designsystem.MonkHexColors
import com.monkhex.app.feature.commitment.ChallengesScreen
import com.monkhex.app.feature.home.BottomNavBar
import com.monkhex.app.feature.home.BottomTab
import com.monkhex.app.feature.home.HomeRoute
import com.monkhex.app.feature.profile.ProfileScreen
import com.monkhex.app.feature.stats.StatsScreen

private object Routes {
    const val HOME = "home"
    const val STATS = "stats"
    const val CHALLENGES = "challenges"
    const val PROFILE = "profile"
}

@Composable
fun MonkHexNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selectedTab = when {
        currentDestination?.hierarchy?.any { it.route == Routes.STATS } == true -> BottomTab.STATS
        currentDestination?.hierarchy?.any { it.route == Routes.CHALLENGES } == true -> BottomTab.CHALLENGES
        currentDestination?.hierarchy?.any { it.route == Routes.PROFILE } == true -> BottomTab.PROFILE
        else -> BottomTab.HOME
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MonkHexColors.Background, MonkHexColors.Background)
                )
            ),
        containerColor = MonkHexColors.Background,
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    val route = when (tab) {
                        BottomTab.HOME -> Routes.HOME
                        BottomTab.STATS -> Routes.STATS
                        BottomTab.CHALLENGES -> Routes.CHALLENGES
                        BottomTab.PROFILE -> Routes.PROFILE
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            )
        }
    ) { contentPadding ->
        MonkHexNavHost(
            navController = navController,
            contentPadding = contentPadding
        )
    }
}

@Composable
private fun MonkHexNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = Modifier.padding(contentPadding)
    ) {
        composable(Routes.HOME) {
            HomeRoute()
        }
        composable(Routes.STATS) { StatsScreen() }
        composable(Routes.CHALLENGES) { ChallengesScreen() }
        composable(Routes.PROFILE) { ProfileScreen() }
    }
}
