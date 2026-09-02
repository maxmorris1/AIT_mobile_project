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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.aitmobileproject.ui.components.NavWheel
import com.example.aitmobileproject.ui.components.Orange
import com.example.aitmobileproject.ui.components.LightBeige
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily

@Composable
fun FlashcardsScreen(onNavigateBack: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
            .padding(top = 0.dp, start = 6.dp, end = 6.dp, bottom = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 0.dp), // Increased side padding for boxes
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(modifier = Modifier.size(50.dp, 60.dp).background(Orange, RoundedCornerShape(16.dp)))
                Spacer(modifier = Modifier.weight(1f))
                repeat(4) {
                    Box(modifier = Modifier.size(50.dp, 60.dp).background(LightBeige, RoundedCornerShape(16.dp)))
                }
            }

            // Main Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(LightBeige, RoundedCornerShape(26.dp))
            )

            // Bottom Section
            Row(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Response Buttons Row
                Row(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val labels = listOf("Again", "Hard", "Good", "Easy")
                    labels.forEach { label ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .background(LightBeige, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
                                .padding(bottom = 12.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Text(
                                text = label,
                                fontFamily = InstrumentSerifFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Right Orange Bar
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .fillMaxHeight()
                        .background(Orange, RoundedCornerShape(20.dp))
                )
            }
        }

        // Nav Wheel Container matching Dashboard style
        // Positioned exactly where the bottom-left tile is on the Dashboard
        // Background removed to prevent beige slivers in the corner
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 4.dp, bottom = 0.dp) // Matches dashboard inner column padding
                .fillMaxWidth(0.58f) // Matches dashboard left column weight
                .height(280.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            NavWheel(
                modifier = Modifier.align(Alignment.BottomStart),
                onClick = { onNavigateBack() },
                onActionSelected = { actionId ->
                    if (actionId != "flashcards") {
                        onNavigateBack()
                    }
                }
            )
        }
    }
}