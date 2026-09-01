package com.example.aitmobileproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aitmobileproject.ui.components.*
import com.example.aitmobileproject.ui.theme.AITMobileProjectTheme

@Composable
fun DashboardScreen(onNavigateToFlashcards: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding() // Handles obstruction by status/navigation bars
            .padding(top = 0.dp, start = 6.dp, end = 6.dp, bottom = 0.dp) // Bottom padding removed
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 0.dp), // Bottom padding removed
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            GreetingCard(name = "Max", modifier = Modifier.height(210.dp)) // Smaller box
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Left Column - widened to fill gap (58%)
                Column(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ActionTile(
                        subtitle = "Open",
                        title = "File\nBrowser",
                        modifier = Modifier.fillMaxWidth().height(130.dp) // Bigger box
                    )
                    
                    // Tall box for NavWheel at the bottom-left
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(26.dp))
                            .background(LightBeige),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        NavWheel(
                            modifier = Modifier.align(Alignment.BottomStart),
                            onClick = onNavigateToFlashcards,
                            onActionSelected = { actionId ->
                                if (actionId == "flashcards") onNavigateToFlashcards()
                            }
                        )
                    }
                }

                // Right Column - thinner (42%)
                Column(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp) // Taller top box
                            .background(LightBeige, RoundedCornerShape(26.dp))
                    )
                    
                    ProgressTile(
                        progress = 0.75f,
                        label = "Start\nTopic\nRevision",
                        modifier = Modifier.fillMaxWidth().height(248.dp) // ~10% smaller height
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(LightBeige, RoundedCornerShape(26.dp))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    AITMobileProjectTheme {
        DashboardScreen()
    }
}
