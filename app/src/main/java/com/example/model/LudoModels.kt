package com.example.model

enum class LudoColor(val displayName: String, val trackStartIndex: Int) {
    RED("Red", 0),
    GREEN("Green", 13),
    YELLOW("Yellow", 26),
    BLUE("Blue", 39)
}

data class BoardCoordinate(val row: Float, val col: Float)

object LudoBoardLayout {
    const val GRID_SIZE = 15
    const val MAX_STEPS = 57 // 0=base, 1=start cell, 51=last track step, 52-56=home path, 57=finished
    val SAFE_TRACK_INDICES = setOf(0, 8, 13, 21, 26, 34, 39, 47)

    // Clockwise 52-cell track coordinates (row, col in 15x15)
    val TRACK_COORDINATES: List<BoardCoordinate> = listOf(
        // Red start quadrant (0..12)
        BoardCoordinate(6f, 1f),   // 0: Red Start (Safe)
        BoardCoordinate(6f, 2f),   // 1
        BoardCoordinate(6f, 3f),   // 2
        BoardCoordinate(6f, 4f),   // 3
        BoardCoordinate(6f, 5f),   // 4
        BoardCoordinate(5f, 6f),   // 5
        BoardCoordinate(4f, 6f),   // 6
        BoardCoordinate(3f, 6f),   // 7
        BoardCoordinate(2f, 6f),   // 8: Safe Star
        BoardCoordinate(1f, 6f),   // 9
        BoardCoordinate(0f, 6f),   // 10
        BoardCoordinate(0f, 7f),   // 11
        BoardCoordinate(0f, 8f),   // 12

        // Green start quadrant (13..25)
        BoardCoordinate(1f, 8f),   // 13: Green Start (Safe)
        BoardCoordinate(2f, 8f),   // 14
        BoardCoordinate(3f, 8f),   // 15
        BoardCoordinate(4f, 8f),   // 16
        BoardCoordinate(5f, 8f),   // 17
        BoardCoordinate(6f, 9f),   // 18
        BoardCoordinate(6f, 10f),  // 19
        BoardCoordinate(6f, 11f),  // 20
        BoardCoordinate(6f, 12f),  // 21: Safe Star
        BoardCoordinate(6f, 13f),  // 22
        BoardCoordinate(6f, 14f),  // 23
        BoardCoordinate(7f, 14f),  // 24
        BoardCoordinate(8f, 14f),  // 25

        // Yellow start quadrant (26..38)
        BoardCoordinate(8f, 13f),  // 26: Yellow Start (Safe)
        BoardCoordinate(8f, 12f),  // 27
        BoardCoordinate(8f, 11f),  // 28
        BoardCoordinate(8f, 10f),  // 29
        BoardCoordinate(8f, 9f),   // 30
        BoardCoordinate(9f, 8f),   // 31
        BoardCoordinate(10f, 8f),  // 32
        BoardCoordinate(11f, 8f),  // 33
        BoardCoordinate(12f, 8f),  // 34: Safe Star
        BoardCoordinate(13f, 8f),  // 35
        BoardCoordinate(14f, 8f),  // 36
        BoardCoordinate(14f, 7f),  // 37
        BoardCoordinate(14f, 6f),  // 38

        // Blue start quadrant (39..51)
        BoardCoordinate(13f, 6f),  // 39: Blue Start (Safe)
        BoardCoordinate(12f, 6f),  // 40
        BoardCoordinate(11f, 6f),  // 41
        BoardCoordinate(10f, 6f),  // 42
        BoardCoordinate(9f, 6f),   // 43
        BoardCoordinate(8f, 5f),   // 44
        BoardCoordinate(8f, 4f),   // 45
        BoardCoordinate(8f, 3f),   // 46
        BoardCoordinate(8f, 2f),   // 47: Safe Star
        BoardCoordinate(8f, 1f),   // 48
        BoardCoordinate(8f, 0f),   // 49
        BoardCoordinate(7f, 0f),   // 50
        BoardCoordinate(6f, 0f)    // 51
    )

    // 5 steps home path + center for each color
    val HOME_PATHS: Map<LudoColor, List<BoardCoordinate>> = mapOf(
        LudoColor.RED to listOf(
            BoardCoordinate(7f, 1f),
            BoardCoordinate(7f, 2f),
            BoardCoordinate(7f, 3f),
            BoardCoordinate(7f, 4f),
            BoardCoordinate(7f, 5f),
            BoardCoordinate(7f, 6.5f) // Center Home
        ),
        LudoColor.GREEN to listOf(
            BoardCoordinate(1f, 7f),
            BoardCoordinate(2f, 7f),
            BoardCoordinate(3f, 7f),
            BoardCoordinate(4f, 7f),
            BoardCoordinate(5f, 7f),
            BoardCoordinate(6.5f, 7f) // Center Home
        ),
        LudoColor.YELLOW to listOf(
            BoardCoordinate(7f, 13f),
            BoardCoordinate(7f, 12f),
            BoardCoordinate(7f, 11f),
            BoardCoordinate(7f, 10f),
            BoardCoordinate(7f, 9f),
            BoardCoordinate(7f, 7.5f) // Center Home
        ),
        LudoColor.BLUE to listOf(
            BoardCoordinate(13f, 7f),
            BoardCoordinate(12f, 7f),
            BoardCoordinate(11f, 7f),
            BoardCoordinate(10f, 7f),
            BoardCoordinate(9f, 7f),
            BoardCoordinate(7.5f, 7f) // Center Home
        )
    )

