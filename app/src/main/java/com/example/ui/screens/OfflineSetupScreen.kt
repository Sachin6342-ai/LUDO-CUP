package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.AiDifficulty
import com.example.model.GameMode
import com.example.model.LudoColor
import com.example.model.Player
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
fun OfflineSetupScreen(
    onBackClick: () -> Unit,
    onStartGame: (GameMode, List<Player>) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVsAi by remember { mutableStateOf(true) }
    var playerCount by remember { mutableIntStateOf(4) }
    var aiDifficulty by remember { mutableStateOf(AiDifficulty.MEDIUM) }
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Computer A") }
    var player3Name by remember { mutableStateOf("Computer B") }
    var player4Name by remember { mutableStateOf("Computer C") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Offline Match Setup", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
            // Mode Select (Vs AI / Pass & Play)
            Text("SELECT MATCH TYPE", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isVsAi) ArenaGold else ArenaNavySurface)
                        .border(1.dp, if (isVsAi) ArenaGold else ArenaNavyCard, RoundedCornerShape(16.dp))
                        .clickable {
                            isVsAi = true
                            player2Name = "Computer A"
                            player3Name = "Computer B"
                            player4Name = "Computer C"
                        }
                        .padding(vertical = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = if (isVsAi) Color(0xFF0F172A) else Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Vs Computer", color = if (isVsAi) Color(0xFF0F172A) else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (!isVsAi) ArenaGold else ArenaNavySurface)
                        .border(1.dp, if (!isVsAi) ArenaGold else ArenaNavyCard, RoundedCornerShape(16.dp))
                        .clickable {
                            isVsAi = false
                            player2Name = "Player 2"
                            player3Name = "Player 3"
                            player4Name = "Player 4"
                        }
                        .padding(vertical = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = if (!isVsAi) Color(0xFF0F172A) else Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pass & Play", color = if (!isVsAi) Color(0xFF0F172A) else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Player Count (2, 3, 4)
            Text("NUMBER OF PLAYERS", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(2, 3, 4).forEach { count ->
                    val isSelected = playerCount == count
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) ArenaGold else ArenaNavySurface)
                            .border(1.dp, if (isSelected) ArenaGold else ArenaNavyCard, RoundedCornerShape(14.dp))
                            .clickable { playerCount = count }
                            .padding(vertical = 12.dp)
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

            // AI Difficulty (only if Vs AI)
            if (isVsAi) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("AI BOT DIFFICULTY", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    AiDifficulty.entries.forEach { diff ->
                        val isSelected = aiDifficulty == diff
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ArenaGold else ArenaNavySurface)
                                .border(1.dp, if (isSelected) ArenaGold else ArenaNavyCard, RoundedCornerShape(12.dp))
                                .clickable { aiDifficulty = diff }
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = diff.label,
                                color = if (isSelected) Color(0xFF0F172A) else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Player Slots
            Text("PLAYER SEATS", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(10.dp))

            PlayerSeatConfigRow(color = LudoRed, name = player1Name, isAi = false, onNameChange = { player1Name = it })
            Spacer(modifier = Modifier.height(8.dp))
            PlayerSeatConfigRow(color = LudoGreen, name = player2Name, isAi = isVsAi, onNameChange = { player2Name = it })

            if (playerCount >= 3) {
                Spacer(modifier = Modifier.height(8.dp))
                PlayerSeatConfigRow(color = LudoYellow, name = player3Name, isAi = isVsAi, onNameChange = { player3Name = it })
            }

            if (playerCount == 4) {
                Spacer(modifier = Modifier.height(8.dp))
                PlayerSeatConfigRow(color = LudoBlue, name = player4Name, isAi = isVsAi, onNameChange = { player4Name = it })
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Start Match Button
            Button(
                onClick = {
                    val colors = listOf(LudoColor.RED, LudoColor.GREEN, LudoColor.YELLOW, LudoColor.BLUE)
                    val names = listOf(player1Name, player2Name, player3Name, player4Name)
                    val players = (0 until playerCount).map { index ->
                        Player(
                            id = "p_$index",
                            name = names[index].ifBlank { "Player ${index + 1}" },
                            color = colors[index],
                            isAi = if (index == 0) false else isVsAi,
                            aiDifficulty = aiDifficulty,
                            avatarId = index
                        )
                    }
                    val mode = if (isVsAi) GameMode.OFFLINE_VS_AI else GameMode.OFFLINE_PASS_AND_PLAY
                    onStartGame(mode, players)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ArenaGold),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_start_match")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF0F172A), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("START ARENA MATCH", color = Color(0xFF0F172A), fontWeight = FontWeight.Black, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun PlayerSeatConfigRow(
    color: Color,
    name: String,
    isAi: Boolean,
    onNameChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ArenaNavySurface)
            .border(1.dp, ArenaNavyCard, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = ArenaGold,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f)
        )
        if (isAi) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ArenaNavyCard)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("BOT", color = ArenaGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
