package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "local_user",
    val username: String = "LudoChampion",
    val displayName: String = "Ludo Champion",
    val playerId: String = "LA-78419",
    val avatarId: Int = 1,
    val level: Int = 5,
    val xp: Int = 1420,
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
    val soundFxEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val voiceChatEnabled: Boolean = true,
    val textChatEnabled: Boolean = true,
    val language: String = "en",
    val lastClaimedDay: Int = 2,
    val lastClaimTimestamp: Long = 0L
)

@Entity(tableName = "match_history")
data class MatchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mode: String,
    val playerCount: Int,
    val winnerName: String,
    val playerPlacement: Int, // 1 = 1st, 2 = 2nd, etc.
    val durationSeconds: Int,
    val coinsEarned: Int,
    val xpEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val currentProgress: Int,
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long = 0L
)
