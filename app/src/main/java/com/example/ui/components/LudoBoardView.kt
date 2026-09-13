package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.BoardCoordinate
import com.example.model.LudoBoardLayout
import com.example.model.LudoColor
import com.example.model.Player
import com.example.model.Token
import com.example.ui.theme.ArenaBoardBg
import com.example.ui.theme.ArenaBoardBorder
import com.example.ui.theme.ArenaBoardTrack
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.LudoBlue
import com.example.ui.theme.LudoBlueDark
import com.example.ui.theme.LudoBlueLight
import com.example.ui.theme.LudoGreen
import com.example.ui.theme.LudoGreenDark
import com.example.ui.theme.LudoGreenLight
import com.example.ui.theme.LudoRed
import com.example.ui.theme.LudoRedDark
import com.example.ui.theme.LudoRedLight
import com.example.ui.theme.LudoYellow
import com.example.ui.theme.LudoYellowDark
import com.example.ui.theme.LudoYellowLight

@Composable
fun LudoBoardView(
    players: List<Player>,
    currentPlayerColor: LudoColor?,
    movableTokenIds: Set<Int>,
    animatedCoordinate: BoardCoordinate?,
    movingTokenId: Int?,
    onTokenClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "token_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "token_scale"
    )

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(ArenaBoardBg)
            .border(4.dp, ArenaNavyCard, RoundedCornerShape(20.dp))
            .testTag("ludo_board")
    ) {
        val boardWidth = maxWidth
        val cellSize = boardWidth / 15f

        // 1. Draw static grid and color tracks
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellPx = size.width / 15f

            drawBoardBases(cellPx)
            drawTracksAndHomePaths(cellPx)
            drawCenterTriangleHome(cellPx)
            drawGridLines(cellPx)
            drawSafeStars(cellPx)
        }

        // 2. Overlay Tokens
        players.forEach { player ->
            val isCurrentPlayer = player.color == currentPlayerColor

            player.tokens.forEach { token ->
                val isMovable = isCurrentPlayer && movableTokenIds.contains(token.id)

                val coord: BoardCoordinate = if (token.id == movingTokenId && animatedCoordinate != null) {
                    animatedCoordinate
                } else {
                    LudoBoardLayout.getCoordinateForToken(player.color, token.id, token.stepCount)
                }

                // Token display size is roughly 80% of cell size
                val tokenSize = cellSize * 0.88f
                val leftOffset = cellSize * coord.col + (cellSize - tokenSize) / 2f
                val topOffset = cellSize * coord.row + (cellSize - tokenSize) / 2f

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(x = leftOffset, y = topOffset)
                        .size(tokenSize)
                        .scale(if (isMovable) pulseScale else 1f)
                        .clickable(
                            enabled = isMovable,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onTokenClicked(token.id)
                        }
                        .testTag("token_${player.color.name}_${token.id}")
                ) {
                    LudoTokenPawn(
                        color = player.color,
                        isPulsing = isMovable
                    )
                }
            }
        }
    }
}