    // Base tokens positions (4 per yard)
    val BASE_COORDINATES: Map<LudoColor, List<BoardCoordinate>> = mapOf(
        LudoColor.RED to listOf(
            BoardCoordinate(10.5f, 1.5f),
            BoardCoordinate(10.5f, 3.5f),
            BoardCoordinate(12.5f, 1.5f),
            BoardCoordinate(12.5f, 3.5f)
        ),
        LudoColor.GREEN to listOf(
            BoardCoordinate(1.5f, 1.5f),
            BoardCoordinate(1.5f, 3.5f),
            BoardCoordinate(3.5f, 1.5f),
            BoardCoordinate(3.5f, 3.5f)
        ),
        LudoColor.YELLOW to listOf(
            BoardCoordinate(1.5f, 10.5f),
            BoardCoordinate(1.5f, 12.5f),
            BoardCoordinate(3.5f, 10.5f),
            BoardCoordinate(3.5f, 12.5f)
        ),
        LudoColor.BLUE to listOf(
            BoardCoordinate(10.5f, 10.5f),
            BoardCoordinate(10.5f, 12.5f),
            BoardCoordinate(12.5f, 10.5f),
            BoardCoordinate(12.5f, 12.5f)
        )
    )

    fun getTrackIndexForStep(color: LudoColor, step: Int): Int {
        if (step !in 1..51) return -1
        return (color.trackStartIndex + (step - 1)) % 52
    }

    fun getCoordinateForToken(color: LudoColor, tokenIndex: Int, stepCount: Int): BoardCoordinate {
        return when {
            stepCount == 0 -> BASE_COORDINATES[color]!![tokenIndex]
            stepCount in 1..51 -> {
                val trackIndex = getTrackIndexForStep(color, stepCount)
                TRACK_COORDINATES[trackIndex]
            }
            stepCount in 52..57 -> {
                val homeIndex = (stepCount - 52).coerceIn(0, 5)
                HOME_PATHS[color]!![homeIndex]
            }
            else -> BoardCoordinate(7f, 7f)
        }
    }
}

data class Token(
    val id: Int, // 0..3
    val color: LudoColor,
    val stepCount: Int = 0 // 0=base, 1..51=track, 52..56=home path, 57=finished
) {
    val isInBase: Boolean get() = stepCount == 0
    val isOnTrack: Boolean get() = stepCount in 1..51
    val isInHomePath: Boolean get() = stepCount in 52..56
    val isFinished: Boolean get() = stepCount == LudoBoardLayout.MAX_STEPS

    fun canMove(diceRoll: Int): Boolean {
        if (isFinished) return false
        if (isInBase) return diceRoll == 6
        return (stepCount + diceRoll) <= LudoBoardLayout.MAX_STEPS
    }
}

enum class AiDifficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    EXPERT("Expert")
}

enum class GameMode(val title: String) {
    OFFLINE_PASS_AND_PLAY("Pass & Play"),
    OFFLINE_VS_AI("Vs Computer AI"),
    ONLINE_QUICK_MATCH("Quick Match"),
    ONLINE_PRIVATE_ROOM("Private Room"),
    TOURNAMENT("Tournament Cup")
}

data class Player(
    val id: String,
    val name: String,
    val color: LudoColor,
    val isAi: Boolean,
    val aiDifficulty: AiDifficulty = AiDifficulty.MEDIUM,
    val avatarId: Int = 0,
    val isMuted: Boolean = false,
    val isVoiceSpeaking: Boolean = false,
    val pingMs: Int = 28,
    val tokens: List<Token> = List(4) { index -> Token(id = index, color = color) }
) {
    val finishedTokensCount: Int get() = tokens.count { it.isFinished }
    val hasWon: Boolean get() = finishedTokensCount == 4
}

enum class GameTurnPhase {
    NEED_ROLL,
    ROLLING,
    SELECTING_TOKEN,
    MOVING_TOKEN,
    ROUND_TRANSITION,
    GAME_OVER
}

data class MoveHighlight(
    val tokenId: Int,
    val playerColor: LudoColor,
    val targetStep: Int
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val senderColor: LudoColor,
    val message: String,
    val isEmoji: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val isSystem: Boolean = false
)

data class TournamentMatch(
    val matchId: String,
    val roundName: String,
    val player1Name: String,
    val player2Name: String,
    val player1Color: LudoColor,
    val player2Color: LudoColor,
    val winnerName: String? = null,
    val isCompleted: Boolean = false
)
