package com.example.aitmobileproject.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily

val Orange = Color(0xFFEC6C03)
val LightBeige = Color(0xFFFEF7EC)

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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(LightBeige, RoundedCornerShape(26.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color.Black.copy(alpha = 0.1f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = Orange,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                   Text(
                       text = "Perc",
                       fontFamily = InstrumentSerifFontFamily,
                       fontSize = 10.sp
                   )
                   Text(
                       text = "${(progress * 100).toInt()}",
                       fontFamily = InstrumentSerifFontFamily,
                       fontSize = 32.sp,
                       fontWeight = FontWeight.Bold
                   )
                   Text(
                       text = "ent",
                       fontFamily = InstrumentSerifFontFamily,
                       fontSize = 10.sp
                   )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontFamily = InstrumentSerifFontFamily,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}
