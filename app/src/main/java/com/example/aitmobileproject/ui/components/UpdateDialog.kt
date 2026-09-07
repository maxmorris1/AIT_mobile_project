package com.example.aitmobileproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.ui.viewmodel.UpdateState

@Composable
fun UpdateDialog(
    state: UpdateState,
    onDownload: () -> Unit,
    onDismiss: () -> Unit,
    onInstall: () -> Unit
) {
    if (state is UpdateState.Idle) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = state !is UpdateState.Idle) { /* block clicks */ }
    ) {
        when (state) {
            is UpdateState.UpdateAvailable -> {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text("Update Available") },
                    text = { Text("A new version (${state.release.tagName}) is available.") },
                    confirmButton = {
                        Button(onClick = onDownload) { Text("Download") }
                    },
                    dismissButton = {
                        TextButton(onClick = onDismiss) { Text("Later") }
                    }
                )
            }
            is UpdateState.Downloading -> {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Downloading...") },
                    text = {
                        LinearProgressIndicator(
                            progress = { state.progress },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {}
                )
            }
            is UpdateState.ReadyToInstall -> {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Download Complete") },
                    text = { Text("Ready to install.") },
                    confirmButton = {
                        Button(onClick = onInstall) { Text("Install") }
                    }
                )
            }
            is UpdateState.Error -> {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text("Error") },
                    text = { Text(state.message) },
                    confirmButton = {
                        Button(onClick = onDismiss) { Text("OK") }
                    }
                )
            }
            else -> {}
        }
    }
}
