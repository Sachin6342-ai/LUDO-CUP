package com.example.engine

import com.example.model.BoardCoordinate
import com.example.model.GameMode
import com.example.model.GameTurnPhase
import com.example.model.LudoBoardLayout
import com.example.model.LudoColor
import com.example.model.Player
import com.example.model.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class GameUiState(
    val gameMode: GameMode = GameMode.OFFLINE_VS_AI,
    val players: List<Player> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val diceValue: Int = 1,
    val isDiceRolling: Boolean = false,
    val turnPhase: GameTurnPhase = GameTurnPhase.NEED_ROLL,
    val consecutiveSixes: Int = 0,
    val timerSecondsRemaining: Int = 20,
    val movableTokenIds: Set<Int> = emptySet(),
    val movingTokenId: Int? = null,
    val animatedCoordinate: BoardCoordinate? = null,
    val winners: List<Player> = emptyList(),
    val isMatchOver: Boolean = false,
    val statusBanner: String = "Roll the dice to begin!",
    val lastCapturedPlayerColor: LudoColor? = null,
    val roomCode: String = "LA-5821",
    val isOnlineSyncing: Boolean = false
) {
    val currentPlayer: Player?
        get() = if (players.isNotEmpty() && currentPlayerIndex in players.indices) players[currentPlayerIndex] else null
}

