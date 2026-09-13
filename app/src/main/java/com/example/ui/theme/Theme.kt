package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = ArenaGold,
        onPrimary = Color(0xFF1E1B4B),
        primaryContainer = ArenaGoldDark,
        onPrimaryContainer = Color.White,
        secondary = LudoBlue,
        onSecondary = Color.White,
        tertiary = LudoGreen,
        background = ArenaNavy,
        surface = ArenaNavySurface,
        onBackground = Color(0xFFF1F5F9),
        onSurface = Color(0xFFF8FAFC),
        surfaceVariant = ArenaNavyCard,
        onSurfaceVariant = Color(0xFFCBD5E1),
    )

private val LightColorScheme =
    lightColorScheme(
        primary = ArenaGoldDark,
        onPrimary = Color.White,
        primaryContainer = ArenaGoldLight,
        onPrimaryContainer = Color(0xFF451A03),
        secondary = LudoBlue,
        onSecondary = Color.White,
        tertiary = LudoGreen,
        background = Color(0xFF0F172A), // Keep arena dark aesthetic by default for immersive gaming
        surface = Color(0xFF1E293B),
        onBackground = Color(0xFFF8FAFC),
        onSurface = Color(0xFFF8FAFC),
        surfaceVariant = Color(0xFF334155),
        onSurfaceVariant = Color(0xFFCBD5E1),
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional royal game palette
    content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
  )
}
