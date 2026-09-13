package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavySurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalPolicyScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fair Play & Legal Policies", color = Color.White, fontWeight = FontWeight.Bold) },
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
            PolicySection(
                title = "Originality & Copyright Notice",
                body = "Ludo Cup is built from scratch with original code, graphics, vectors, color palettes, and audio synthesizers. All assets and user interface components are unique and do not copy or replicate proprietary designs or trademarks of other games."
            )

            Spacer(modifier = Modifier.height(16.dp))

            PolicySection(
                title = "Fair Play Community Standards",
                body = "All dice rolls are generated using cryptographically uniform pseudo-random number generators without bias or manipulation. Game matches are server-synchronized and protected against tampering. Collusion, abusive voice/text chat, and griefing are strictly prohibited."
            )

            Spacer(modifier = Modifier.height(16.dp))

            PolicySection(
                title = "Privacy Policy & Data Security",
                body = "Ludo Cup only collects essential local profile progress and temporary room signaling data for voice and text chat. Microphone access is solely used during active voice sessions and is never recorded or stored on remote servers."
            )

            Spacer(modifier = Modifier.height(16.dp))

            PolicySection(
                title = "Terms of Service",
                body = "Virtual currency (Coins, Trophies, XP) has no real-world monetary value and is intended solely for in-game entertainment and progression. Players may reset their local data at any time."
            )
        }
    }
}

@Composable
private fun PolicySection(title: String, body: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(ArenaNavySurface)
            .padding(16.dp)
    ) {
        Text(text = title, color = ArenaGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = body, color = Color(0xFFE2E8F0), fontSize = 12.sp, lineHeight = 18.sp)
    }
}
