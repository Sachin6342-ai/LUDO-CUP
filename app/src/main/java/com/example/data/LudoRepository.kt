package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.example.model.Achievement
import com.example.model.UserProfile
import com.example.model.GameSettings

class LudoRepository(private val database: AppDatabase) {

    private val defaultAchievements = listOf(
        Achievement("first_win", "First Glory", "Win your first Ludo Arena match", "crown", 1, 1, 300, 100, true),
        Achievement("10_wins", "Arena Contender", "Win 10 matches in any mode", "swords", 10, 10, 800, 300, true),
        Achievement("50_wins", "Ludo Champion", "Win 50 matches against rivals", "trophy", 18, 50, 2500, 1000, false),
        Achievement("100_wins", "Living Legend", "Reach 100 victories in the Arena", "gem", 18, 100, 6000, 2500, false),
        Achievement("streak_3", "On Fire", "Achieve a 3-win streak", "flame", 3, 3, 500, 200, true),
        Achievement("token_master", "Safe Havens", "Bring all 4 tokens home in a single match", "shield", 4, 4, 600, 250, true),
        Achievement("tournament_champ", "Tournament Victor", "Win a Tournament bracket championship", "medal", 1, 1, 1500, 800, true)
    )

    fun getUserProfile(): Flow<UserProfile> {
        return database.userProfileDao().getUserProfile().map { entity ->
            if (entity == null) {
                // Initialize default profile
                val initial = UserProfileEntity()
                withContext(Dispatchers.IO) {
                    database.userProfileDao().insertOrUpdateProfile(initial)
                }
                UserProfile()
            } else {
                UserProfile(
                    id = entity.id,
                    username = entity.username,
                    displayName = entity.displayName,
                    playerId = entity.playerId,
                    avatarId = entity.avatarId,
                    level = entity.level,
                    xp = entity.xp,
                    coins = entity.coins,
                    trophies = entity.trophies,
                    wins = entity.wins,
                    losses = entity.losses,
                    matchesPlayed = entity.matchesPlayed,
                    currentStreak = entity.currentStreak,
                    bestStreak = entity.bestStreak,
                    selectedBoardTheme = entity.selectedBoardTheme,
                    selectedDiceSkin = entity.selectedDiceSkin,
                    selectedTokenSkin = entity.selectedTokenSkin
                )
            }
        }
    }

    suspend fun updateProfile(profile: UserProfile) {
        withContext(Dispatchers.IO) {
            val existing = database.userProfileDao().getUserProfile()
            val entity = UserProfileEntity(
                id = profile.id,
                username = profile.username,
                displayName = profile.displayName,
                playerId = profile.playerId,
                avatarId = profile.avatarId,
                level = profile.level,
                xp = profile.xp,
                coins = profile.coins,
                trophies = profile.trophies,
                wins = profile.wins,
                losses = profile.losses,
                matchesPlayed = profile.matchesPlayed,
                currentStreak = profile.currentStreak,
                bestStreak = profile.bestStreak,
                selectedBoardTheme = profile.selectedBoardTheme,
                selectedDiceSkin = profile.selectedDiceSkin,
                selectedTokenSkin = profile.selectedTokenSkin
            )
            database.userProfileDao().insertOrUpdateProfile(entity)
        }
    }

    suspend fun recordMatchResult(isWinner: Boolean, coinsReward: Int, xpReward: Int, durationSec: Int, modeName: String) {
        withContext(Dispatchers.IO) {
            // Fetch current or default
            val current = UserProfileEntity()
            val newWins = if (isWinner) current.wins + 1 else current.wins
            val newLosses = if (!isWinner) current.losses + 1 else current.losses
            val newMatches = current.matchesPlayed + 1
            val newStreak = if (isWinner) current.currentStreak + 1 else 0
            val newBestStreak = maxOf(newStreak, current.bestStreak)
            val newTrophies = if (isWinner) current.trophies + 25 else maxOf(0, current.trophies - 8)
            val newCoins = current.coins + coinsReward
            val newXp = current.xp + xpReward
            val newLevel = 1 + (newXp / 1000)

            val updated = current.copy(
                wins = newWins,
                losses = newLosses,
                matchesPlayed = newMatches,
                currentStreak = newStreak,
                bestStreak = newBestStreak,
                trophies = newTrophies,
                coins = newCoins,
                xp = newXp,
                level = newLevel
            )
            database.userProfileDao().insertOrUpdateProfile(updated)

            database.matchHistoryDao().insertMatch(
                MatchHistoryEntity(
                    mode = modeName,
                    playerCount = 4,
                    winnerName = if (isWinner) current.displayName else "Rival",
                    playerPlacement = if (isWinner) 1 else 2,
                    durationSeconds = durationSec,
                    coinsEarned = coinsReward,
                    xpEarned = xpReward
                )
            )
        }
    }

    suspend fun addCoins(amount: Int) {
        withContext(Dispatchers.IO) {
            val entity = UserProfileEntity()
            database.userProfileDao().insertOrUpdateProfile(
                entity.copy(coins = entity.coins + amount)
            )
        }
    }

    fun getAchievements(): List<Achievement> {
        return defaultAchievements
    }
}
