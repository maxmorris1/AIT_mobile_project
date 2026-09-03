package com.example.aitmobileproject.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.ui.components.NavWheel
import com.example.aitmobileproject.ui.components.Orange
import com.example.aitmobileproject.ui.components.LightBeige
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily

@Composable
fun FlashcardsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {}
) {
    var isShowingAnswer by remember { mutableStateOf(false) }
    val cornerRadius = 26.dp
    val gap = 2.dp
    
    // Constants for layout
    val topOrangeHeight = 150.dp
    val bottomSectionHeight = 460.dp
    val orangeWidth = 70.dp
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            // --- TOP ROW ---
            Row(
                modifier = Modifier.fillMaxWidth().height(topOrangeHeight),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                // Top Left Orange Pill
                Box(
                    modifier = Modifier
                        .width(orangeWidth)
                        .fillMaxHeight()
                        .background(Orange, RoundedCornerShape(cornerRadius)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("10:54", fontFamily = InstrumentSerifFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }

                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(gap)
                ) {
                    // Settings Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        horizontalArrangement = Arrangement.spacedBy(gap)
                    ) {
                        repeat(4) {
                            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(LightBeige, RoundedCornerShape(20.dp)))
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightBeige, RoundedCornerShape(topEnd = cornerRadius))
                    )
                }
            }

            // --- MAIN CARD (Canvas based) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .offset(y = (-2).dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize().clickable { isShowingAnswer = !isShowingAnswer }) {
                    val r = cornerRadius.toPx()
                    val g = gap.toPx()
                    val w = orangeWidth.toPx()
                    
                    drawRect(color = LightBeige)
                    
                    // Top-Left Concave Bite
                    drawCircle(
                        color = Color.Black,
                        radius = r + g,
                        center = Offset(w - r, -g - r)
                    )
                    
                    // Bottom-Right Concave Bite
                    drawCircle(
                        color = Color.Black,
                        radius = r + g,
                        center = Offset(size.width - w + g + r, size.height + g + r)
                    )
                }
                
                Crossfade(
                    targetState = isShowingAnswer, 
                    animationSpec = tween(500),
                    modifier = Modifier.align(Alignment.Center)
                ) { answerVisible ->
                    Text(
                        text = if (answerVisible) "The Answer" else "The Question?",
                        fontFamily = InstrumentSerifFontFamily,
                        fontSize = 32.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            // --- BOTTOM SECTION ---
            Row(
                modifier = Modifier.fillMaxWidth().height(bottomSectionHeight).offset(y = (-2).dp),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(gap)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(LightBeige, RoundedCornerShape(bottomStart = cornerRadius))
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(gap)
                    ) {
                        val labels = listOf("Again", "Hard", "Good", "Easy")
                        labels.forEach { label ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(LightBeige, RoundedCornerShape(cornerRadius))
                                    .padding(bottom = 32.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Text(label, fontFamily = InstrumentSerifFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .width(orangeWidth)
                        .fillMaxHeight()
                        .background(Orange, RoundedCornerShape(cornerRadius))
                )
            }
        }

        // Nav Wheel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 4.dp, bottom = 0.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            NavWheel(
                modifier = Modifier.align(Alignment.BottomStart),
                onClick = { onNavigateBack() },
                onActionSelected = { actionId ->
                    if (actionId == "notes") onNavigateToNotes()
                    else if (actionId != "flashcards") onNavigateBack()
                }
            )
        }
    }
}
