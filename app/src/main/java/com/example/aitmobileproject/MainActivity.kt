package com.example.aitmobileproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.core.content.FileProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aitmobileproject.ui.components.UpdateDialog
import com.example.aitmobileproject.ui.screens.DashboardScreen
import com.example.aitmobileproject.ui.screens.FlashcardsScreen
import com.example.aitmobileproject.ui.screens.NoteTakerScreen
import com.example.aitmobileproject.ui.theme.AITMobileProjectTheme
import com.example.aitmobileproject.ui.viewmodel.UpdateState
import com.example.aitmobileproject.ui.viewmodel.UpdateViewModel
import java.io.File

class MainActivity : ComponentActivity() {
    private val updateViewModel: UpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AITMobileProjectTheme {
                val updateState by updateViewModel.state
                
                LaunchedEffect(Unit) {
                    updateViewModel.checkForUpdates()
                }

                UpdateDialog(
                    state = updateState,
                    onDownload = { 
                        if (updateState is UpdateState.UpdateAvailable) {
                            updateViewModel.downloadUpdate((updateState as UpdateState.UpdateAvailable).release)
                        }
                    },
                    onDismiss = { updateViewModel.dismissUpdate() },
                    onInstall = {
                        if (updateState is UpdateState.ReadyToInstall) {
                            installApk((updateState as UpdateState.ReadyToInstall).apkFile)
                        }
                    }
                )

                StudyAppNavHost()
            }
        }
    }

    private fun installApk(apkFile: File) {
        val uri: Uri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            apkFile
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }
}

@Composable
fun StudyAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "flashcards") {
        composable("dashboard") {
            DashboardScreen(
                onNavigateToFlashcards = { navController.navigate("flashcards") },
                onNavigateToNotes = { navController.navigate("notes") }
            )
        }
        composable("flashcards") {
            FlashcardsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNotes = { navController.navigate("notes") }
            )
        }
        composable("notes") {
            NoteTakerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
