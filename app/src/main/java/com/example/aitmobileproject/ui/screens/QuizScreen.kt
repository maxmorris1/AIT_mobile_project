package com.example.aitmobileproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.ui.components.Orange
import com.example.aitmobileproject.ui.components.LightBeige
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: Int
)

@Composable
fun QuizScreen(onNavigateBack: () -> Unit) {
    val questions = remember {
        listOf(
            Question("What is the primary color of Hajio?", listOf("Blue", "Orange", "Green", "Red"), 1),
            Question("Which framework is used for this app?", listOf("Flutter", "React Native", "Jetpack Compose", "SwiftUI"), 2),
            Question("What is the font family used in the logo?", listOf("Roboto", "Instrument Serif", "Open Sans", "Arial"), 1)
        )
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (!isFinished) {
                val currentQuestion = questions[currentQuestionIndex]

                // Header Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(LightBeige)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                        }
                        Text(
                            "Revision Quiz",
                            color = Color.Black,
                            fontFamily = InstrumentSerifFontFamily,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "${currentQuestionIndex + 1}/${questions.size}",
                            color = Orange,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = InstrumentSerifFontFamily
                        )
                    }
                }

                // Question Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(26.dp))
                        .background(LightBeige)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentQuestion.text,
                        fontFamily = InstrumentSerifFontFamily,
                        fontSize = 34.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    )
                }

                // Options Grid/List
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    currentQuestion.options.forEachIndexed { index, option ->
                        val backgroundColor = when {
                            isAnswered && index == currentQuestion.correctAnswer -> Color(0xFF4CAF50)
                            isAnswered && index == selectedOption -> Color(0xFFF44336)
                            selectedOption == index -> Orange
                            else -> LightBeige
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .background(backgroundColor)
                                .clickable(enabled = !isAnswered) { selectedOption = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontFamily = InstrumentSerifFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOption == index || (isAnswered && index == currentQuestion.correctAnswer)) Color.White else Color.Black
                            )
                        }
                    }
                }

                // Action Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (selectedOption != null) Orange else LightBeige.copy(alpha = 0.5f))
                        .clickable(enabled = selectedOption != null) {
                            if (!isAnswered) {
                                isAnswered = true
                                if (selectedOption == currentQuestion.correctAnswer) score++
                            } else {
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedOption = null
                                    isAnswered = false
                                } else {
                                    isFinished = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isAnswered) "Next Question" else "Check Answer",
                        fontFamily = InstrumentSerifFontFamily,
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {
                // Result Screen - Hajio style
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(26.dp))
                        .background(Orange)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Quiz Complete",
                            fontFamily = InstrumentSerifFontFamily,
                            fontSize = 48.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Score: $score / ${questions.size}",
                            fontFamily = InstrumentSerifFontFamily,
                            fontSize = 32.sp,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(LightBeige)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Return to Dashboard",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontFamily = InstrumentSerifFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
