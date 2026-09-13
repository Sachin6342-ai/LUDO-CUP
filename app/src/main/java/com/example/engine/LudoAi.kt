package com.example.engine

import com.example.model.AiDifficulty
import com.example.model.LudoBoardLayout
import com.example.model.LudoColor
import com.example.model.Player
import com.example.model.Token
import kotlin.random.Random

object LudoAi {

    fun chooseTokenToMove(
        aiPlayer: Player,
        allPlayers: List<Player>,
        diceRoll: Int,
        difficulty: AiDifficulty
    ): Token? {
        val movableTokens = aiPlayer.tokens.filter { it.canMove(diceRoll) }
        if (movableTokens.isEmpty()) return null
        if (movableTokens.size == 1) return movableTokens.first()

        return when (difficulty) {
            AiDifficulty.EASY -> {
                // Easy: Random choice
                movableTokens.random()
            }
            AiDifficulty.MEDIUM -> {
                // Medium: Prioritize releasing new token on 6, or capturing opponent
                val captureToken = findCapturingToken(aiPlayer, allPlayers, movableTokens, diceRoll)
                if (captureToken != null) return captureToken

                if (diceRoll == 6) {
                    val baseToken = movableTokens.firstOrNull { it.isInBase }
                    if (baseToken != null) return baseToken
                }

                // Or advance token farthest along
                movableTokens.maxByOrNull { it.stepCount } ?: movableTokens.first()
            }
            AiDifficulty.HARD, AiDifficulty.EXPERT -> {
                // Hard & Expert: Full heuristic score
                evaluateBestMove(aiPlayer, allPlayers, movableTokens, diceRoll, difficulty == AiDifficulty.EXPERT)
            }
        }
    }

    private fun findCapturingToken(
        player: Player,
        allPlayers: List<Player>,
        movableTokens: List<Token>,
        diceRoll: Int
    ): Token? {
        for (token in movableTokens) {
            val targetStep = if (token.isInBase) 1 else token.stepCount + diceRoll
            if (targetStep in 1..51) {
                val targetTrackIndex = LudoBoardLayout.getTrackIndexForStep(player.color, targetStep)
                if (!LudoBoardLayout.SAFE_TRACK_INDICES.contains(targetTrackIndex)) {
                    val hasOpponent = allPlayers.filter { it.color != player.color }.any { opp ->
                        opp.tokens.any { oppToken ->
                            oppToken.isOnTrack && LudoBoardLayout.getTrackIndexForStep(opp.color, oppToken.stepCount) == targetTrackIndex
                        }
                    }
                    if (hasOpponent) return token
                }
            }
        }
        return null
    }

    private fun evaluateBestMove(
        player: Player,
        allPlayers: List<Player>,
        movableTokens: List<Token>,
        diceRoll: Int,
        isExpert: Boolean
    ): Token {
        var bestToken = movableTokens.first()
        var bestScore = Int.MIN_VALUE

        val opponents = allPlayers.filter { it.color != player.color }

        for (token in movableTokens) {
            var score = 0
            val currentStep = token.stepCount
            val targetStep = if (token.isInBase) 1 else currentStep + diceRoll

            // 1. Scoring Home (Finish token)
            if (targetStep == LudoBoardLayout.MAX_STEPS) {
                score += 500 // Completing a token is massive
            }

            // 2. Entering Home Path (Steps 52-56)
            if (targetStep >= 52 && currentStep < 52) {
                score += 250 // Entering safe home corridor
            }

            // 3. Capturing opponent
            if (targetStep in 1..51) {
                val targetTrack = LudoBoardLayout.getTrackIndexForStep(player.color, targetStep)
                val isSafe = LudoBoardLayout.SAFE_TRACK_INDICES.contains(targetTrack)

                if (!isSafe) {
                    val capturedCount = opponents.sumOf { opp ->
                        opp.tokens.count { oppToken ->
                            oppToken.isOnTrack && LudoBoardLayout.getTrackIndexForStep(opp.color, oppToken.stepCount) == targetTrack
                        }
                    }
                    if (capturedCount > 0) {
                        score += 350 * capturedCount
                    }
                } else {
                    // Safe Star cell bonus
                    score += 120
                }

                // 4. Vulnerability check: Is an opponent 1..6 steps behind our landing cell?
                if (!isSafe) {
                    val threatened = opponents.any { opp ->
                        opp.tokens.any { oppToken ->
                            if (!oppToken.isOnTrack) return@any false
                            val oppTrack = LudoBoardLayout.getTrackIndexForStep(opp.color, oppToken.stepCount)
                            val distance = (targetTrack - oppTrack + 52) % 52
                            distance in 1..6
                        }
                    }
                    if (threatened) {
                        score -= if (isExpert) 140 else 80
                    }
                }
            }

            // 5. Escaping immediate danger
            if (token.isOnTrack) {
                val currentTrack = LudoBoardLayout.getTrackIndexForStep(player.color, currentStep)
                val isCurrentlySafe = LudoBoardLayout.SAFE_TRACK_INDICES.contains(currentTrack)
                if (!isCurrentlySafe) {
                    val inDangerNow = opponents.any { opp ->
                        opp.tokens.any { oppToken ->
                            if (!oppToken.isOnTrack) return@any false
                            val oppTrack = LudoBoardLayout.getTrackIndexForStep(opp.color, oppToken.stepCount)
                            val dist = (currentTrack - oppTrack + 52) % 52
                            dist in 1..6
                        }
                    }
                    if (inDangerNow) {
                        score += if (isExpert) 160 else 100 // Escape danger!
                    }
                }
            }

            // 6. Breaking out of base with 6
            if (token.isInBase && diceRoll == 6) {
                // If we don't have many tokens active, release
                val activeTokens = player.tokens.count { it.isOnTrack }
                score += if (activeTokens == 0) 280 else 180
            }

            // 7. General forward progress
            score += targetStep * 2

            // Add tiny random jitter for expert natural play
            val jitter = if (isExpert) Random.nextInt(5) else Random.nextInt(15)
            score += jitter

            if (score > bestScore) {
                bestScore = score
                bestToken = token
            }
        }

        return bestToken
    }
}
