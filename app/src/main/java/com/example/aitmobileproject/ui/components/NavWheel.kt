package com.example.aitmobileproject.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.aitmobileproject.ui.theme.MainOrange
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class NavAction(val id: String, val icon: ImageVector, val angle: Float)

@Composable
fun NavWheel(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onActionSelected: (String) -> Unit = {},
) {
    val orange = MainOrange
    val black = Color.Black
    val haptic = LocalHapticFeedback.current
    
    var isExpanded by remember { mutableStateOf(value = false) }
    // Initial angle pointing towards the middle icon (Settings at -45)
    var currentAngle by remember { mutableFloatStateOf(value = -45f) } 
    
    val actions = remember {
        listOf(
            NavAction(id = "flashcards", icon = Icons.Default.Style, angle = -105f),
            NavAction(id = "school", icon = Icons.Default.School, angle = -70f),
            NavAction(id = "settings", icon = Icons.Default.Settings, angle = -35f),
            NavAction(id = "profile", icon = Icons.Default.Person, angle = 0f),
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

    LaunchedEffect(selectedActionIndex) {
        if (selectedActionIndex != -1) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val blackCircleSize by animateDpAsState(
        targetValue = if (isExpanded) 170.dp else 100.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "BlackCircleSize"
    )

    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        // Container that stays fixed in the corner
        // Its center is where the dial and expansion will be anchored
        Box(
            modifier = Modifier
                .offset(x = (-58).dp, y = 58.dp)
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            // The Black Circle - Grows from the center of the 200.dp container
            Box(
                modifier = Modifier
                    .size(blackCircleSize)
                    .background(black, CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onClick() })
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { isExpanded = true },
                            onDrag = { change, _ ->
                                val centerX = size.width / 2f
                                val centerY = size.height / 2f
                                val relativePosition = Offset(change.position.x - centerX, change.position.y - centerY)
                                val angleRad = atan2(relativePosition.y, relativePosition.x)
                                currentAngle = (angleRad * 180f / PI.toFloat())
                                change.consume()
                            },
                            onDragEnd = {
                                // Calculate selection immediately on release to be safe
                                val finalIndex = if (!isExpanded) -1
                                else {
                                    actions.indices.minByOrNull { i ->
                                        var diff = kotlin.math.abs(actions[i].angle - currentAngle)
                                        if (diff > 180) diff = 360 - diff
                                        diff
                                    } ?: -1
                                }
                                
                                if (finalIndex != -1) {
                                    onActionSelected(actions[finalIndex].id)
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
                    val radius = 64.dp
                    
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
                    .rotate(animatedRotation + 90f)
                    .background(orange, CircleShape),
                contentAlignment = Alignment.TopCenter
            ) {
                // Notch to show direction - Only visible when expanded
                if (isExpanded) {
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
}
}
