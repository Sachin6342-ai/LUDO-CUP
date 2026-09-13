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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaderboardEntry
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Global", "Weekly", "Friends")

    val sampleLeaderboard = remember {
        listOf(
            LeaderboardEntry(1, "LA-0012", "Aarav_Master", 1, 3840, 142, 78.5f),
            LeaderboardEntry(2, "LA-0045", "Elena_R", 2, 3420, 128, 74.0f),
            LeaderboardEntry(3, "LA-0089", "Rajesh_Pro", 3, 3110, 115, 69.8f),
            LeaderboardEntry(4, "LA-0112", "LudoChampion (You)", 0, 2940, 94, 75.0f),
            LeaderboardEntry(5, "LA-0241", "Maya_Queen", 4, 2810, 88, 66.2f),
            LeaderboardEntry(6, "LA-0399", "Vikram_Ace", 5, 2650, 79, 63.5f),
            LeaderboardEntry(7, "LA-0412", "Sarah_Sky", 6, 2430, 72, 61.0f)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hall of Champions", color = Color.White, fontWeight = FontWeight.Bold) },
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
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = ArenaNavySurface,
                contentColor = ArenaGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = ArenaGold
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleLeaderboard) { entry ->
                    LeaderboardRowItem(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRowItem(entry: LeaderboardEntry) {
    val isTop3 = entry.rank <= 3
    val rankBadgeColor = when (entry.rank) {
        1 -> ArenaGold
        2 -> Color(0xFFCBD5E1)
        3 -> Color(0xFFB45309)
        else -> Color(0xFF64748B)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ArenaNavySurface)
            .border(
                width = if (entry.displayName.contains("You")) 1.5.dp else 1.dp,
                color = if (entry.displayName.contains("You")) ArenaGold else ArenaNavyCard,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        // Rank Badge
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(rankBadgeColor.copy(alpha = 0.2f))
        ) {
            if (isTop3) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = rankBadgeColor,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Text(
                    text = "#${entry.rank}",
                    color = rankBadgeColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Avatar
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFF334155))
        ) {
            Text(
                text = entry.displayName.take(1).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.displayName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "${entry.wins} Wins • ${entry.winRate}% Win Rate",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
            )
        }

        // Trophies
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${entry.trophies}",
                color = ArenaGold,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "🏆", fontSize = 12.sp)
        }
    }
}
