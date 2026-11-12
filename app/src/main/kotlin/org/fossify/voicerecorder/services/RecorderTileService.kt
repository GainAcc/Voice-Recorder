package org.fossify.voicerecorder.services

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import org.fossify.voicerecorder.activities.BackgroundRecordActivity

class RecorderTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile(RecorderService.isRunning)
    }

    override fun onClick() {
        super.onClick()
        val willRecord = !RecorderService.isRunning

        val intent = Intent(this, BackgroundRecordActivity::class.java).apply {
            action = BackgroundRecordActivity.RECORD_INTENT_ACTION
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                startActivityAndCollapse(pendingIntent)
            } else {
                @Suppress("DEPRECATION")
                startActivityAndCollapse(intent)
            }
            updateTile(willRecord)
        } catch (ignored: Exception) {
        }
    }

    private fun updateTile(isRecording: Boolean) {
        val tile = qsTile ?: return
        tile.state = if (isRecording) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }
}
