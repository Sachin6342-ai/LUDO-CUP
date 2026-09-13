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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface
import com.example.ui.theme.ArenaSuccess
import com.example.ui.theme.LudoBlue
import com.example.ui.theme.LudoGreen
import com.example.ui.theme.LudoRed
import com.example.ui.theme.LudoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onSaveProfile: (UserProfile) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var displayNameInput by remember { mutableStateOf(profile.displayName) }
    var selectedAvatar by remember { mutableStateOf(profile.avatarId) }

    val scrollState = rememberScrollState()

    val avatarPalette = listOf(
        LudoRed, LudoGreen, LudoYellow, LudoBlue, ArenaGold, Color(0xFFA855F7)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Profile", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = ArenaGold)
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with Level Ring
            Box(contentAlignment = Alignment.Center) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(avatarPalette.getOrElse(selectedAvatar % avatarPalette.size) { ArenaGold })
                        .border(3.dp, ArenaGold, CircleShape)
                ) {
                    Text(
                        text = displayNameInput.take(1).uppercase(),
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.Black,
                        fontSize = 36.sp
                    )
                }

                // Level Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, ArenaGold, RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "LVL ${profile.level}",
                        color = ArenaGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = displayNameInput,
                    onValueChange = { displayNameInput = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = ArenaGold
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Choose Avatar color
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    avatarPalette.forEachIndexed { idx, color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    if (selectedAvatar == idx) 2.5.dp else 1.dp,
                                    if (selectedAvatar == idx) Color.White else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { selectedAvatar = idx }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        onSaveProfile(
                            profile.copy(
                                displayName = displayNameInput.ifBlank { "Champion" },
                                avatarId = selectedAvatar
                            )
                        )
                        isEditing = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ArenaGold)
                ) {
                    Text("SAVE CHANGES", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                }
            } else {
                Text(
                    text = profile.displayName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "ID: #${profile.playerId}",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // XP Progress
            val xpRatio = (profile.xp % 1000).toFloat() / 1000f
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("XP to Level ${profile.level + 1}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Text("${profile.xp % 1000} / 1000", color = ArenaGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { xpRatio },
                    color = ArenaGold,
                    trackColor = ArenaNavyCard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Career Stats Grid
            Text(
                text = "CAREER STATISTICS",
                color = ArenaGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard("Matches", "${profile.matchesPlayed}", modifier = Modifier.weight(1f))
                StatCard("Victories", "${profile.wins}", tint = ArenaSuccess, modifier = Modifier.weight(1f))
                StatCard("Win Rate", "${profile.winRate.toInt()}%", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard("Current Streak", "${profile.currentStreak} 🔥", modifier = Modifier.weight(1f))
                StatCard("Best Streak", "${profile.bestStreak}", modifier = Modifier.weight(1f))
                StatCard("Trophies", "${profile.trophies} 🏆", tint = ArenaGold, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Activity
            Text(
                text = "RECENT MATCHES",
                color = ArenaGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(10.dp))

            RecentMatchItem("Online 4-Player", "Victory (1st)", "+250 Coins", true)
            Spacer(modifier = Modifier.height(8.dp))
            RecentMatchItem("Tournament Semi-Finals", "Victory (1st)", "+400 Coins", true)
            Spacer(modifier = Modifier.height(8.dp))
            RecentMatchItem("Vs AI (Expert)", "2nd Place", "+50 Coins", false)
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, tint: Color = Color.White, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(ArenaNavySurface)
            .border(1.dp, ArenaNavyCard, RoundedCornerShape(14.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, color = tint, fontWeight = FontWeight.Black, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun RecentMatchItem(mode: String, result: String, reward: String, isWin: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ArenaNavySurface)
            .border(1.dp, ArenaNavyCard, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = if (isWin) ArenaSuccess else Color(0xFF94A3B8),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = mode, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = result, color = if (isWin) ArenaSuccess else Color(0xFFCBD5E1), fontSize = 11.sp)
            }
        }
        Text(text = reward, color = ArenaGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}
