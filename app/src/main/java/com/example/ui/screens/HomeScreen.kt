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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaGoldDark
import com.example.ui.theme.ArenaGoldLight
import com.example.ui.theme.ArenaNavy
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface
import com.example.ui.theme.ArenaSuccess
import com.example.ui.theme.LudoBlue
import com.example.ui.theme.LudoGreen
import com.example.ui.theme.LudoRed
import com.example.ui.theme.LudoYellow

@Composable
fun HomeScreen(
    profile: UserProfile,
    onNavigateToOnline: () -> Unit,
    onNavigateToOffline: () -> Unit,
    onNavigateToTournament: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToCustomization: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToPolicy: () -> Unit,
    onOpenDailyRewards: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(ArenaNavy, Color(0xFF020617))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Top Bar: Profile & Balance
            TopProfileBar(
                profile = profile,
                onProfileClick = onNavigateToProfile,
                onRewardsClick = onOpenDailyRewards
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Hero Arena Banner
            HeroArenaBanner(onOpenDailyRewards = onOpenDailyRewards)

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Game Mode Cards
            Text(
                text = "BATTLE MODES",
                color = ArenaGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // 1. Play Online Multiplayer
            MainGameModeCard(
                title = "Online Multiplayer",
                subtitle = "2-4 Players • Quick Match • Private Rooms",
                icon = Icons.Default.Public,
                gradient = listOf(Color(0xFF1D4ED8), Color(0xFF2563EB)),
                accentColor = Color(0xFF93C5FD),
                onClick = onNavigateToOnline,
                testTag = "btn_play_online"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Play Offline Ludo
            MainGameModeCard(
                title = "Offline Play",
                subtitle = "Pass & Play • Vs AI Computer (4 Difficulties)",
                icon = Icons.Default.SmartToy,
                gradient = listOf(Color(0xFF047857), Color(0xFF10B981)),
                accentColor = Color(0xFFA7F3D0),
                onClick = onNavigateToOffline,
                testTag = "btn_play_offline"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Tournament Championship
            MainGameModeCard(
                title = "Tournament Cup",
                subtitle = "Brackets • Quarterfinals to Champion • Big Rewards",
                icon = Icons.Default.EmojiEvents,
                gradient = listOf(Color(0xFFB45309), Color(0xFFF59E0B)),
                accentColor = Color(0xFFFEF3C7),
                onClick = onNavigateToTournament,
                testTag = "btn_tournament"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Hub Grid (Leaderboard, Achievements, Customization, Settings, Admin)
            Text(
                text = "ARENA HUB",
                color = ArenaGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HubActionTile(
                    title = "Rankings",
                    icon = Icons.Default.Leaderboard,
                    iconColor = ArenaGold,
                    onClick = onNavigateToLeaderboard,
                    modifier = Modifier.weight(1f)
                )
                HubActionTile(
                    title = "Badges",
                    icon = Icons.Default.WorkspacePremium,
                    iconColor = Color(0xFF38BDF8),
                    onClick = onNavigateToAchievements,
                    modifier = Modifier.weight(1f)
                )
                HubActionTile(
                    title = "Themes",
                    icon = Icons.Default.Palette,
                    iconColor = Color(0xFFEC4899),
                    onClick = onNavigateToCustomization,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HubActionTile(
                    title = "Settings",
                    icon = Icons.Default.Settings,
                    iconColor = Color(0xFFCBD5E1),
                    onClick = onNavigateToSettings,
                    modifier = Modifier.weight(1f)
                )
                HubActionTile(
                    title = "Admin",
                    icon = Icons.Default.AdminPanelSettings,
                    iconColor = Color(0xFFA855F7),
                    onClick = onNavigateToAdmin,
                    modifier = Modifier.weight(1f)
                )
                HubActionTile(
                    title = "Rules & Legal",
                    icon = Icons.Default.Policy,
                    iconColor = ArenaSuccess,
                    onClick = onNavigateToPolicy,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun TopProfileBar(
    profile: UserProfile,
    onProfileClick: () -> Unit,
    onRewardsClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // User Profile Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(ArenaNavySurface)
                .border(1.dp, ArenaNavyCard, RoundedCornerShape(20.dp))
                .clickable { onProfileClick() }
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ArenaGold)
            ) {
                Text(
                    text = profile.displayName.take(1).uppercase(),
                    color = Color(0xFF1E1B4B),
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = profile.displayName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Lvl ${profile.level} • ${profile.trophies} 🏆",
                    color = ArenaGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Coins & Daily Reward badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Daily Reward Pill
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEF4444).copy(alpha = 0.2f))
                    .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(16.dp))
                    .clickable { onRewardsClick() }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Daily Rewards",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Claim",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Coins pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(ArenaNavySurface)
                    .border(1.dp, ArenaGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Coins",
                    tint = ArenaGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${profile.coins}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun HeroArenaBanner(onOpenDailyRewards: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF1E293B))
                )
            )
            .border(1.5.dp, ArenaGold.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "LUDO CUP",
                        color = ArenaGold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Season 1 Championship Live!",
                        color = Color(0xFFE2E8F0),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // 4 mini token colored dots preview
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(LudoRed, LudoGreen, LudoYellow, LudoBlue).forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(c)
                                .border(1.dp, Color.White, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Real-time voice rooms • Zero unfair dice • 100% Original gameplay",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun MainGameModeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(gradient))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(18.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = accentColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun HubActionTile(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(ArenaNavySurface)
            .border(1.dp, ArenaNavyCard, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = Color(0xFFF1F5F9),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
