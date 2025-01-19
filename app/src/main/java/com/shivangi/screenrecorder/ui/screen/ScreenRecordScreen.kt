package com.shivangi.screenrecorder.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plcoding.recordscreen.ui.theme.CoralRed
import com.plcoding.recordscreen.ui.theme.MintGreen

@Composable
fun ScreenRecordScreen(
    isRecording: Boolean,
    hasNotificationPermission: Boolean,
    onRequestPermission: () -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isRecording) "Recording in Progress" else "Ready to Record",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!hasNotificationPermission) {
                            onRequestPermission()
                        } else {
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
                    // Show Stop icon if recording, else FiberManualRecord icon
                    val icon = if (isRecording) Icons.Filled.Check else Icons.Filled.PlayArrow
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRecording) "Stop Recording" else "Start Recording",
                        color = Color.Black
                    )
                }
            }
        }
    }
}