class LudoGameEngine(
    private val scope: CoroutineScope,
    private val soundHelper: SoundAndHapticHelper? = null,
    private val isSoundEnabled: () -> Boolean = { true },
    private val isVibrateEnabled: () -> Boolean = { true }
) {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var aiJob: Job? = null

    fun startNewGame(
        mode: GameMode,
        playerConfigs: List<Player>,
        roomCode: String = "LA-${Random.nextInt(1000, 9999)}"
    ) {
        timerJob?.cancel()
        aiJob?.cancel()

        _uiState.value = GameUiState(
            gameMode = mode,
            players = playerConfigs,
            currentPlayerIndex = 0,
            diceValue = 1,
            isDiceRolling = false,
            turnPhase = GameTurnPhase.NEED_ROLL,
            consecutiveSixes = 0,
            timerSecondsRemaining = 20,
            movableTokenIds = emptySet(),
            winners = emptyList(),
            isMatchOver = false,
            statusBanner = "${playerConfigs.firstOrNull()?.name ?: "Player"}'s turn! Tap dice to roll.",
            roomCode = roomCode
        )

        startTurnTimer()
        checkAiTurn()
    }

    fun rollDice() {
        val state = _uiState.value
        if (state.turnPhase != GameTurnPhase.NEED_ROLL || state.isDiceRolling || state.isMatchOver) return

        val player = state.currentPlayer ?: return

        scope.launch {
            _uiState.value = state.copy(
                isDiceRolling = true,
                turnPhase = GameTurnPhase.ROLLING,
                statusBanner = "${player.name} is rolling..."
            )

            soundHelper?.playDiceRollSound(isSoundEnabled(), isVibrateEnabled())

            // Smooth dice rolling animation
            for (i in 0..5) {
                val tempRoll = Random.nextInt(1, 7)
                _uiState.value = _uiState.value.copy(diceValue = tempRoll)
                delay(60)
            }

            val finalRoll = Random.nextInt(1, 7)
            val newSixes = if (finalRoll == 6) state.consecutiveSixes + 1 else 0

            // 3 consecutive 6s penalty
            if (newSixes >= 3) {
                _uiState.value = _uiState.value.copy(
                    diceValue = finalRoll,
                    isDiceRolling = false,
                    consecutiveSixes = 0,
                    turnPhase = GameTurnPhase.ROUND_TRANSITION,
                    statusBanner = "Three 6s in a row! Turn forfeited."
                )
                delay(1200)
                advanceToNextPlayer()
                return@launch
            }

            val validTokens = player.tokens.filter { it.canMove(finalRoll) }
            val validIds = validTokens.map { it.id }.toSet()

            _uiState.value = _uiState.value.copy(
                diceValue = finalRoll,
                isDiceRolling = false,
                consecutiveSixes = newSixes,
                movableTokenIds = validIds
            )

            if (validTokens.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    turnPhase = GameTurnPhase.ROUND_TRANSITION,
                    statusBanner = "${player.name} rolled $finalRoll - No valid moves!"
                )
                delay(1000)
                advanceToNextPlayer()
            } else if (validTokens.size == 1 && !player.isAi) {
                // Auto move single option for human player convenience
                _uiState.value = _uiState.value.copy(
                    turnPhase = GameTurnPhase.SELECTING_TOKEN,
                    statusBanner = "Moving token..."
                )
                delay(300)
                moveToken(validTokens.first().id)
            } else {
                _uiState.value = _uiState.value.copy(
                    turnPhase = GameTurnPhase.SELECTING_TOKEN,
                    statusBanner = if (player.isAi) "AI is choosing token..." else "Select a token to move!"
                )
                if (player.isAi) {
                    delay(700)
                    val aiChoice = LudoAi.chooseTokenToMove(player, _uiState.value.players, finalRoll, player.aiDifficulty)
                    if (aiChoice != null) {
                        moveToken(aiChoice.id)
                    } else {
                        advanceToNextPlayer()
                    }
                }
            }
        }
    }

    fun onTokenClicked(tokenId: Int) {
        val state = _uiState.value
        if (state.turnPhase != GameTurnPhase.SELECTING_TOKEN || state.isDiceRolling) return
        val player = state.currentPlayer ?: return
        if (player.isAi) return // AI handles its own click

        if (state.movableTokenIds.contains(tokenId)) {
            moveToken(tokenId)
        }
    }

    private fun moveToken(tokenId: Int) {
        val state = _uiState.value
        val player = state.currentPlayer ?: return
        val token = player.tokens.find { it.id == tokenId } ?: return
        val diceRoll = state.diceValue

        scope.launch {
            _uiState.value = state.copy(
                turnPhase = GameTurnPhase.MOVING_TOKEN,
                movableTokenIds = emptySet(),
                movingTokenId = tokenId
            )

            val initialStep = token.stepCount
            val targetStep = if (token.isInBase) 1 else initialStep + diceRoll

            // Smooth step-by-step animation
            if (token.isInBase) {
                // Releasing from base
                soundHelper?.playTokenMoveSound(isSoundEnabled())
                delay(200)
            } else {
                for (s in (initialStep + 1)..targetStep) {
                    val coord = LudoBoardLayout.getCoordinateForToken(player.color, tokenId, s)
                    _uiState.value = _uiState.value.copy(animatedCoordinate = coord)
                    soundHelper?.playTokenMoveSound(isSoundEnabled())
                    delay(120)
                }
            }

            // Update token position
            val updatedTokens = player.tokens.map {
                if (it.id == tokenId) it.copy(stepCount = targetStep) else it
            }

            var extraTurnGranted = diceRoll == 6
            var capturedColor: LudoColor? = null

            // Check Home entry
            if (targetStep == LudoBoardLayout.MAX_STEPS) {
                soundHelper?.playHomeEntrySound(isSoundEnabled(), isVibrateEnabled())
                extraTurnGranted = true // finishing token grants bonus turn!
            }

            // Check Capture opponent on track
            val trackIndex = if (targetStep in 1..51) LudoBoardLayout.getTrackIndexForStep(player.color, targetStep) else -1
            val isSafe = trackIndex == -1 || LudoBoardLayout.SAFE_TRACK_INDICES.contains(trackIndex)

            var updatedPlayers = state.players.map { p ->
                if (p.id == player.id) {
                    p.copy(tokens = updatedTokens)
                } else if (!isSafe) {
                    // Check if any opponent token is on this cell
                    var wasCaptured = false
                    val capturedTokens = p.tokens.map { oppToken ->
                        if (oppToken.isOnTrack && LudoBoardLayout.getTrackIndexForStep(p.color, oppToken.stepCount) == trackIndex) {
                            wasCaptured = true
                            oppToken.copy(stepCount = 0) // send to base!
                        } else {
                            oppToken
                        }
                    }
                    if (wasCaptured) {
                        capturedColor = p.color
                    }
                    p.copy(tokens = capturedTokens)
                } else {
                    p
                }
            }

            if (capturedColor != null) {
                soundHelper?.playCaptureSound(isSoundEnabled(), isVibrateEnabled())
                extraTurnGranted = true // capture grants bonus turn!
            }

            // Check winner
            val updatedCurrentPlayer = updatedPlayers.first { it.id == player.id }
            val hasWon = updatedCurrentPlayer.hasWon
            var winnersList = state.winners
            var matchFinished = false

            if (hasWon && !winnersList.any { it.id == player.id }) {
                winnersList = winnersList + updatedCurrentPlayer
                soundHelper?.playVictoryFanfare(isSoundEnabled(), isVibrateEnabled())
                // In 2 player or when only 1 active player remains, match ends
                if (winnersList.size >= updatedPlayers.size - 1 || updatedPlayers.size == 2) {
                    matchFinished = true
                }
            }

            val statusMsg = when {
                matchFinished -> "${winnersList.first().name} has WON the match!"
                capturedColor != null -> "${player.name} captured ${capturedColor?.displayName}! Extra roll!"
                targetStep == LudoBoardLayout.MAX_STEPS -> "${player.name} brought a token HOME! Extra roll!"
                extraTurnGranted -> "${player.name} rolled a 6! Roll again!"
                else -> "${player.name} moved to step $targetStep"
            }

            _uiState.value = _uiState.value.copy(
                players = updatedPlayers,
                turnPhase = if (matchFinished) GameTurnPhase.GAME_OVER else GameTurnPhase.ROUND_TRANSITION,
                movingTokenId = null,
                animatedCoordinate = null,
                winners = winnersList,
                isMatchOver = matchFinished,
                statusBanner = statusMsg,
                lastCapturedPlayerColor = capturedColor
            )

            if (matchFinished) {
                timerJob?.cancel()
                return@launch
            }

            delay(600)

            if (extraTurnGranted) {
                // Keep same player
                _uiState.value = _uiState.value.copy(
                    turnPhase = GameTurnPhase.NEED_ROLL,
                    timerSecondsRemaining = 20,
                    statusBanner = "${player.name} has a bonus roll!"
                )
                startTurnTimer()
                checkAiTurn()
            } else {
                advanceToNextPlayer()
            }
        }
    }

    private fun advanceToNextPlayer() {
        val state = _uiState.value
        if (state.isMatchOver) return

        var nextIndex = (state.currentPlayerIndex + 1) % state.players.size
        // Skip players who have already won
        var attempts = 0
        while (attempts < state.players.size && state.winners.any { it.id == state.players[nextIndex].id }) {
            nextIndex = (nextIndex + 1) % state.players.size
            attempts++
        }

        val nextPlayer = state.players[nextIndex]

        _uiState.value = state.copy(
            currentPlayerIndex = nextIndex,
            turnPhase = GameTurnPhase.NEED_ROLL,
            consecutiveSixes = 0,
            movableTokenIds = emptySet(),
            timerSecondsRemaining = 20,
            statusBanner = "${nextPlayer.name}'s turn! Tap dice to roll."
        )

        startTurnTimer()
        checkAiTurn()
    }

    private fun startTurnTimer() {
        timerJob?.cancel()
        timerJob = scope.launch(Dispatchers.Default) {
            var timeLeft = 20
            while (timeLeft > 0 && isActive) {
                delay(1000)
                timeLeft--
                _uiState.value = _uiState.value.copy(timerSecondsRemaining = timeLeft)
            }
            // Timeout handling
            if (isActive && !_uiState.value.isMatchOver) {
                handleTimeout()
            }
        }
    }

    private fun handleTimeout() {
        val state = _uiState.value
        if (state.turnPhase == GameTurnPhase.NEED_ROLL) {
            // Auto roll on timeout
            rollDice()
        } else if (state.turnPhase == GameTurnPhase.SELECTING_TOKEN) {
            // Auto pick any valid token
            val firstToken = state.movableTokenIds.firstOrNull()
            if (firstToken != null) {
                moveToken(firstToken)
            } else {
                advanceToNextPlayer()
            }
        }
    }

    private fun checkAiTurn() {
        aiJob?.cancel()
        val player = _uiState.value.currentPlayer ?: return
        if (player.isAi && !_uiState.value.isMatchOver) {
            aiJob = scope.launch {
                delay(800) // Brief natural thinking pause
                rollDice()
            }
        }
    }

    fun cleanUp() {
        timerJob?.cancel()
        aiJob?.cancel()
    }
}
