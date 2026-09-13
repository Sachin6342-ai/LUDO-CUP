package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LudoColor
import com.example.model.Player
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface
import com.example.ui.theme.ArenaSuccess
import com.example.ui.theme.LudoBlue
import com.example.ui.theme.LudoGreen
import com.example.ui.theme.LudoRed
import com.example.ui.theme.LudoYellow

@Composable
fun PlayerCardView(
    player: Player,
    isCurrentTurn: Boolean,
    turnTimerSeconds: Int,
    isVoiceSpeaking: Boolean,
    onTogglePlayerMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeColor = when (player.color) {
        LudoColor.RED -> LudoRed
        LudoColor.GREEN -> LudoGreen
        LudoColor.YELLOW -> LudoYellow
        LudoColor.BLUE -> LudoBlue
    }

    val infiniteTransition = rememberInfiniteTransition(label = "player_turn_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val voiceWaveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "voice_wave"
    )

    Box(
        modifier = modifier
            .shadow(if (isCurrentTurn) 10.dp else 4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isCurrentTurn) {
                    Brush.verticalGradient(
                        listOf(
                            ArenaNavySurface,
                            ArenaNavyCard
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        listOf(
                            ArenaNavyCard.copy(alpha = 0.7f),
                            ArenaNavySurface.copy(alpha = 0.7f)
                        )
                    )
                }
            )
            .border(
                width = if (isCurrentTurn) 2.5.dp else 1.dp,
                color = if (isCurrentTurn) themeColor.copy(alpha = glowAlpha) else Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar with color border & Voice Wave
                Box(contentAlignment = Alignment.Center) {
                    if (isVoiceSpeaking) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .scale(voiceWaveScale)
                                .clip(CircleShape)
                                .background(ArenaSuccess.copy(alpha = 0.35f))
                        )
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(themeColor)
                            .border(2.dp, Color.White, CircleShape)
                    ) {
                        Text(
                            text = player.name.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Name & Mode
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = player.name,
                            color = Color.White,
                            fontWeight = if (isCurrentTurn) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (player.isAi) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "AI Bot",
                                tint = ArenaGold,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Tokens Home tracker
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        repeat(4) { idx ->
                            val isFinished = idx < player.finishedTokensCount
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isFinished) ArenaGold else themeColor.copy(alpha = 0.35f)
                                    )
                            )
                        }
                    }
                }

                // Voice Mute button for player
                if (!player.isAi) {
                    IconButton(
                        onClick = onTogglePlayerMute,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (player.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "Mute player",
                            tint = if (player.isMuted) Color(0xFFEF4444) else Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Turn Countdown progress bar
            if (isCurrentTurn) {
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { (turnTimerSeconds / 20f).coerceIn(0f, 1f) },
                    color = if (turnTimerSeconds <= 5) Color(0xFFEF4444) else ArenaGold,
                    trackColor = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }
        }
    }
}
