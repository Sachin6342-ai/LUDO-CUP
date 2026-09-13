package com.example.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.VoiceState
import com.example.ui.theme.ArenaGold
import com.example.ui.theme.ArenaNavyCard
import com.example.ui.theme.ArenaSuccess

@Composable
fun VoiceChatControls(
    voiceState: VoiceState,
    onToggleMic: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onPushToTalkChange: (Boolean) -> Unit,
    onPermissionResult: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ArenaNavyCard)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Voice status indicator
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (voiceState.isConnected) ArenaSuccess else Color(0xFFEF4444)
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (voiceState.isConnected) {
                    if (voiceState.isMicMuted) "Voice Connected (Muted)" else "Voice Active"
                } else {
                    "Voice Off"
                },
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Speaker toggle
            IconButton(
                onClick = onToggleSpeaker,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (voiceState.isSpeakerMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                    contentDescription = "Toggle speaker",
                    tint = if (voiceState.isSpeakerMuted) Color(0xFFEF4444) else ArenaGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Mic Toggle / Request Permission
            IconButton(
                onClick = {
                    if (!voiceState.hasPermission) {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        onToggleMic()
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (!voiceState.isMicMuted) ArenaSuccess else Color(0xFF334155)
                    )
            ) {
                Icon(
                    imageVector = if (!voiceState.isMicMuted) Icons.Default.Mic else Icons.Default.MicOff,
                    contentDescription = "Toggle mic",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Push To Talk Hold Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (voiceState.isPushToTalkActive) ArenaSuccess else ArenaGold.copy(alpha = 0.2f)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                if (!voiceState.hasPermission) {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                } else {
                                    onPushToTalkChange(true)
                                    tryAwaitRelease()
                                    onPushToTalkChange(false)
                                }
                            }
                        )
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (voiceState.isPushToTalkActive) "TALKING..." else "HOLD PTT",
                    color = if (voiceState.isPushToTalkActive) Color.White else ArenaGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}
