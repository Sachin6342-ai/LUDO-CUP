package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.LudoAi
import com.example.model.AiDifficulty
import com.example.model.LudoBoardLayout
import com.example.model.LudoColor
import com.example.model.Player
import com.example.model.Token
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("LUDO CUP", appName)
    }

    @Test
    fun `token can only exit base on six`() {
        val baseToken = Token(id = 0, color = LudoColor.RED, stepCount = 0)
        assertTrue(baseToken.isInBase)
        assertFalse(baseToken.canMove(1))
        assertFalse(baseToken.canMove(5))
        assertTrue(baseToken.canMove(6))
    }

    @Test
    fun `token cannot exceed max steps`() {
        val tokenNearHome = Token(id = 0, color = LudoColor.RED, stepCount = 55)
        assertTrue(tokenNearHome.canMove(1))
        assertTrue(tokenNearHome.canMove(2))
        assertFalse(tokenNearHome.canMove(3))
        assertFalse(tokenNearHome.canMove(6))
    }

    @Test
    fun `track indices for different player start positions`() {
        assertEquals(0, LudoBoardLayout.getTrackIndexForStep(LudoColor.RED, 1))
        assertEquals(13, LudoBoardLayout.getTrackIndexForStep(LudoColor.GREEN, 1))
        assertEquals(26, LudoBoardLayout.getTrackIndexForStep(LudoColor.YELLOW, 1))
        assertEquals(39, LudoBoardLayout.getTrackIndexForStep(LudoColor.BLUE, 1))
    }

    @Test
    fun `safe track cells include all four start cells and four star cells`() {
        val safeCells = LudoBoardLayout.SAFE_TRACK_INDICES
        assertEquals(8, safeCells.size)
        assertTrue(safeCells.contains(0))
        assertTrue(safeCells.contains(8))
        assertTrue(safeCells.contains(13))
        assertTrue(safeCells.contains(21))
        assertTrue(safeCells.contains(26))
        assertTrue(safeCells.contains(34))
        assertTrue(safeCells.contains(39))
        assertTrue(safeCells.contains(47))
    }

    @Test
    fun `ai selects valid movable token on roll`() {
        val aiPlayer = Player(
            id = "ai_test",
            name = "SmartBot",
            color = LudoColor.GREEN,
            isAi = true,
            aiDifficulty = AiDifficulty.HARD,
            tokens = listOf(
                Token(id = 0, color = LudoColor.GREEN, stepCount = 0),
                Token(id = 1, color = LudoColor.GREEN, stepCount = 10),
                Token(id = 2, color = LudoColor.GREEN, stepCount = 50),
                Token(id = 3, color = LudoColor.GREEN, stepCount = 57)
            )
        )
        val allPlayers = listOf(aiPlayer)
        val selected = LudoAi.chooseTokenToMove(aiPlayer, allPlayers, 4, AiDifficulty.HARD)
        assertNotNull(selected)
        assertTrue(selected!!.id == 1 || selected.id == 2)
    }
}
