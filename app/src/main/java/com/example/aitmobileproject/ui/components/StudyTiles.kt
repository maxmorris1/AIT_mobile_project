package com.example.aitmobileproject.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.ui.theme.CustomCream
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily
import com.example.aitmobileproject.ui.theme.MainOrange

val Orange = MainOrange
val LightBeige = CustomCream

@Composable
fun GreetingCard(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Orange, RoundedCornerShape(26.dp))
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Good Morning,",
                color = Color.Black,
                fontFamily = InstrumentSerifFontFamily,
                fontSize = 28.sp,
                lineHeight = 32.sp
            )
            Text(
                text = name,
                color = Color.Black,
                fontFamily = InstrumentSerifFontFamily,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 52.sp
            )
        }
    }
}

@Composable
fun ActionTile(
    title: String,
    subtitle: String = "",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(LightBeige, RoundedCornerShape(26.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontFamily = InstrumentSerifFontFamily,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = title,
                fontFamily = InstrumentSerifFontFamily,
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun ProgressTile(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "ProgressTileScale"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            // Match the dial's haptic exactly
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        } else {
            // Subtle release haptic
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(LightBeige, RoundedCornerShape(26.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No default ripple to keep the custom feel
                onClick = {
                    onClick()
                }
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(18.dp)) // Slightly smaller top gap
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(117.dp)) { // ~10% smaller circle
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Full dark circle
                    drawArc(
                        color = Color.Black,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 12.5.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // Orange progress arc
                    drawArc(
                        color = Orange,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 12.5.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .offset(x = (-1).dp) // Adjusted offset
                        .padding(top = 2.dp)
                ) {
                   Text(
                       text = "Perc",
                       fontFamily = InstrumentSerifFontFamily,
                       fontSize = 9.sp, // ~10% smaller
                       fontWeight = FontWeight.Bold,
                       color = Color.Black
                   )
                   Text(
                       text = "${(progress * 100).toInt()}",
                       fontFamily = InstrumentSerifFontFamily,
                       fontSize = 46.sp, // ~10% smaller
                       fontWeight = FontWeight.Bold,
                       color = Color.Black,
                       modifier = Modifier.padding(start = 0.dp, end = 4.dp)
                   )
                   Text(
                       text = "ent",
                       fontFamily = InstrumentSerifFontFamily,
                       fontSize = 9.sp, // ~10% smaller
                       fontWeight = FontWeight.Bold,
                       color = Color.Black
                   )
                }
            }
            Spacer(modifier = Modifier.height(11.dp)) // ~10% smaller middle gap
            
            val lines = label.split("\n")
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy((-11).dp)
            ) {
                if (lines.isNotEmpty()) {
                    Text(
                        text = lines[0],
                        fontFamily = InstrumentSerifFontFamily,
                        fontSize = 11.sp, // ~10% smaller
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                if (lines.size > 1) {
                    lines.drop(1).forEach { line ->
                        Text(
                            text = line,
                            fontFamily = InstrumentSerifFontFamily,
                            fontSize = 29.sp, // ~10% smaller
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 25.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp)) // ~10% smaller bottom gap
        }
    }
}
