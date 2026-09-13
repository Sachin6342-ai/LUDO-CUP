package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.LudoRepository
import com.example.engine.LudoGameEngine
import com.example.engine.MultiplayerSyncManager
import com.example.engine.SoundAndHapticHelper
import com.example.engine.VoiceChatManager
import com.example.model.Achievement
import com.example.model.GameMode
import com.example.model.GameSettings
import com.example.model.Player
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LudoArenaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    val repository = LudoRepository(database)

    val soundHelper = SoundAndHapticHelper(application)
    val voiceChatManager = VoiceChatManager(viewModelScope)
    val multiplayerManager = MultiplayerSyncManager(viewModelScope)

    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    val userProfile: StateFlow<UserProfile> = repository.getUserProfile().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )

    private val _achievements = MutableStateFlow(repository.getAchievements())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    val gameEngine = LudoGameEngine(
        scope = viewModelScope,
        soundHelper = soundHelper,
        isSoundEnabled = { _settings.value.soundFxEnabled },
        isVibrateEnabled = { _settings.value.vibrationEnabled }
    )

    fun updateSettings(newSettings: GameSettings) {
        _settings.value = newSettings
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    fun addCoins(amount: Int) {
        viewModelScope.launch {
            repository.addCoins(amount)
        }
    }

    fun claimAchievement(achievement: Achievement) {
        viewModelScope.launch {
            val updated = _achievements.value.map {
                if (it.id == achievement.id) it.copy(isUnlocked = true) else it
            }
            _achievements.value = updated
            repository.addCoins(achievement.rewardCoins)
        }
    }

    fun startMatch(mode: GameMode, players: List<Player>, roomCode: String = "LA-MATCH") {
        if (_settings.value.voiceChatEnabled) {
            voiceChatManager.connectVoice()
        }
        multiplayerManager.addSystemChatMessage("Match started in ${mode.name.replace("_", " ")} mode!")
        gameEngine.startNewGame(mode, players, roomCode)
    }

    fun endMatch() {
        voiceChatManager.disconnectVoice()
        gameEngine.cleanUp()
    }

    override fun onCleared() {
        super.onCleared()
        soundHelper.release()
        voiceChatManager.disconnectVoice()
        multiplayerManager.cleanup()
        gameEngine.cleanUp()
    }
}
