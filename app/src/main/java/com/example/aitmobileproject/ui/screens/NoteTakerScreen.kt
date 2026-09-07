package com.example.aitmobileproject.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.aitmobileproject.ui.components.NavWheel
import com.example.aitmobileproject.ui.components.Orange
import com.example.aitmobileproject.ui.components.LightBeige
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

enum class NoteState { IDLE, LISTENING, PAUSED, PROCESSING, MORPHING, FINISHED }

@Composable
fun NoteTakerScreen(onNavigateBack: () -> Unit = {}) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Time State
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(1000)
        }
    }
    
    var hasPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { hasPermission = it }

    var noteState by remember { mutableStateOf(NoteState.IDLE) }
    var isMuted by remember { mutableStateOf(false) }
    var transcript by remember { mutableStateOf("") }
    var summaryText by remember { mutableStateOf("") }
    
    val morphHeight = remember { Animatable(0f) }
    val morphGap = remember { Animatable(2f) }
    val morphAlpha = remember { Animatable(0f) }

    // Visualizer Values
    val baseHeight = 80f
    val bar1Height = remember { Animatable(baseHeight) }
    val bar2Height = remember { Animatable(baseHeight) }
    val bar3Height = remember { Animatable(baseHeight) }
    val bar4Height = remember { Animatable(baseHeight) }
    
    val bars = listOf(bar1Height, bar2Height, bar3Height, bar4Height)
    val springSpec = spring<Float>(stiffness = 150f, dampingRatio = 0.8f)

    // Speech Recognizer setup
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }

    val recognitionListener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {
                if (noteState == NoteState.LISTENING && !isMuted) {
                    val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
                    bars.forEachIndexed { index, anim ->
                        scope.launch { 
                            anim.animateTo(baseHeight + (normalized * (200f + index * 20f)), springSpec) 
                        }
                    }
                }
            }
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                if (noteState == NoteState.LISTENING) {
                    speechRecognizer.startListening(recognizerIntent)
                }
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    transcript += (if (transcript.isEmpty()) "" else " ") + matches[0]
                }
                if (noteState == NoteState.LISTENING) {
                    speechRecognizer.startListening(recognizerIntent)
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    LaunchedEffect(noteState) {
        if (noteState == NoteState.LISTENING || noteState == NoteState.IDLE) {
            // Reset animatables for a fresh start
            scope.launch { morphHeight.snapTo(0f) }
            scope.launch { morphGap.snapTo(2f) }
            scope.launch { morphAlpha.snapTo(0f) }
            
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(recognizerIntent)
        } else if (noteState == NoteState.PAUSED) {
            speechRecognizer.stopListening()
            bars.forEach { anim -> launch { anim.animateTo(baseHeight, springSpec) } }
        } else {
            speechRecognizer.stopListening()
            if (noteState == NoteState.IDLE || noteState == NoteState.FINISHED || noteState == NoteState.PROCESSING) {
                bars.forEach { anim -> launch { anim.animateTo(baseHeight, springSpec) } }
            }
        }
    }

    LaunchedEffect(noteState) {
        if (noteState == NoteState.PROCESSING) {
            delay(500) // Quicker rest (0.5s) before starting animation
            
            val animationJob = scope.launch {
                val loadingSpec = tween<Float>(durationMillis = 800, easing = FastOutSlowInEasing)
                while (noteState == NoteState.PROCESSING) {
                    for (i in 3 downTo 0) {
                        bars.forEachIndexed { idx, anim ->
                            launch {
                                if (idx == i) anim.animateTo(baseHeight + 140f, loadingSpec)
                                else anim.animateTo(baseHeight, loadingSpec)
                            }
                        }
                        delay(250) // Slower cycle to give bars time to extend
                    }
                }
            }

            delay(4000) // Simulate AI Processing time for the animation
            animationJob.cancel()
            
            noteState = NoteState.MORPHING
        }
        
        if (noteState == NoteState.MORPHING) {
            summaryText = if (transcript.isBlank()) {
                "No audio captured. Please try again."
            } else {
                "AI GENERATED NOTES:\n\n" + transcript.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + ".\n\n" +
                "Key Points:\n- " + transcript.split(" ").take(10).joinToString(" ") + "...\n" +
                "- " + transcript.split(" ").drop(10).take(10).joinToString(" ") + "..."
            }
            
            // Animation: Grow taller
            launch {
                morphHeight.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
            }
            // Animation: Morph into box (close gaps)
            launch {
                delay(600)
                morphGap.animateTo(0f, tween(600, easing = LinearOutSlowInEasing))
            }
            
            delay(1200)
            noteState = NoteState.FINISHED
            morphAlpha.animateTo(1f, tween(500))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black).systemBarsPadding().padding(horizontal = 6.dp)
    ) {
        val columnGap = 2.dp
        val topCornerRadius = 26.dp
        val buttonHeight = 80.dp
        val centerButtonHeight = 110.dp
        val bottomLockedHeight = 205.dp
        val sideOffset = 15.dp

        Box(
            modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(columnGap)
            ) {
                // Column 1
                Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(columnGap)) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(130.dp).background(Orange, RoundedCornerShape(26.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AnimatedContent(
                                targetState = currentTime.get(Calendar.HOUR_OF_DAY),
                                transitionSpec = {
                                    (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                                        slideOutVertically { height -> height } + fadeOut()
                                    )
                                },
                                label = "HourAnimation"
                            ) { targetHour ->
                                Text(
                                    text = targetHour.toString(),
                                    fontFamily = InstrumentSerifFontFamily,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            
                            Row(
                                modifier = Modifier.offset(y = (-10).dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val minute = currentTime.get(Calendar.MINUTE)
                                val tens = minute / 10
                                val ones = minute % 10

                                AnimatedContent(
                                    targetState = tens,
                                    transitionSpec = {
                                        (slideInVertically(animationSpec = tween(durationMillis = 500, delayMillis = 1000)) { height -> -height } + fadeIn(animationSpec = tween(delayMillis = 1000))).togetherWith(
                                            slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeOut()
                                        )
                                    },
                                    label = "MinuteTensAnimation"
                                ) { targetTens ->
                                    Text(
                                        text = targetTens.toString(),
                                        fontFamily = InstrumentSerifFontFamily,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }

                                AnimatedContent(
                                    targetState = ones,
                                    transitionSpec = {
                                        (slideInVertically(animationSpec = tween(durationMillis = 500)) { height -> -height } + fadeIn()).togetherWith(
                                            slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeOut()
                                        )
                                    },
                                    label = "MinuteOnesAnimation"
                                ) { targetOnes ->
                                    Text(
                                        text = targetOnes.toString(),
                                        fontFamily = InstrumentSerifFontFamily,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxSize().background(LightBeige, RoundedCornerShape(26.dp)))
                }

                val movingUpperBase = 200.dp.value

                // Columns 2-5
                if (noteState == NoteState.FINISHED || noteState == NoteState.MORPHING) {
                    Box(modifier = Modifier.weight(4f).fillMaxHeight()) {
                        // Top Content Column (Notes box and white bars)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = bottomLockedHeight + centerButtonHeight + columnGap),
                            verticalArrangement = Arrangement.spacedBy(columnGap)
                        ) {
                            // Top White Bars (Separated into 4) - Fixed gaps to preserve "lines" structure
                            val topWeight = 1.9f - (1.2f * morphHeight.value)
                            Row(
                                modifier = Modifier.fillMaxWidth().weight(topWeight),
                                horizontalArrangement = Arrangement.spacedBy(columnGap)
                            ) {
                                repeat(4) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(LightBeige, RoundedCornerShape(topStart = topCornerRadius, topEnd = topCornerRadius, bottomStart = 26.dp, bottomEnd = 26.dp))
                                    )
                                }
                            }

                            // Orange Notes Box / Morphing bars
                            val orangeWeight = 0.2f + (1.9f * morphHeight.value)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(orangeWeight),
                                contentAlignment = Alignment.Center
                            ) {
                                // The morphing bars growing within the orange slot
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    horizontalArrangement = Arrangement.spacedBy(morphGap.value.dp)
                                ) {
                                    repeat(4) { index ->
                                        // Animate internal corner radius based on the gap closing
                                        // When gap is 2dp, radius is 26dp. When gap is 0dp, radius is 0dp.
                                        val innerRadius = (morphGap.value * 13f).dp
                                        val shape = when (index) {
                                            0 -> RoundedCornerShape(topStart = 26.dp, bottomStart = 26.dp, topEnd = innerRadius, bottomEnd = innerRadius)
                                            3 -> RoundedCornerShape(topEnd = 26.dp, bottomEnd = 26.dp, topStart = innerRadius, bottomStart = innerRadius)
                                            else -> RoundedCornerShape(innerRadius)
                                        }
                                        
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .background(Orange, shape)
                                        )
                                    }
                                }
                                
                                // Text Content (Fades in after morphing)
                                if (noteState == NoteState.FINISHED) {
                                    Column(
                                        modifier = Modifier.fillMaxSize().padding(20.dp).graphicsLayer(alpha = morphAlpha.value).verticalScroll(rememberScrollState()),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            summaryText,
                                            fontFamily = InstrumentSerifFontFamily,
                                            fontSize = 18.sp,
                                            color = Color.Black,
                                            lineHeight = 24.sp
                                        )
                                    }
                                }
                            }

                            // Bottom White Bars (Separated into 4, above buttons)
                            val bottomWeight = 1.2f - (0.7f * morphHeight.value)
                            Row(
                                modifier = Modifier.fillMaxWidth().weight(bottomWeight),
                                horizontalArrangement = Arrangement.spacedBy(columnGap)
                            ) {
                                repeat(4) { index ->
                                    val isOuter = index == 0 || index == 3
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .then(
                                                if (isOuter) {
                                                    Modifier.layout { measurable, constraints ->
                                                        val extension = sideOffset.roundToPx()
                                                        val placeable = measurable.measure(
                                                            constraints.copy(
                                                                minHeight = constraints.maxHeight + extension,
                                                                maxHeight = constraints.maxHeight + extension
                                                            )
                                                        )
                                                        layout(placeable.width, constraints.maxHeight) {
                                                            placeable.placeRelative(0, 0)
                                                        }
                                                    }
                                                } else Modifier
                                            )
                                            .background(LightBeige, RoundedCornerShape(26.dp))
                                    )
                                }
                            }
                        }

                        // Stationary White Bars (Bottom) - Below the buttons
                        Row(
                            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.spacedBy(columnGap)
                        ) {
                            repeat(4) { index ->
                                val isMiddle = index == 1 || index == 2
                                val barHeight = if (isMiddle) {
                                    bottomLockedHeight + columnGap - 3.dp
                                } else {
                                    bottomLockedHeight + sideOffset + columnGap
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(barHeight)
                                        // Offset brought up slightly from 20dp to 16dp
                                        .offset(y = if (isMiddle) 16.dp else 0.dp)
                                        .background(LightBeige, RoundedCornerShape(26.dp))
                                )
                            }
                        }
                    }
                } else {
                    bars.forEachIndexed { index, anim ->
                        val isMiddle = index == 1 || index == 2
                        
                        val visualizerBottomPadding = if (isMiddle) {
                            bottomLockedHeight + centerButtonHeight + columnGap
                        } else {
                            bottomLockedHeight + sideOffset + buttonHeight + columnGap
                        }
                        
                        val currentBottomBarHeight = if (isMiddle) {
                            bottomLockedHeight + columnGap
                        } else {
                            bottomLockedHeight + sideOffset + columnGap
                        }
                        
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            val upperWhiteHeight = (movingUpperBase - (anim.value - baseHeight) * 0.55f).coerceIn(40f, 350f).dp

                            // Moving elements (Top)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = visualizerBottomPadding),
                                verticalArrangement = Arrangement.spacedBy(columnGap)
                            ) {
                                Box(
                                    modifier = Modifier.weight(1f).fillMaxWidth()
                                        .background(LightBeige, RoundedCornerShape(topStart = topCornerRadius, topEnd = topCornerRadius, bottomStart = 26.dp, bottomEnd = 26.dp))
                                )
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(anim.value.dp)
                                        .background(Orange, RoundedCornerShape(26.dp))
                                )
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                        .height(upperWhiteHeight + if (isMiddle) 0.dp else sideOffset)
                                        .background(LightBeige, RoundedCornerShape(26.dp))
                                )
                            }

                            // Stationary White Bars (Bottom)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(currentBottomBarHeight)
                                    .align(Alignment.BottomCenter)
                                    .background(LightBeige, RoundedCornerShape(26.dp))
                            )
                        }
                    }
                }

                // Column 6
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Box(modifier = Modifier.fillMaxSize().background(LightBeige, RoundedCornerShape(26.dp)))
                }
            }

            // Button Layer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomLockedHeight + columnGap)
                    .height(centerButtonHeight),
                horizontalArrangement = Arrangement.spacedBy(columnGap)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.weight(4f).fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(columnGap),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pause Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight)
                            .background(Orange, RoundedCornerShape(26.dp))
                            .clickable {
                                if (noteState == NoteState.LISTENING) noteState = NoteState.PAUSED
                                else if (noteState == NoteState.PAUSED) noteState = NoteState.LISTENING
                            },
                        contentAlignment = Alignment.Center
                    ) { 
                        Text(
                            if (noteState == NoteState.PAUSED) "Resume" else "Pause", 
                            fontFamily = InstrumentSerifFontFamily, 
                            fontSize = 16.sp, 
                            color = Color.Black
                        ) 
                    }

                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .height(centerButtonHeight)
                            .background(Orange, RoundedCornerShape(26.dp))
                            .clickable {
                                if (!hasPermission) {
                                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                                } else {
                                    when (noteState) {
                                        NoteState.IDLE, NoteState.FINISHED -> {
                                            transcript = ""
                                            noteState = NoteState.LISTENING
                                        }
                                        NoteState.LISTENING, NoteState.PAUSED -> {
                                            noteState = NoteState.PROCESSING
                                        }
                                        NoteState.PROCESSING, NoteState.MORPHING -> {}
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (noteState == NoteState.PROCESSING || noteState == NoteState.MORPHING || noteState == NoteState.FINISHED) "New" else "Taking",
                                fontFamily = InstrumentSerifFontFamily, 
                                fontSize = 12.sp, 
                                color = Color.Black, 
                                textAlign = TextAlign.Center
                            )
                            Text(
                                when (noteState) {
                                    NoteState.LISTENING, NoteState.PAUSED -> "Stop"
                                    NoteState.PROCESSING, NoteState.MORPHING -> "..."
                                    else -> "Begin"
                                },
                                fontFamily = InstrumentSerifFontFamily,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.offset(y = (-6).dp)
                            )
                            Text("Notes", fontFamily = InstrumentSerifFontFamily, fontSize = 12.sp, color = Color.Black, textAlign = TextAlign.Center, modifier = Modifier.offset(y = (-8).dp))
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f).height(buttonHeight).background(Orange, RoundedCornerShape(26.dp))
                            .clickable { isMuted = !isMuted },
                        contentAlignment = Alignment.Center
                    ) { Text(if (isMuted) "Unmute" else "Mute", fontFamily = InstrumentSerifFontFamily, fontSize = 16.sp, color = Color.Black) }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Nav Wheel
        Box(modifier = Modifier.fillMaxSize().padding(start = 4.dp), contentAlignment = Alignment.BottomStart) {
            NavWheel(
                modifier = Modifier.align(Alignment.BottomStart),
                onClick = { onNavigateBack() },
                onActionSelected = { if (it != "notes") onNavigateBack() }
            )
        }
    }
}
