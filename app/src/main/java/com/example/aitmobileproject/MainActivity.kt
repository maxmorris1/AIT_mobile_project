package com.example.aitmobileproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aitmobileproject.ui.screens.DashboardScreen
import com.example.aitmobileproject.ui.screens.FlashcardsScreen
import com.example.aitmobileproject.ui.theme.AITMobileProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge enabled but without the system UI overlay border
        setContent {
            AITMobileProjectTheme {
                StudyAppNavHost()
            }
        }
    }
}

@Composable
fun StudyAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                onNavigateToFlashcards = { navController.navigate("flashcards") }
            )
        }
        composable("flashcards") {
            FlashcardsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}