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
            .padding(top = 0.dp, start = 6.dp, end = 6.dp, bottom = 0.dp)
    ) {
        // Main Content Column - Padded inwards by 10.dp for the tiles
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            GreetingCard(name = "Max", modifier = Modifier.height(210.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Left Column (58%)
                Column(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ActionTile(
                        subtitle = "Open",
                        title = "File\nBrowser",
                        modifier = Modifier.fillMaxWidth().height(130.dp)
                    )
                    
                    // Beige tile that houses the dial area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(26.dp))
                            .background(LightBeige)
                    )
                }

                // Right Column (42%)
                Column(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .background(LightBeige, RoundedCornerShape(26.dp))
                    )
                    
                    ProgressTile(
                        progress = 0.75f,
                        label = "Start\nTopic\nRevision",
                        modifier = Modifier.fillMaxWidth().height(235.dp)
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

        // The Dial - Placed independently of the main Column's 10.dp padding
        // This keeps it at the 4.dp edge gap, matching its original position
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 4.dp, bottom = 0.dp),
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
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    AITMobileProjectTheme {
        DashboardScreen()
    }
}
