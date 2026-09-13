package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.MultiplayerSyncManager
import com.example.model.GameMode
import com.example.model.LudoColor
import com.example.model.Player
import com.example.model.UserProfile
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface
import com.example.ui.theme.ArenaSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineLobbyScreen(
    userProfile: UserProfile,
    multiplayerManager: MultiplayerSyncManager,
    onBackClick: () -> Unit,
    onStartGame: (GameMode, List<Player>, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lobbyState by multiplayerManager.lobbyState.collectAsState()
    var selectedPlayerCount by remember { mutableIntStateOf(4) }
    var enteredRoomCode by remember { mutableStateOf("") }
    var joinError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    val localPlayer = remember(userProfile) {
        Player(
            id = userProfile.id,
            name = userProfile.displayName,
            color = LudoColor.RED,
            isAi = false,
            avatarId = userProfile.avatarId
        )
    }

    LaunchedEffect(lobbyState.isGameReady) {
        if (lobbyState.isGameReady && lobbyState.playersInRoom.isNotEmpty()) {
            val mode = if (lobbyState.roomCode != null) GameMode.ONLINE_PRIVATE_ROOM else GameMode.ONLINE_QUICK_MATCH
            onStartGame(mode, lobbyState.playersInRoom, lobbyState.roomCode ?: "LA-MATCH")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Online Multiplayer", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    // Ping latency indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ArenaNavySurface)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.SignalCellularAlt, contentDescription = "Latency", tint = ArenaSuccess, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${lobbyState.latencyMs}ms", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                .padding(20.dp)
        ) {
            if (lobbyState.isSearchingMatch) {
                // Matchmaking radar active view
                MatchmakingSearchingView(
                    searchSeconds = lobbyState.searchSeconds,
                    playerCount = lobbyState.targetPlayerCount,
                    foundCount = lobbyState.playersInRoom.size,
                    onCancel = { multiplayerManager.cancelSearch() }
                )
            } else {
                // 1. Quick Matchmaking Card
                Text("QUICK MATCH", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(ArenaNavySurface)
                        .border(1.dp, ArenaNavyCard, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ArenaGold.copy(alpha = 0.2f))
                            ) {
                                Icon(Icons.Default.Public, contentDescription = null, tint = ArenaGold, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Automated Matchmaking", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Find online opponents matched by your rating", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Player Count Selector (2 or 4)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            listOf(2, 4).forEach { count ->
                                val isSelected = selectedPlayerCount == count
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) ArenaGold else ArenaNavyCard)
                                        .clickable { selectedPlayerCount = count }
                                        .padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "$count Players",
                                        color = if (isSelected) Color(0xFF0F172A) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                multiplayerManager.startQuickMatch(localPlayer, selectedPlayerCount)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ArenaGold),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_find_match")
                        ) {
                            Text("FIND MATCH NOW", color = Color(0xFF0F172A), fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Private Room (Host or Join)
                Text("PRIVATE ROOM", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(ArenaNavySurface)
                        .border(1.dp, ArenaNavyCard, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        // Create Room
                        Button(
                            onClick = {
                                val code = multiplayerManager.createPrivateRoom(localPlayer, selectedPlayerCount)
                                // simulate friend joining
                                multiplayerManager.joinPrivateRoom(code, localPlayer)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_create_room")
                        ) {
                            Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CREATE PRIVATE ROOM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Join with Room Code
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = enteredRoomCode,
                                onValueChange = {
                                    enteredRoomCode = it.uppercase()
                                    joinError = null
                                },
                                placeholder = { Text("Enter Code (e.g. LA-4821)", color = Color(0xFF94A3B8), fontSize = 12.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = ArenaGold,
                                    unfocusedBorderColor = ArenaNavyCard
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (enteredRoomCode.length >= 4) {
                                        val success = multiplayerManager.joinPrivateRoom(enteredRoomCode, localPlayer)
                                        if (!success) joinError = "Room not found"
                                    } else {
                                        joinError = "Enter valid code"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ArenaGold),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.height(52.dp)
                            ) {
                                Text("JOIN", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                            }
                        }

                        if (joinError != null) {
                            Text(
                                text = joinError!!,
                                color = Color(0xFFEF4444),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchmakingSearchingView(
    searchSeconds: Int,
    playerCount: Int,
    foundCount: Int,
    onCancel: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radar"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(ArenaGold.copy(alpha = 0.2f))
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(ArenaGold)
            ) {
                Icon(
                    imageVector = Icons.Default.Radar,
                    contentDescription = "Searching",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Searching for Arena Rivals...",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Text(
            text = "Players Found: $foundCount / $playerCount • Time: ${searchSeconds}s",
            color = ArenaGold,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("CANCEL SEARCH", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
