package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameMode
import com.example.model.LudoColor
import com.example.model.Player
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface
import com.example.ui.theme.LudoRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentScreen(
    onBackClick: () -> Unit,
    onStartTournamentMatch: (GameMode, List<Player>) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournament Championship", color = Color.White, fontWeight = FontWeight.Bold) },
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
            // Trophy Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF78350F), Color(0xFFB45309), Color(0xFFD97706))
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(ArenaGold)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "GRAND ARENA CUP",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Prize Pool: 5,000 Coins + 500 Trophies",
                            color = ArenaGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bracket Tree
            Text(
                text = "BRACKET PROGRESSION",
                color = ArenaGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            BracketMatchCard(
                stageTitle = "Quarter-Finals (Round 1)",
                player1 = "You (LudoChampion)",
                player2 = "Rajesh_Pro",
                isCompleted = true,
                winner = "You"
            )

            Spacer(modifier = Modifier.height(10.dp))

            BracketMatchCard(
                stageTitle = "Semi-Finals (Round 2)",
                player1 = "You (LudoChampion)",
                player2 = "Vikram_Ace",
                isCompleted = true,
                winner = "You"
            )

            Spacer(modifier = Modifier.height(10.dp))

            BracketMatchCard(
                stageTitle = "Grand Finals (Championship)",
                player1 = "You (LudoChampion)",
                player2 = "King_David (AI Expert)",
                isCompleted = false,
                winner = "Upcoming Match"
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    val p1 = Player("p_champ", "You", LudoColor.RED, false)
                    val p2 = Player("p_opp", "King_David", LudoColor.GREEN, true)
                    onStartTournamentMatch(GameMode.TOURNAMENT, listOf(p1, p2))
                },
                colors = ButtonDefaults.buttonColors(containerColor = ArenaGold),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_play_finals")
            ) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFF0F172A))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ENTER GRAND FINALS MATCH", color = Color(0xFF0F172A), fontWeight = FontWeight.Black, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun BracketMatchCard(
    stageTitle: String,
    player1: String,
    player2: String,
    isCompleted: Boolean,
    winner: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ArenaNavySurface)
            .border(1.dp, if (isCompleted) ArenaGold.copy(alpha = 0.5f) else ArenaNavyCard, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stageTitle, color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = if (isCompleted) "PASSED ✓" else "READY",
                    color = if (isCompleted) Color(0xFF10B981) else Color(0xFF38BDF8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "• $player1", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = "• $player2", color = Color(0xFFCBD5E1), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Status: $winner",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
            )
        }
    }
}
