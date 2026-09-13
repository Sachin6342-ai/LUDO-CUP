package com.example.engine

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

class SoundAndHapticHelper(private val context: Context) {

    private var toneGen: ToneGenerator? = null
    private var vibrator: Vibrator? = null

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            Log.w("SoundAndHapticHelper", "Audio/Vibrator initialization fallback", e)
        }
    }

    fun playDiceRollSound(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
            } catch (ignored: Exception) {}
        }
        if (vibrateEnabled) {
            vibrate(30)
        }
    }

    fun playTokenMoveSound(soundEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 60)
            } catch (ignored: Exception) {}
        }
    }

    fun playCaptureSound(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGen?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 150)
            } catch (ignored: Exception) {}
        }
        if (vibrateEnabled) {
            vibrate(100)
        }
    }

    fun playHomeEntrySound(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_PROMPT, 180)
            } catch (ignored: Exception) {}
        }
        if (vibrateEnabled) {
            vibrate(60)
        }
    }

    fun playVictoryFanfare(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGen?.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 300)
            } catch (ignored: Exception) {}
        }
        if (vibrateEnabled) {
            vibrate(250)
        }
    }

    private fun vibrate(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (ignored: Exception) {}
    }

    fun release() {
        try {
            toneGen?.release()
        } catch (ignored: Exception) {}
    }
}
