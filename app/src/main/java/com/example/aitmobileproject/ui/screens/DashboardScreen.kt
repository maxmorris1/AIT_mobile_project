package com.example.aitmobileproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aitmobileproject.ui.components.*

@Composable
fun DashboardScreen(onNavigateToFlashcards: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            GreetingCard(name = "Max", modifier = Modifier.height(220.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Left Column - wider so the boxes meet slightly off-centered
                Column(
                    modifier = Modifier
                        .weight(0.55f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ActionTile(
                        subtitle = "Open",
                        title = "File\nBrowser",
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )
                    
                    // Tall box for NavWheel at the bottom-left
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(LightBeige, RoundedCornerShape(26.dp)),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        NavWheel(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(x = 12.dp, y = 12.dp),
                            onClick = onNavigateToFlashcards
                        )
                    }
                }

                // Right Column - narrower
                Column(
                    modifier = Modifier
                        .weight(0.45f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(LightBeige, RoundedCornerShape(26.dp))
                    )
                    
                    ProgressTile(
                        progress = 0.75f,
                        label = "Start\nTopic\nRevision",
                        modifier = Modifier.fillMaxWidth().height(220.dp)
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