package com.example.engine

import com.example.model.ChatMessage
import com.example.model.GameMode
import com.example.model.LudoColor
import com.example.model.Player
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

data class LobbyState(
    val isSearchingMatch: Boolean = false,
    val roomCode: String? = null,
    val isHost: Boolean = false,
    val playersInRoom: List<Player> = emptyList(),
    val targetPlayerCount: Int = 4,
    val isGameReady: Boolean = false,
    val searchSeconds: Int = 0,
    val connectionStatus: String = "Connected",
    val latencyMs: Int = 24,
    val chatMessages: List<ChatMessage> = emptyList()
)

class MultiplayerSyncManager(private val scope: CoroutineScope) {

    private val _lobbyState = MutableStateFlow(LobbyState())
    val lobbyState: StateFlow<LobbyState> = _lobbyState.asStateFlow()

    private var matchSearchJob: Job? = null
    private var pingJob: Job? = null

    init {
        startPingMonitor()
    }

    fun startQuickMatch(localPlayer: Player, playerCount: Int = 4) {
        matchSearchJob?.cancel()
        _lobbyState.value = LobbyState(
            isSearchingMatch = true,
            targetPlayerCount = playerCount,
            playersInRoom = listOf(localPlayer),
            searchSeconds = 0,
            connectionStatus = "Matchmaking..."
        )

        matchSearchJob = scope.launch(Dispatchers.Default) {
            val names = listOf("Rajesh_Pro", "Sarah_Sky", "Aarav_99", "Elena_R", "Vikram_Ace", "Maya_Queen")
            val colors = listOf(LudoColor.GREEN, LudoColor.YELLOW, LudoColor.BLUE)

            var seconds = 0
            while (seconds < 2) {
                delay(1000)
                seconds++
                _lobbyState.value = _lobbyState.value.copy(searchSeconds = seconds)
            }

            // Simulate finding real online opponents matched by rating/ELO
            val opponent1 = Player(
                id = "opp_1",
                name = names.random(),
                color = colors[0],
                isAi = false,
                avatarId = 2,
                pingMs = Random.nextInt(25, 45)
            )

            val current = _lobbyState.value.playersInRoom.toMutableList()
            current.add(opponent1)
            _lobbyState.value = _lobbyState.value.copy(playersInRoom = current, searchSeconds = 3)

            if (playerCount > 2) {
                delay(1000)
                val opponent2 = Player(
                    id = "opp_2",
                    name = names.shuffled().last(),
                    color = colors[1],
                    isAi = false,
                    avatarId = 3,
                    pingMs = Random.nextInt(30, 55)
                )
                current.add(opponent2)
                _lobbyState.value = _lobbyState.value.copy(playersInRoom = current, searchSeconds = 4)
            }

            if (playerCount == 4) {
                delay(800)
                val opponent3 = Player(
                    id = "opp_3",
                    name = "King_David",
                    color = colors[2],
                    isAi = false,
                    avatarId = 4,
                    pingMs = Random.nextInt(28, 48)
                )
                current.add(opponent3)
                _lobbyState.value = _lobbyState.value.copy(playersInRoom = current, searchSeconds = 5)
            }

            delay(600)
            _lobbyState.value = _lobbyState.value.copy(
                isSearchingMatch = false,
                isGameReady = true,
                connectionStatus = "Match Found! Launching..."
            )
        }
    }

    fun createPrivateRoom(hostPlayer: Player, targetCount: Int = 4): String {
        val code = "LA-${Random.nextInt(1000, 9999)}"
        _lobbyState.value = LobbyState(
            roomCode = code,
            isHost = true,
            targetPlayerCount = targetCount,
            playersInRoom = listOf(hostPlayer),
            connectionStatus = "Room Created"
        )
        return code
    }

    fun joinPrivateRoom(code: String, player: Player): Boolean {
        if (code.isBlank() || code.length < 4) return false

        val host = Player(
            id = "host_1",
            name = "ArenaHost_Priya",
            color = LudoColor.RED,
            isAi = false,
            avatarId = 0,
            pingMs = 28
        )
        val guest = player.copy(color = LudoColor.GREEN)

        _lobbyState.value = LobbyState(
            roomCode = code,
            isHost = false,
            targetPlayerCount = 2,
            playersInRoom = listOf(host, guest),
            isGameReady = true,
            connectionStatus = "Joined Room $code"
        )
        return true
    }

    fun addSystemChatMessage(text: String) {
        val msg = ChatMessage(
            id = "sys_${System.currentTimeMillis()}",
            senderId = "system",
            senderName = "System",
            senderColor = LudoColor.YELLOW,
            message = text,
            isSystem = true
        )
        _lobbyState.value = _lobbyState.value.copy(
            chatMessages = _lobbyState.value.chatMessages + msg
        )
    }

    fun sendUserChatMessage(player: Player, text: String, isEmoji: Boolean = false) {
        val msg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = player.id,
            senderName = player.name,
            senderColor = player.color,
            message = text,
            isEmoji = isEmoji
        )
        _lobbyState.value = _lobbyState.value.copy(
            chatMessages = _lobbyState.value.chatMessages + msg
        )
    }

    fun cancelSearch() {
        matchSearchJob?.cancel()
        _lobbyState.value = LobbyState()
    }

    private fun startPingMonitor() {
        pingJob = scope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(3000)
                _lobbyState.value = _lobbyState.value.copy(
                    latencyMs = Random.nextInt(20, 36)
                )
            }
        }
    }

    fun cleanup() {
        matchSearchJob?.cancel()
        pingJob?.cancel()
    }
}
