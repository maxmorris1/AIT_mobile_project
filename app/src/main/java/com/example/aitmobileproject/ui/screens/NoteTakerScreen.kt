package com.example.aitmobileproject.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.aitmobileproject.ui.components.NavWheel
import com.example.aitmobileproject.ui.components.Orange
import com.example.aitmobileproject.ui.components.LightBeige
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.max

enum class VisualizerState { IDLE, LISTENING }

@Composable
fun NoteTakerScreen(onNavigateBack: () -> Unit = {}) {
    val context = LocalContext.current
    
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

    var vizState by remember { mutableStateOf(VisualizerState.IDLE) }
    var isMuted by remember { mutableStateOf(false) }

    // Visualizer Values
    val baseHeight = 80f
    val bar1Height = remember { Animatable(baseHeight) }
    val bar2Height = remember { Animatable(baseHeight) }
    val bar3Height = remember { Animatable(baseHeight) }
    val bar4Height = remember { Animatable(baseHeight) }
    
    val bars = listOf(bar1Height, bar2Height, bar3Height, bar4Height)
    val springSpec = spring<Float>(stiffness = 150f, dampingRatio = 0.8f)

    // Audio Capture
    LaunchedEffect(vizState, isMuted, hasPermission) {
        if (vizState == VisualizerState.LISTENING && !isMuted && hasPermission) {
            val sampleRate = 44100
            val channelConfig = AudioFormat.CHANNEL_IN_MONO
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            val audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize)
            
            val buffer = ShortArray(bufferSize)
            audioRecord.startRecording()
            
            try {
                while (isActive && vizState == VisualizerState.LISTENING) {
                    val read = audioRecord.read(buffer, 0, bufferSize)
                    if (read > 0) {
                        val chunk = read / 4
                        for (i in 0 until 4) {
                            var maxAbs = 0f
                            for (j in (i * chunk) until ((i + 1) * chunk)) {
                                maxAbs = max(maxAbs, abs(buffer[j].toFloat()))
                            }
                            val db = if (maxAbs > 0) 20 * log10(maxAbs / 32768f) else -60f
                            val normalized = ((db + 60f) / 60f).coerceIn(0f, 1f)
                            val targetHeight = baseHeight + (normalized * 220f)
                            launch { bars[i].animateTo(targetHeight, springSpec) }
                        }
                    }
                    delay(50)
                }
            } finally {
                audioRecord.stop()
                audioRecord.release()
            }
        } else {
            bars.forEach { anim -> launch { anim.animateTo(baseHeight, springSpec) } }
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

                val movingUpperBase = 160.dp.value

                // Columns 2-5 (Visualizer & White Bars)
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
                                .background(LightBeige, RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp, bottomStart = 26.dp, bottomEnd = 26.dp))
                        )
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
                    Box(
                        modifier = Modifier.weight(1f).height(buttonHeight).background(Orange, RoundedCornerShape(26.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("Type", fontFamily = InstrumentSerifFontFamily, fontSize = 16.sp, color = Color.Black) }

                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .height(centerButtonHeight)
                            .background(Orange, RoundedCornerShape(26.dp))
                            .clickable {
                                if (!hasPermission) launcher.launch(Manifest.permission.RECORD_AUDIO)
                                else vizState = if (vizState == VisualizerState.LISTENING) VisualizerState.IDLE else VisualizerState.LISTENING
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Taking", fontFamily = InstrumentSerifFontFamily, fontSize = 12.sp, color = Color.Black, textAlign = TextAlign.Center)
                            Text(
                                if (vizState == VisualizerState.LISTENING) "Stop" else "Begin",
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
