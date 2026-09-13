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
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizationScreen(
    profile: UserProfile,
    onSaveCustomization: (UserProfile) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTheme by remember { mutableStateOf(profile.selectedBoardTheme) }
    var selectedDice by remember { mutableStateOf(profile.selectedDiceSkin) }
    var selectedToken by remember { mutableStateOf(profile.selectedTokenSkin) }

    val scrollState = rememberScrollState()

    val boardThemes = listOf(
        Pair("ROYAL_CLASSIC", "Royal Classic (Default)"),
        Pair("NEON_CYBER", "Neon Cyberpunk"),
        Pair("WOOD_MARBLE", "Wood & Marble Luxury"),
        Pair("CANDY_ARENA", "Candy Pop Arena")
    )

    val diceSkins = listOf(
        Pair("GOLDEN_ROYAL", "Golden Royal (Equipped)"),
        Pair("CLASSIC_IVORY", "Classic Ivory"),
        Pair("RUBY_SPARK", "Ruby Spark"),
        Pair("EMERALD_GLOW", "Emerald Glow")
    )

    val tokenSkins = listOf(
        Pair("ROYAL_CROWN", "Royal Crown"),
        Pair("CLASSIC_PAWN", "Classic Pawn"),
        Pair("GEM_STONE", "Gem Stone"),
        Pair("STAR_BADGE", "Star Badge")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Arena Wardrobe", color = Color.White, fontWeight = FontWeight.Bold) },
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
            // Board Themes Section
            Text("BOARD THEME", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))
            boardThemes.forEach { (id, name) ->
                val isSelected = selectedTheme == id
                SkinSelectionItem(
                    name = name,
                    isSelected = isSelected,
                    onClick = {
                        selectedTheme = id
                        onSaveCustomization(profile.copy(selectedBoardTheme = id))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dice Skins Section
            Text("DICE SKINS", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))
            diceSkins.forEach { (id, name) ->
                val isSelected = selectedDice == id
                SkinSelectionItem(
                    name = name,
                    isSelected = isSelected,
                    onClick = {
                        selectedDice = id
                        onSaveCustomization(profile.copy(selectedDiceSkin = id))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Token Skins Section
            Text("TOKEN SKINS", color = ArenaGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))
            tokenSkins.forEach { (id, name) ->
                val isSelected = selectedToken == id
                SkinSelectionItem(
                    name = name,
                    isSelected = isSelected,
                    onClick = {
                        selectedToken = id
                        onSaveCustomization(profile.copy(selectedTokenSkin = id))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SkinSelectionItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ArenaNavySurface)
            .border(
                1.dp,
                if (isSelected) ArenaGold else ArenaNavyCard,
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = name,
            color = Color.White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp
        )

        if (isSelected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(ArenaGold)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Equipped",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
