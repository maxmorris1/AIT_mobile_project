package com.example.aitmobileproject.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HajioLogo(size: Dp = 120.dp, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(size)) {
        val s = this.size.width
        val scale = s / 512f
        
        // Left Pill
        drawRoundRect(
            color = Color(0xFFFFF9F0),
            topLeft = Offset(112f * scale, 64f * scale),
            size = Size(128f * scale, 384f * scale),
            cornerRadius = CornerRadius(64f * scale)
        )
        
        // Top Right Pill
        drawRoundRect(
            color = Color(0xFFFF8A00),
            topLeft = Offset(272f * scale, 64f * scale),
            size = Size(128f * scale, 128f * scale),
            cornerRadius = CornerRadius(48f * scale)
        )
        
        // Bottom Right Pill
        drawRoundRect(
            color = Color(0xFFFFF9F0),
            topLeft = Offset(272f * scale, 224f * scale),
            size = Size(128f * scale, 224f * scale),
            cornerRadius = CornerRadius(64f * scale)
        )
    }
}
