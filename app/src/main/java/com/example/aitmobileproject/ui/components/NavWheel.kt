package com.example.aitmobileproject.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily

@Composable
fun NavWheel(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onActionSelected: (String) -> Unit = {}
) {
    val orange = Color(0xFFEC6C03)
    val black = Color.Black
    
    var isPressed by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    
    // Expansion progress - limited to show a reasonable expansion
    val expansionProgress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        label = "Expansion"
    )
    
    // Size - the tile is a square that grows when expanded
    val size by animateDpAsState(
        targetValue = if (isExpanded) 140.dp else 100.dp,
        label = "Size"
    )

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                    },
                    onDoubleTap = {
                        // Double tap = click - go home
                        isPressed = false
                        isExpanded = false
                        onClick()
                    },
                    onLongPress = {
                        isExpanded = true
                    }
                )
            }
            .background(
                color = black,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 26.dp,
                    bottomEnd = 26.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 26.dp,
                    bottomEnd = 26.dp
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Orange dial circle in the center
        Box(
            modifier = Modifier
                .size(if (isExpanded) 60.dp else 50.dp)
                .background(
                    orange,
                    shape = CircleShape
                )
        )
        
        // Side actions - dummy actions shown when expanded
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 70.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                NavActionButton(
                    title = "Flashcards",
                    onSelect = { onActionSelected("flashcards") }
                )
                NavActionButton(
                    title = "Settings",
                    onSelect = { onActionSelected("settings") }
                )
                NavActionButton(
                    title = "Profile",
                    onSelect = { onActionSelected("profile") }
                )
            }
        }
    }
}

@Composable
fun NavActionButton(
    title: String,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        TextButton(
            onClick = onSelect,
            content = {
                Text(
                    text = title,
                    fontFamily = InstrumentSerifFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        )
    }
}