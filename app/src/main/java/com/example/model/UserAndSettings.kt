package com.example.model

data class UserProfile(
    val id: String = "u_local_user",
    val username: String = "LudoChampion",
    val displayName: String = "Ludo Champion",
    val playerId: String = "LA-78419",
    val avatarId: Int = 1,
    val level: Int = 5,
    val xp: Int = 1420,
    val xpToNextLevel: Int = 2000,
    val coins: Int = 5800,
    val trophies: Int = 420,
    val wins: Int = 18,
    val losses: Int = 6,
    val matchesPlayed: Int = 24,
    val currentStreak: Int = 4,
    val bestStreak: Int = 7,
    val selectedBoardTheme: String = "ROYAL_CLASSIC",
    val selectedDiceSkin: String = "GOLDEN_ROYAL",
    val selectedTokenSkin: String = "ROYAL_CROWN",
    val isVip: Boolean = false
) {
    val winRate: Float
        get() = if (matchesPlayed > 0) (wins.toFloat() / matchesPlayed * 100f) else 0f
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val rewardCoins: Int,
    val rewardXp: Int,
    val isUnlocked: Boolean = false
)

data class Friend(
    val id: String,
    val displayName: String,
    val playerId: String,
    val avatarId: Int,
    val trophies: Int,
    val isOnline: Boolean,
    val currentActivity: String? = null // e.g. "In Match", "Lobby", "Offline"
)

data class GameSettings(
    val soundFxEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val voiceChatEnabled: Boolean = true,
    val pushToTalk: Boolean = false,
    val textChatEnabled: Boolean = true,
    val turnTimerSeconds: Int = 20,
    val language: String = "en", // "en" or "hi"
    val graphicsQuality: String = "HIGH",
    val quickReactionsEnabled: Boolean = true,
    val autoPassEnabled: Boolean = true
)

data class DailyRewardDay(
    val dayNumber: Int,
    val title: String,
    val rewardDescription: String,
    val coinAmount: Int,
    val xpAmount: Int,
    val isClaimed: Boolean,
    val isAvailable: Boolean
)

data class LeaderboardEntry(
    val rank: Int,
    val playerId: String,
    val displayName: String,
    val avatarId: Int,
    val trophies: Int,
    val wins: Int,
    val winRate: Float,
    val country: String = "IN"
)
