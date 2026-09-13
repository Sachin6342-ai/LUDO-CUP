package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.LudoGameEngine
import com.example.engine.MultiplayerSyncManager
import com.example.engine.VoiceChatManager
import com.example.model.LudoColor
import com.example.ui.components.DiceComponent
import com.example.ui.components.InGameChatSheet
import com.example.ui.components.LudoBoardView
import com.example.ui.components.PlayerCardView
import com.example.ui.components.VictoryDialog
import com.example.ui.components.VoiceChatControls
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface
import com.example.ui.theme.LudoBlue
import com.example.ui.theme.LudoGreen
import com.example.ui.theme.LudoRed
import com.example.ui.theme.LudoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePlayScreen(
    gameEngine: LudoGameEngine,
    multiplayerManager: MultiplayerSyncManager,
    voiceChatManager: VoiceChatManager,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameState by gameEngine.uiState.collectAsState()
    val lobbyState by multiplayerManager.lobbyState.collectAsState()
    val voiceState by voiceChatManager.voiceState.collectAsState()

    var showExitDialog by remember { mutableStateOf(false) }
    var showChatSheet by remember { mutableStateOf(false) }
    var showVoiceControls by remember { mutableStateOf(false) }

    val chatSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = gameState.gameMode.name.replace("_", " "),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Room: ${gameState.roomCode}",
                            color = ArenaGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Exit game", tint = Color.White)
                    }
                },
                actions = {
                    // Voice Chat Toggle
                    IconButton(onClick = { showVoiceControls = !showVoiceControls }) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice chat controls",
                            tint = if (voiceState.isConnected) ArenaGold else Color(0xFF94A3B8)
                        )
                    }

                    // Text Chat Toggle with badge
                    IconButton(onClick = { showChatSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Chat",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ArenaNavy)
            )
        },
        containerColor = ArenaNavy,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Expandable Voice Controls Bar
            if (showVoiceControls) {
                VoiceChatControls(
                    voiceState = voiceState,
                    onToggleMic = { voiceChatManager.toggleMicMute() },
                    onToggleSpeaker = { voiceChatManager.toggleSpeaker() },
                    onPushToTalkChange = { voiceChatManager.setPushToTalk(it) },
                    onPermissionResult = { voiceChatManager.updatePermission(it) },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Top Players Row (Green top-left, Yellow top-right)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val greenPlayer = gameState.players.find { it.color == LudoColor.GREEN }
                val yellowPlayer = gameState.players.find { it.color == LudoColor.YELLOW }

                if (greenPlayer != null) {
                    PlayerCardView(
                        player = greenPlayer,
                        isCurrentTurn = gameState.currentPlayer?.color == LudoColor.GREEN,
                        turnTimerSeconds = gameState.timerSecondsRemaining,
                        isVoiceSpeaking = voiceState.speakingPlayers.contains(greenPlayer.id),
                        onTogglePlayerMute = { /* toggle mute */ },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (yellowPlayer != null) {
                    PlayerCardView(
                        player = yellowPlayer,
                        isCurrentTurn = gameState.currentPlayer?.color == LudoColor.YELLOW,
                        turnTimerSeconds = gameState.timerSecondsRemaining,
                        isVoiceSpeaking = voiceState.speakingPlayers.contains(yellowPlayer.id),
                        onTogglePlayerMute = { /* toggle mute */ },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // The Center Ludo Board View
            LudoBoardView(
                players = gameState.players,
                currentPlayerColor = gameState.currentPlayer?.color,
                movableTokenIds = gameState.movableTokenIds,
                animatedCoordinate = gameState.animatedCoordinate,
                movingTokenId = gameState.movingTokenId,
                onTokenClicked = { tokenId -> gameEngine.onTokenClicked(tokenId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Players Row (Red bottom-left, Blue bottom-right)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val redPlayer = gameState.players.find { it.color == LudoColor.RED }
                val bluePlayer = gameState.players.find { it.color == LudoColor.BLUE }

                if (redPlayer != null) {
                    PlayerCardView(
                        player = redPlayer,
                        isCurrentTurn = gameState.currentPlayer?.color == LudoColor.RED,
                        turnTimerSeconds = gameState.timerSecondsRemaining,
                        isVoiceSpeaking = voiceState.speakingPlayers.contains(redPlayer.id),
                        onTogglePlayerMute = { /* toggle mute */ },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (bluePlayer != null) {
                    PlayerCardView(
                        player = bluePlayer,
                        isCurrentTurn = gameState.currentPlayer?.color == LudoColor.BLUE,
                        turnTimerSeconds = gameState.timerSecondsRemaining,
                        isVoiceSpeaking = voiceState.speakingPlayers.contains(bluePlayer.id),
                        onTogglePlayerMute = { /* toggle mute */ },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Banner Message
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ArenaNavySurface)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = gameState.statusBanner,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dice Action Center
            val currentPlayer = gameState.currentPlayer
            if (currentPlayer != null) {
                DiceComponent(
                    value = gameState.diceValue,
                    isRolling = gameState.isDiceRolling,
                    playerColor = currentPlayer.color,
                    isCurrentPlayerTurn = !currentPlayer.isAi && !gameState.isMatchOver,
                    onRollClick = { gameEngine.rollDice() },
                    modifier = Modifier.testTag("interactive_dice")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // In-Game Chat Sheet
    if (showChatSheet) {
        InGameChatSheet(
            sheetState = chatSheetState,
            messages = lobbyState.chatMessages,
            onDismiss = { showChatSheet = false },
            onSendMessage = { text, isEmoji ->
                val sender = gameState.currentPlayer ?: gameState.players.first()
                multiplayerManager.sendUserChatMessage(sender, text, isEmoji)
            }
        )
    }

    // Victory Celebration Dialog
    if (gameState.isMatchOver && gameState.winners.isNotEmpty()) {
        val localWinner = gameState.winners.firstOrNull()?.isAi == false
        VictoryDialog(
            winners = gameState.winners,
            isLocalPlayerWinner = localWinner,
            coinsWon = 500,
            xpWon = 250,
            onRematchClick = {
                gameEngine.startNewGame(gameState.gameMode, gameState.players, gameState.roomCode)
            },
            onHomeClick = onExitGame
        )
    }

    // Exit Game Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Leave Match?", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Leaving now will forfeit the current game. Are you sure?", color = Color(0xFFCBD5E1)) },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        onExitGame()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Leave Match", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Stay", color = ArenaGold)
                }
            },
            containerColor = ArenaNavyCard
        )
    }
}
