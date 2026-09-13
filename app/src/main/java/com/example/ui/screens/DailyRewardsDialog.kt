package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.DailyRewardDay
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaNavySurface
import com.example.ui.theme.ArenaSuccess

@Composable
fun DailyRewardsDialog(
    onClaimDay: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sampleDays = listOf(
        DailyRewardDay(1, "Day 1", "+200 Coins", 200, 50, isClaimed = true, isAvailable = false),
        DailyRewardDay(2, "Day 2", "+350 Coins", 350, 75, isClaimed = true, isAvailable = false),
        DailyRewardDay(3, "Day 3", "+500 Coins", 500, 100, isClaimed = false, isAvailable = true),
        DailyRewardDay(4, "Day 4", "+750 Coins", 750, 150, isClaimed = false, isAvailable = false),
        DailyRewardDay(5, "Day 5", "+1,000 Coins", 1000, 200, isClaimed = false, isAvailable = false),
        DailyRewardDay(6, "Day 6", "+1,500 Coins", 1500, 300, isClaimed = false, isAvailable = false),
        DailyRewardDay(7, "Day 7", "Legendary Chest", 3000, 500, isClaimed = false, isAvailable = false)
    )

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .shadow(20.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(ArenaNavySurface, ArenaNavyCard)
                    )
                )
                .border(2.dp, ArenaGold, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Gift Icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(ArenaGold.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = ArenaGold,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "DAILY REWARD STREAK",
                    color = ArenaGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )

                Text(
                    text = "Log in each day to unlock escalating rewards!",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                // Grid of 7 days
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sampleDays.chunked(4).forEach { rowDays ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowDays.forEach { day ->
                                DailyDayPill(day = day, modifier = Modifier.weight(1f))
                            }
                            if (rowDays.size < 4) {
                                repeat(4 - rowDays.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onClaimDay(3, 500)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ArenaGold),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("CLAIM DAY 3 (+500 COINS)", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                TextButton(onClick = onDismiss) {
                    Text("CLOSE", color = Color(0xFF94A3B8), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun DailyDayPill(day: DailyRewardDay, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (day.isAvailable) ArenaGold.copy(alpha = 0.2f) else ArenaNavyCard
            )
            .border(
                1.dp,
                if (day.isAvailable) ArenaGold else if (day.isClaimed) ArenaSuccess else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .padding(vertical = 10.dp, horizontal = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.title,
                color = if (day.isAvailable) ArenaGold else Color(0xFF94A3B8),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (day.isClaimed) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Claimed",
                    tint = ArenaSuccess,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = if (day.isAvailable) ArenaGold else Color(0xFF64748B),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "+${day.coinAmount}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
