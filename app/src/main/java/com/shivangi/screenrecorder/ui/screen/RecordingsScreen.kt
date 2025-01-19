package com.shivangi.screenrecorder.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shivangi.screenrecorder.viewmodel.Recording
import com.shivangi.screenrecorder.viewmodel.RecordingsViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RecordingsScreen(
    recordingsViewModel: RecordingsViewModel
) {
    val recordings by recordingsViewModel.recordings.collectAsStateWithLifecycle()

    var showRenameDialog by remember { mutableStateOf(false) }
    var recordingToRename by remember { mutableStateOf<Recording?>(null) }
    var newName by remember { mutableStateOf("") }

    if (showRenameDialog && recordingToRename != null) {
        AlertDialog(
            onDismissRequest = {
                showRenameDialog = false
            },
            title = { Text("Rename Recording") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    val r = recordingToRename ?: return@Button
                    if (newName.isNotBlank()) {
                        recordingsViewModel.renameRecording(r, ensureMp4(newName))
                    }
                    showRenameDialog = false
                    newName = ""
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showRenameDialog = false
                    newName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recordings) { rec ->
            RecordingItem(
                recording = rec,
                onPlay = { recordingsViewModel.playRecording(rec) },
                onDelete = { recordingsViewModel.deleteRecording(rec) },
                onRename = {
                    recordingToRename = rec
                    newName = rec.displayName
                    showRenameDialog = true
                }
            )
        }
    }
}

@Composable
fun RecordingItem(
    recording: Recording,
    onPlay: () -> Unit,
    onDelete: () -> Unit,
    onRename: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() },
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = recording.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${recording.size / 1024} KB",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Row {
                TextButton(onClick = { onRename() }) {
                    Text("Rename")
                }
                TextButton(onClick = { onDelete() }) {
                    Text("Delete")
                }
            }
        }
    }
}

/** Ensure .mp4 suffix. */
private fun ensureMp4(name: String): String {
    return if (name.endsWith(".mp4", ignoreCase = true)) name else "$name.mp4"
}
