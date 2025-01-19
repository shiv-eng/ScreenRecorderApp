package com.shivangi.screenrecorder.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.plcoding.recordscreen.ui.theme.CoralRed
import com.plcoding.recordscreen.ui.theme.MintGreen

@Composable
fun ScreenRecordScreen(
    isRecording: Boolean,
    hasNotificationPermission: Boolean,
    onRequestPermission: () -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (!hasNotificationPermission) {
                    // Request permission if needed
                    onRequestPermission()
                } else {
                    // Start or stop recording
                    if (isRecording) {
                        onStopRecording()
                    } else {
                        onStartRecording()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) CoralRed else MintGreen
            )
        ) {
            Text(
                text = if (isRecording) "Stop Recording" else "Start Recording",
                color = Color.Black
            )
        }
    }
}
