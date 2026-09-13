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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.model.GameSettings
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavySurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var soundFx by remember { mutableStateOf(settings.soundFxEnabled) }
    var music by remember { mutableStateOf(settings.musicEnabled) }
    var vibration by remember { mutableStateOf(settings.vibrationEnabled) }
    var voiceChat by remember { mutableStateOf(settings.voiceChatEnabled) }
    var textChat by remember { mutableStateOf(settings.textChatEnabled) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White, fontWeight = FontWeight.Bold) },
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
            Text("AUDIO & HAPTICS", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))

            SettingSwitchItem("Sound Effects", "Token moves, capture sounds, fanfare", soundFx) {
                soundFx = it
                onSettingsChanged(settings.copy(soundFxEnabled = it))
            }
            Spacer(modifier = Modifier.height(8.dp))

            SettingSwitchItem("Background Music", "Ambient arena orchestral loops", music) {
                music = it
                onSettingsChanged(settings.copy(musicEnabled = it))
            }
            Spacer(modifier = Modifier.height(8.dp))

            SettingSwitchItem("Vibration & Haptics", "Haptic pulse on turn & captures", vibration) {
                vibration = it
                onSettingsChanged(settings.copy(vibrationEnabled = it))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("COMMUNICATION", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))

            SettingSwitchItem("In-Game Voice Chat", "Real-time room voice communications", voiceChat) {
                voiceChat = it
                onSettingsChanged(settings.copy(voiceChatEnabled = it))
            }
            Spacer(modifier = Modifier.height(8.dp))

            SettingSwitchItem("Text Chat & Emojis", "Display reaction drawer in matches", textChat) {
                textChat = it
                onSettingsChanged(settings.copy(textChatEnabled = it))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("GAMEPLAY PREFERENCES", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ArenaNavySurface)
                    .padding(14.dp)
            ) {
                Column {
                    Text("Turn Timer", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Standard 20 seconds per roll", color = Color(0xFF94A3B8), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun SettingSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ArenaNavySurface)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = subtitle, color = Color(0xFF94A3B8), fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF0F172A),
                checkedTrackColor = ArenaGold,
                uncheckedTrackColor = Color(0xFF334155)
            )
        )
    }
}
