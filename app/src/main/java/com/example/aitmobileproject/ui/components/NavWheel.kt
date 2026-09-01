package com.example.aitmobileproject.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class NavAction(val id: String, val icon: ImageVector, val angle: Float)

@Composable
fun NavWheel(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onActionSelected: (String) -> Unit = {}
) {
    val orange = Color(0xFFEC6C03)
    val black = Color.Black
    
    var isExpanded by remember { mutableStateOf(false) }
    // Initial angle pointing towards the middle icon (Settings at -45)
    var currentAngle by remember { mutableStateOf(-45f) } 
    
    val actions = remember {
        listOf(
            NavAction("school", Icons.Default.School, -75f),
            NavAction("settings", Icons.Default.Settings, -45f),
            NavAction("profile", Icons.Default.Person, -15f)
        )
    }
    
    val selectedActionIndex = remember(currentAngle, isExpanded) {
        if (!isExpanded) -1
        else {
            actions.indices.minByOrNull { i ->
                var diff = kotlin.math.abs(actions[i].angle - currentAngle)
                if (diff > 180) diff = 360 - diff
                diff
            } ?: -1
        }
    }

    val blackCircleSize by animateDpAsState(
        targetValue = if (isExpanded) 180.dp else 100.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "BlackCircleSize"
    )

    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        // The Black Circle
        Box(
            modifier = Modifier
                .offset(x = (-12).dp, y = 12.dp)
                .size(blackCircleSize)
                .background(black, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { onClick() })
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { isExpanded = true },
                        onDrag = { change, _ ->
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val relativePosition = Offset(change.position.x - centerX, change.position.y - centerY)
                            val angleRad = atan2(relativePosition.y, relativePosition.x)
                            currentAngle = (angleRad * 180 / PI).toFloat()
                            change.consume()
                        },
                        onDragEnd = {
                            if (selectedActionIndex != -1) {
                                onActionSelected(actions[selectedActionIndex].id)
                            }
                            isExpanded = false
                        },
                        onDragCancel = { isExpanded = false }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Icons on a curve
            if (isExpanded) {
                actions.forEachIndexed { index, action ->
                    val isSelected = index == selectedActionIndex
                    val iconColor by animateColorAsState(if (isSelected) orange else Color.White)
                    val radius = 70.dp
                    
                    val angleRad = action.angle * PI / 180
                    val xOffset = (radius.value * cos(angleRad)).dp
                    val yOffset = (radius.value * sin(angleRad)).dp

                    Box(
                        modifier = Modifier
                            .offset(x = xOffset, y = yOffset)
                            .size(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = action.id,
                            tint = iconColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Fixed Orange Dial (75.dp)
            val animatedRotation by animateFloatAsState(targetValue = currentAngle)
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .rotate(animatedRotation + 45f)
                    .background(orange, CircleShape),
                contentAlignment = Alignment.TopCenter
            ) {
                // Notch to show direction
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(6.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}
