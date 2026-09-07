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
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.ui.components.NavWheel
import com.example.aitmobileproject.ui.components.Orange
import com.example.aitmobileproject.ui.components.LightBeige
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily
import kotlin.math.*

@Composable
fun FlashcardsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {}
) {
    var isShowingAnswer by remember { mutableStateOf(false) }
    val cornerRadius = 26.dp
    val gap = 2.dp
    
    val topOrangeHeight = 150.dp
    val bottomSectionHeight = 460.dp
    val orangeWidth = 70.dp
    
    // Geometry Constants for Rounded Cutouts
    val br = 38.dp // Bite Radius
    val fr = 20.dp // Fillet Radius - Increased for high visibility
    
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
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val r = cornerRadius.toPx()
                        val g = gap.toPx()
                        val brPx = br.toPx()
                        val frPx = fr.toPx()
                        
                        // Junction is at size.width + g/2 in global coords
                        val bx = size.width + g / 2
                        val by = size.height + g / 2
                        
                        val path = Path().apply {
                            // Start at Top-Left
                            moveTo(0f, r)
                            arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false)
                            lineTo(size.width - r, 0f)
                            arcTo(Rect(size.width - 2 * r, 0f, size.width, 2 * r), 270f, 90f, false)
                            
                            // Right side
                            lineTo(size.width, size.height - r)
                            arcTo(Rect(size.width - 2 * r, size.height - 2 * r, size.width, size.height), 0f, 90f, false)
                            
                            // Bottom-Right Rounded Bite
                            addRoundedBiteToPath(this, Offset(bx, by), brPx, frPx, size, side = "bottom")
                            
                            lineTo(r, size.height)
                            arcTo(Rect(0f, size.height - 2 * r, 2 * r, size.height), 90f, 90f, false)
                            close()
                        }
                        drawPath(path, color = Orange)
                    }
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("10:54", fontFamily = InstrumentSerifFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(gap)
                ) {
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

            // --- MAIN CARD ---
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
                    val brPx = br.toPx()
                    val frPx = fr.toPx()
                    
                    val path = Path().apply {
                        moveTo(0f, r)
                        arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false)
                        
                        // Top-Left Rounded Bite
                        addRoundedBiteToPath(this, Offset(w + g / 2, -g / 2), brPx, frPx, size, side = "top")
                        
                        lineTo(size.width - r, 0f)
                        arcTo(Rect(size.width - 2 * r, 0f, size.width, 2 * r), 270f, 90f, false)
                        
                        lineTo(size.width, size.height - r)
                        arcTo(Rect(size.width - 2 * r, size.height - 2 * r, size.width, size.height), 0f, 90f, false)
                        
                        // Bottom-Right Rounded Bite
                        addRoundedBiteToPath(this, Offset(size.width - (w + g / 2), size.height + g / 2), brPx, frPx, size, side = "bottom")
                        
                        lineTo(r, size.height)
                        arcTo(Rect(0f, size.height - 2 * r, 2 * r, size.height), 90f, 90f, false)
                        close()
                    }

                    drawPath(path, color = LightBeige)
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
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val r = cornerRadius.toPx()
                        val g = gap.toPx()
                        val brPx = br.toPx()
                        val frPx = fr.toPx()
                        
                        val bx = -g / 2
                        val by = -g / 2
                        
                        val path = Path().apply {
                            moveTo(0f, r)
                            arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false)
                            
                            // Top-Left Rounded Bite
                            addRoundedBiteToPath(this, Offset(bx, by), brPx, frPx, size, side = "top")
                            
                            lineTo(size.width - r, 0f)
                            arcTo(Rect(size.width - 2 * r, 0f, size.width, 2 * r), 270f, 90f, false)
                            
                            lineTo(size.width, size.height - r)
                            arcTo(Rect(size.width - 2 * r, size.height - 2 * r, size.width, size.height), 0f, 90f, false)
                            
                            lineTo(r, size.height)
                            arcTo(Rect(0f, size.height - 2 * r, 2 * r, size.height), 90f, 90f, false)
                            close()
                        }
                        
                        drawPath(path, color = Orange)
                    }
                }
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

/**
 * Constructs a perfectly rounded bite using arc segments.
 */
fun addRoundedBiteToPath(path: Path, bCenter: Offset, R: Float, fr: Float, size: Size, side: String) {
    val bx = bCenter.x
    val by = bCenter.y
    val isTop = side == "top"
    val fy = if (isTop) fr else size.height - fr
    
    val dy = abs(fy - by)
    if (dy >= R + fr) return
    
    val dx = sqrt((R + fr).pow(2) - dy.pow(2))
    
    // Fillet Centers
    val f1x = bx - dx
    val f2x = bx + dx
    
    // Tangency angles
    val angle1B = atan2(fy - by, f1x - bx).toDegree()
    val angle2B = atan2(fy - by, f2x - bx).toDegree()
    
    // Fillet tangency angles relative to fillet centers
    val angle1F = atan2(by - fy, bx - f1x).toDegree()
    val angle2F = atan2(by - fy, bx - f2x).toDegree()

    if (isTop) {
        // Top edge: Moving from Left to Right
        path.lineTo(f1x, 0f)
        path.arcTo(Rect(f1x - fr, fy - fr, f1x + fr, fy + fr), 270f, getSweep(270f, angle1F, true), false)
        path.arcTo(Rect(bx - R, by - R, bx + R, by + R), angle1B, getSweep(angle1B, angle2B, false), false)
        path.arcTo(Rect(f2x - fr, fy - fr, f2x + fr, fy + fr), angle2F, getSweep(angle2F, 270f, true), false)
    } else {
        // Bottom edge: Moving from Right to Left
        path.lineTo(f2x, size.height)
        path.arcTo(Rect(f2x - fr, fy - fr, f2x + fr, fy + fr), 90f, getSweep(90f, angle2F, true), false)
        path.arcTo(Rect(bx - R, by - R, bx + R, by + R), angle2B, getSweep(angle2B, angle1B, false), false)
        path.arcTo(Rect(f1x - fr, fy - fr, f1x + fr, fy + fr), angle1F, getSweep(angle1F, 90f, true), false)
    }
}

fun getSweep(from: Float, to: Float, clockwise: Boolean): Float {
    var diff = to - from
    if (clockwise) {
        while (diff < 0) diff += 360f
        while (diff > 360) diff -= 360f
    } else {
        while (diff > 0) diff -= 360f
        while (diff < -360) diff += 360f
    }
    return diff
}

fun Float.toDegree() = this * 180f / PI.toFloat()
