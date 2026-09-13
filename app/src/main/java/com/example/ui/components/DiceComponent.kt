package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LudoColor
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.LudoBlue
import com.example.ui.theme.LudoGreen
import com.example.ui.theme.LudoRed
import com.example.ui.theme.LudoYellow

@Composable
fun DiceComponent(
    value: Int,
    isRolling: Boolean,
    playerColor: LudoColor,
    isCurrentPlayerTurn: Boolean,
    onRollClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dice_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCurrentPlayerTurn && !isRolling) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val rollRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isRolling) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(250),
            repeatMode = RepeatMode.Restart
        ),
        label = "roll_rotation"
    )

    val colorTint = when (playerColor) {
        LudoColor.RED -> LudoRed
        LudoColor.GREEN -> LudoGreen
        LudoColor.YELLOW -> LudoYellow
        LudoColor.BLUE -> LudoBlue
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(68.dp)
                .scale(if (isRolling) 0.92f else pulseScale)
                .rotate(rollRotation)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.White, Color(0xFFF1F5F9))
                    )
                )
                .border(
                    width = if (isCurrentPlayerTurn) 3.5.dp else 1.5.dp,
                    color = if (isCurrentPlayerTurn) colorTint else Color(0xFFCBD5E1),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(
                    enabled = isCurrentPlayerTurn && !isRolling,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onRollClick()
                }
                .testTag("dice_button")
        ) {
            DiceFaceDots(value = value, dotColor = colorTint)
        }

        if (isCurrentPlayerTurn && !isRolling) {
            Text(
                text = "TAP TO ROLL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = ArenaGold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun DiceFaceDots(value: Int, dotColor: Color) {
    val dotSize = 10.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(9.dp),
        contentAlignment = Alignment.Center
    ) {
        when (value) {
            1 -> {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
            2 -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Dot(dotSize, dotColor)
                        Box(modifier = Modifier.size(dotSize))
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.align(Alignment.TopStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) { Dot(dotSize, dotColor) }
                }
            }
            3 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.align(Alignment.TopStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.Center)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) { Dot(dotSize, dotColor) }
                }
            }
            4 -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize(0.5f)) {
                            Dot(dotSize, dotColor)
                            Dot(dotSize, dotColor)
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.align(Alignment.TopStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.TopEnd)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) { Dot(dotSize, dotColor) }
                }
            }
            5 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.align(Alignment.TopStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.TopEnd)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.Center)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) { Dot(dotSize, dotColor) }
                }
            }
            6 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.align(Alignment.TopStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.CenterStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomStart)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.TopEnd)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) { Dot(dotSize, dotColor) }
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) { Dot(dotSize, dotColor) }
                }
            }
        }
    }
}

@Composable
private fun Dot(size: androidx.compose.ui.unit.Dp, color: Color) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
