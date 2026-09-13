package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.model.GameMode
import com.example.ui.LudoArenaViewModel
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.CustomizationScreen
import com.example.ui.screens.DailyRewardsDialog
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LegalPolicyScreen
import com.example.ui.screens.OfflineSetupScreen
import com.example.ui.screens.OnlineLobbyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TournamentScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: LudoArenaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LudoArenaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LudoArenaApp(viewModel: LudoArenaViewModel) {
    val navController = rememberNavController()
    val profile by viewModel.userProfile.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val achievements by viewModel.achievements.collectAsState()

    var showDailyRewards by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") {
            HomeScreen(
                profile = profile,
                onNavigateToOnline = { navController.navigate("online_lobby") },
                onNavigateToOffline = { navController.navigate("offline_setup") },
                onNavigateToTournament = { navController.navigate("tournament") },
                onNavigateToLeaderboard = { navController.navigate("leaderboard") },
                onNavigateToAchievements = { navController.navigate("achievements") },
                onNavigateToCustomization = { navController.navigate("customization") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToAdmin = { navController.navigate("admin") },
                onNavigateToPolicy = { navController.navigate("policy") },
                onOpenDailyRewards = { showDailyRewards = true }
            )
        }

        composable("game") {
            GamePlayScreen(
                gameEngine = viewModel.gameEngine,
                multiplayerManager = viewModel.multiplayerManager,
                voiceChatManager = viewModel.voiceChatManager,
                onExitGame = {
                    viewModel.endMatch()
                    navController.popBackStack()
                }
            )
        }

        composable("offline_setup") {
            OfflineSetupScreen(
                onBackClick = { navController.popBackStack() },
                onStartGame = { mode, players ->
                    viewModel.startMatch(mode, players)
                    navController.navigate("game")
                }
            )
        }

        composable("online_lobby") {
            OnlineLobbyScreen(
                userProfile = profile,
                multiplayerManager = viewModel.multiplayerManager,
                onBackClick = { navController.popBackStack() },
                onStartGame = { mode, players, roomCode ->
                    viewModel.startMatch(mode, players, roomCode)
                    navController.navigate("game")
                }
            )
        }

        composable("tournament") {
            TournamentScreen(
                onBackClick = { navController.popBackStack() },
                onStartTournamentMatch = { mode, players ->
                    viewModel.startMatch(mode, players, "ARENA-FINALS")
                    navController.navigate("game")
                }
            )
        }

        composable("leaderboard") {
            LeaderboardScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("achievements") {
            AchievementsScreen(
                achievements = achievements,
                onClaimReward = { viewModel.claimAchievement(it) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                profile = profile,
                onSaveProfile = { viewModel.saveProfile(it) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("customization") {
            CustomizationScreen(
                profile = profile,
                onSaveCustomization = { viewModel.saveProfile(it) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                settings = settings,
                onSettingsChanged = { viewModel.updateSettings(it) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("admin") {
            AdminPanelScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("policy") {
            LegalPolicyScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }

    if (showDailyRewards) {
        DailyRewardsDialog(
            onClaimDay = { _, coins ->
                viewModel.addCoins(coins)
            },
            onDismiss = { showDailyRewards = false }
        )
    }
}

