package com.shivangi.screenrecorder.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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

    // Rename dialog
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
                Button(
                    onClick = {
                        val r = recordingToRename ?: return@Button
                        if (newName.isNotBlank()) {
                            recordingsViewModel.renameRecording(r, ensureMp4(newName))
                        }
                        showRenameDialog = false
                        newName = ""
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRenameDialog = false
                    newName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(recordings) { rec ->
            RecordingCard(
                recording = rec,
                onPlay = { recordingsViewModel.playRecording(rec) },
                onRename = {
                    recordingToRename = rec
                    newName = rec.displayName
                    showRenameDialog = true
                },
                onDelete = {
                    recordingsViewModel.deleteRecording(rec)
                }
            )
        }
    }
}

@Composable
fun RecordingCard(
    recording: Recording,
    onPlay: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onPlay() }

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
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${recording.size / 1024} KB",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row {
                IconButton(onClick = onRename) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Rename"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}

private fun ensureMp4(name: String): String {
    return if (name.endsWith(".mp4", ignoreCase = true)) name else "$name.mp4"
}