@Composable
private fun LudoTokenPawn(
    color: LudoColor,
    isPulsing: Boolean
) {
    val primaryColor = when (color) {
        LudoColor.RED -> LudoRed
        LudoColor.GREEN -> LudoGreen
        LudoColor.YELLOW -> LudoYellow
        LudoColor.BLUE -> LudoBlue
    }

    val darkColor = when (color) {
        LudoColor.RED -> LudoRedDark
        LudoColor.GREEN -> LudoGreenDark
        LudoColor.YELLOW -> LudoYellowDark
        LudoColor.BLUE -> LudoBlueDark
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
            .shadow(if (isPulsing) 8.dp else 4.dp, CircleShape)
            .clip(CircleShape)
            .background(primaryColor)
            .border(
                width = if (isPulsing) 2.5.dp else 1.5.dp,
                color = if (isPulsing) ArenaGold else Color.White,
                shape = CircleShape
            )
    ) {
        // Inner jewel gradient circle
        Box(
            modifier = Modifier
                .fillMaxSize(0.65f)
                .clip(CircleShape)
                .background(darkColor)
                .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
        )
        // Center white shine dot
        Box(
            modifier = Modifier
                .fillMaxSize(0.25f)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

private fun DrawScope.drawBoardBases(cellPx: Float) {
    // Top-Left: Green Base (6x6)
    drawRect(
        color = LudoGreen,
        topLeft = Offset(0f, 0f),
        size = Size(cellPx * 6, cellPx * 6)
    )
    drawInnerBasePocket(0f, 0f, cellPx, LudoGreenLight)

    // Top-Right: Yellow Base (6x6)
    drawRect(
        color = LudoYellow,
        topLeft = Offset(cellPx * 9, 0f),
        size = Size(cellPx * 6, cellPx * 6)
    )
    drawInnerBasePocket(cellPx * 9, 0f, cellPx, LudoYellowLight)

    // Bottom-Left: Red Base (6x6)
    drawRect(
        color = LudoRed,
        topLeft = Offset(0f, cellPx * 9),
        size = Size(cellPx * 6, cellPx * 6)
    )
    drawInnerBasePocket(0f, cellPx * 9, cellPx, LudoRedLight)

    // Bottom-Right: Blue Base (6x6)
    drawRect(
        color = LudoBlue,
        topLeft = Offset(cellPx * 9, cellPx * 9),
        size = Size(cellPx * 6, cellPx * 6)
    )
    drawInnerBasePocket(cellPx * 9, cellPx * 9, cellPx, LudoBlueLight)
}

private fun DrawScope.drawInnerBasePocket(left: Float, top: Float, cellPx: Float, tint: Color) {
    val padding = cellPx * 0.8f
    val innerSize = cellPx * 4.4f
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(left + padding, top + padding),
        size = Size(innerSize, innerSize),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
    )

    // 4 circular token slots
    val slotRadius = cellPx * 0.65f
    val center1 = Offset(left + cellPx * 2f, top + cellPx * 2f)
    val center2 = Offset(left + cellPx * 4f, top + cellPx * 2f)
    val center3 = Offset(left + cellPx * 2f, top + cellPx * 4f)
    val center4 = Offset(left + cellPx * 4f, top + cellPx * 4f)

    listOf(center1, center2, center3, center4).forEach { center ->
        drawCircle(color = tint, radius = slotRadius, center = center)
        drawCircle(color = ArenaBoardBorder, radius = slotRadius, center = center, style = Stroke(width = 1.5f))
    }
}

private fun DrawScope.drawTracksAndHomePaths(cellPx: Float) {
    // Red Start Cell (row 6, col 1)
    drawRect(color = LudoRed, topLeft = Offset(cellPx * 1, cellPx * 6), size = Size(cellPx, cellPx))
    // Red Home Path (row 7, col 1..5)
    for (c in 1..5) {
        drawRect(color = LudoRed, topLeft = Offset(cellPx * c, cellPx * 7), size = Size(cellPx, cellPx))
    }

    // Green Start Cell (row 1, col 8)
    drawRect(color = LudoGreen, topLeft = Offset(cellPx * 8, cellPx * 1), size = Size(cellPx, cellPx))
    // Green Home Path (row 1..5, col 7)
    for (r in 1..5) {
        drawRect(color = LudoGreen, topLeft = Offset(cellPx * 7, cellPx * r), size = Size(cellPx, cellPx))
    }

    // Yellow Start Cell (row 8, col 13)
    drawRect(color = LudoYellow, topLeft = Offset(cellPx * 13, cellPx * 8), size = Size(cellPx, cellPx))
    // Yellow Home Path (row 7, col 9..13)
    for (c in 9..13) {
        drawRect(color = LudoYellow, topLeft = Offset(cellPx * c, cellPx * 7), size = Size(cellPx, cellPx))
    }

    // Blue Start Cell (row 13, col 6)
    drawRect(color = LudoBlue, topLeft = Offset(cellPx * 6, cellPx * 13), size = Size(cellPx, cellPx))
    // Blue Home Path (row 9..13, col 7)
    for (r in 9..13) {
        drawRect(color = LudoBlue, topLeft = Offset(cellPx * 7, cellPx * r), size = Size(cellPx, cellPx))
    }
}

private fun DrawScope.drawCenterTriangleHome(cellPx: Float) {
    val centerLeft = cellPx * 6
    val centerTop = cellPx * 6
    val centerRight = cellPx * 9
    val centerBottom = cellPx * 9
    val midX = cellPx * 7.5f
    val midY = cellPx * 7.5f

    // Red Left Triangle
    val redPath = Path().apply {
        moveTo(centerLeft, centerTop)
        lineTo(midX, midY)
        lineTo(centerLeft, centerBottom)
        close()
    }
    drawPath(redPath, color = LudoRed)

    // Green Top Triangle
    val greenPath = Path().apply {
        moveTo(centerLeft, centerTop)
        lineTo(midX, midY)
        lineTo(centerRight, centerTop)
        close()
    }
    drawPath(greenPath, color = LudoGreen)

    // Yellow Right Triangle
    val yellowPath = Path().apply {
        moveTo(centerRight, centerTop)
        lineTo(midX, midY)
        lineTo(centerRight, centerBottom)
        close()
    }
    drawPath(yellowPath, color = LudoYellow)

    // Blue Bottom Triangle
    val bluePath = Path().apply {
        moveTo(centerLeft, centerBottom)
        lineTo(midX, midY)
        lineTo(centerRight, centerBottom)
        close()
    }
    drawPath(bluePath, color = LudoBlue)

    // Center Gold Crown Emblem
    drawCircle(color = ArenaGold, radius = cellPx * 0.65f, center = Offset(midX, midY))
    drawCircle(color = Color.White, radius = cellPx * 0.65f, center = Offset(midX, midY), style = Stroke(width = 2f))
}

private fun DrawScope.drawGridLines(cellPx: Float) {
    val stroke = 1.2f
    for (i in 0..15) {
        // Vertical
        drawLine(
            color = ArenaBoardBorder.copy(alpha = 0.7f),
            start = Offset(cellPx * i, 0f),
            end = Offset(cellPx * i, size.height),
            strokeWidth = stroke
        )
        // Horizontal
        drawLine(
            color = ArenaBoardBorder.copy(alpha = 0.7f),
            start = Offset(0f, cellPx * i),
            end = Offset(size.width, cellPx * i),
            strokeWidth = stroke
        )
    }
}

private fun DrawScope.drawSafeStars(cellPx: Float) {
    // 8 Safe cells: 4 Starts + 4 Stars
    val starCoords = listOf(
        Pair(6, 1),   // Red start
        Pair(8, 2),   // Red star
        Pair(1, 8),   // Green start
        Pair(2, 6),   // Green star
        Pair(8, 13),  // Yellow start
        Pair(6, 12),  // Yellow star
        Pair(13, 6),  // Blue start
        Pair(12, 8)   // Blue star
    )

    starCoords.forEach { (row, col) ->
        val centerX = cellPx * col + cellPx / 2f
        val centerY = cellPx * row + cellPx / 2f
        drawStarShape(centerX, centerY, cellPx * 0.35f, ArenaGold)
    }
}

private fun DrawScope.drawStarShape(cx: Float, cy: Float, radius: Float, color: Color) {
    val path = Path()
    val innerRadius = radius * 0.45f
    val points = 5
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else innerRadius
        val angle = (i * Math.PI / points) - (Math.PI / 2)
        val x = cx + (r * Math.cos(angle)).toFloat()
        val y = cy + (r * Math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color)
    drawPath(path, color = Color.White, style = Stroke(width = 1.2f))
}
