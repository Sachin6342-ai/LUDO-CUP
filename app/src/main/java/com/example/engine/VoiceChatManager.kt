package com.example.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class VoiceState(
    val isConnected: Boolean = false,
    val isMicMuted: Boolean = true,
    val isSpeakerMuted: Boolean = false,
    val isPushToTalkActive: Boolean = false,
    val hasPermission: Boolean = false,
    val speakingPlayers: Set<String> = emptySet(),
    val voiceActivityLevel: Float = 0f
)

class VoiceChatManager(private val scope: CoroutineScope) {

    private val _voiceState = MutableStateFlow(VoiceState())
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private var simulationJob: Job? = null

    fun updatePermission(granted: Boolean) {
        _voiceState.value = _voiceState.value.copy(hasPermission = granted)
        if (granted && !_voiceState.value.isConnected) {
            connectVoice()
        }
    }

    fun connectVoice() {
        _voiceState.value = _voiceState.value.copy(
            isConnected = true,
            isMicMuted = true
        )
        startVoiceActivitySimulation()
    }

    fun disconnectVoice() {
        simulationJob?.cancel()
        _voiceState.value = _voiceState.value.copy(
            isConnected = false,
            speakingPlayers = emptySet(),
            voiceActivityLevel = 0f
        )
    }

    fun toggleMicMute() {
        val current = _voiceState.value.isMicMuted
        _voiceState.value = _voiceState.value.copy(isMicMuted = !current)
    }

    fun setPushToTalk(active: Boolean) {
        _voiceState.value = _voiceState.value.copy(
            isPushToTalkActive = active,
            isMicMuted = !active
        )
    }

    fun toggleSpeaker() {
        val current = _voiceState.value.isSpeakerMuted
        _voiceState.value = _voiceState.value.copy(isSpeakerMuted = !current)
    }

    private fun startVoiceActivitySimulation() {
        simulationJob?.cancel()
        simulationJob = scope.launch(Dispatchers.Default) {
            while (isActive) {
                if (_voiceState.value.isConnected && !_voiceState.value.isSpeakerMuted) {
                    // Occasionally simulate friendly audio cues from other players in voice chat
                    val isSomeoneSpeaking = Random.nextInt(100) < 40
                    val speaking = if (isSomeoneSpeaking) {
                        val chosen = if (Random.nextBoolean()) "player_2" else "player_3"
                        setOf(chosen)
                    } else {
                        emptySet()
                    }
                    val level = if (isSomeoneSpeaking || (!_voiceState.value.isMicMuted)) {
                        Random.nextFloat() * 0.8f + 0.2f
                    } else {
                        0f
                    }
                    _voiceState.value = _voiceState.value.copy(
                        speakingPlayers = speaking,
                        voiceActivityLevel = level
                    )
                } else {
                    _voiceState.value = _voiceState.value.copy(
                        speakingPlayers = emptySet(),
                        voiceActivityLevel = 0f
                    )
                }
                delay(600)
            }
        }
    }
}
