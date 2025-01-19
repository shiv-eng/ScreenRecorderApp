package com.shivangi.screenrecorder.viewmodel

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivangi.screenrecorder.ScreenRecordConfig
import com.shivangi.screenrecorder.ScreenRecordService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ScreenRecordViewModel : ViewModel() {

    /**
     * Observe the service's Flow to know if it's recording.
     */
    val isRecording = ScreenRecordService.isServiceRunning
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    /**
     * Start recording by launching the foreground service with the given [ScreenRecordConfig].
     */
    fun startRecording(context: Context, config: ScreenRecordConfig) {
        val serviceIntent = Intent(context, ScreenRecordService::class.java).apply {
            action = ScreenRecordService.START_RECORDING
            putExtra(ScreenRecordService.KEY_RECORDING_CONFIG, config)
        }
        context.startForegroundService(serviceIntent)
    }

    /**
     * Stop recording by sending STOP action to the service.
     */
    fun stopRecording(context: Context) {
        Intent(context, ScreenRecordService::class.java).also {
            it.action = ScreenRecordService.STOP_RECORDING
            context.startForegroundService(it)
        }
    }
}
