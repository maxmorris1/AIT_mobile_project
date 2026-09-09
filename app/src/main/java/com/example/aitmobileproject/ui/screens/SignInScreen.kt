package com.example.aitmobileproject.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.data.local.AuthManager
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily
import com.example.aitmobileproject.ui.components.*
import kotlin.math.*

@Composable
fun SignInScreen(onSignInSuccess: () -> Unit, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    
    var username by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    
    val isRegistered = remember { authManager.isRegistered() }
    val gap = 2.dp
    val pillShape = RoundedCornerShape(26.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            // Row 1: Full width beige
            PillBox(Modifier.fillMaxWidth().height(60.dp), LightBeige)

            // Row 2: 70/30 split beige
            Row(Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(0.7f).fillMaxHeight(), LightBeige)
                PillBox(Modifier.weight(0.3f).fillMaxHeight(), LightBeige)
            }

            // Row 3: Username (Orange 80/20)
            Row(Modifier.fillMaxWidth().height(80.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                Box(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxHeight()
                        .clip(pillShape)
                        .background(Orange)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (!isRegistered) {
                        BasicTextField(
                            value = username,
                            onValueChange = { username = it },
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 28.sp,
                                fontFamily = InstrumentSerifFontFamily,
                                fontWeight = FontWeight.Bold
                            ),
                            cursorBrush = SolidColor(Color.Black),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (username.isEmpty()) {
                                    Text("Username", color = Color.Black.copy(0.4f), fontSize = 28.sp, fontFamily = InstrumentSerifFontFamily)
                                }
                                innerTextField()
                            }
                        )
                    } else {
                        Text(authManager.getUsername() ?: "User", color = Color.Black, fontSize = 32.sp, fontFamily = InstrumentSerifFontFamily, fontWeight = FontWeight.Bold)
                    }
                }
                PillBox(Modifier.weight(0.2f).fillMaxHeight(), LightBeige)
            }

            // Row 4: 40/60 split beige
            Row(Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(0.4f).fillMaxHeight(), LightBeige)
                PillBox(Modifier.weight(0.6f).fillMaxHeight(), LightBeige)
            }

            // Row 5: Password (Orange 30/70)
            Row(Modifier.fillMaxWidth().height(80.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(0.3f).fillMaxHeight(), LightBeige)
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                        .clip(pillShape)
                        .background(Orange)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = pin,
                        onValueChange = { if (it.length <= 4) pin = it },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 28.sp,
                            fontFamily = InstrumentSerifFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        cursorBrush = SolidColor(Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        decorationBox = { innerTextField ->
                            if (pin.isEmpty()) {
                                Text("Password", color = Color.Black.copy(0.4f), fontSize = 28.sp, fontFamily = InstrumentSerifFontFamily)
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // Row 6: 50/50 split beige
            Row(Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(0.5f).fillMaxHeight(), LightBeige)
                PillBox(Modifier.weight(0.5f).fillMaxHeight(), LightBeige)
            }

            // Row 7: 65/35 split beige
            Row(Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(0.65f).fillMaxHeight(), LightBeige)
                PillBox(Modifier.weight(0.35f).fillMaxHeight(), LightBeige)
            }

            // Row 8: Wide beige
            PillBox(Modifier.fillMaxWidth().height(60.dp), LightBeige)

            // Row 9: Social Row
            Row(Modifier.fillMaxWidth().height(70.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(1f).fillMaxHeight(), LightBeige)
                SocialButton("GitHub", Modifier.weight(1f).fillMaxHeight(), authManager, onSignInSuccess)
                PillBox(Modifier.weight(0.5f).fillMaxHeight(), LightBeige)
                SocialButton("Google", Modifier.weight(1f).fillMaxHeight(), authManager, onSignInSuccess)
                PillBox(Modifier.weight(1f).fillMaxHeight(), LightBeige)
            }

            // Row 10: Wide beige
            PillBox(Modifier.fillMaxWidth().height(80.dp), LightBeige)

            // Row 11: 40/60 split beige
            Row(Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(0.4f).fillMaxHeight(), LightBeige)
                PillBox(Modifier.weight(0.6f).fillMaxHeight(), LightBeige)
            }

            // Row 12: Login/Sign Up (Full Orange)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(pillShape)
                    .background(Orange)
                    .clickable {
                        if (isRegistered) {
                            if (authManager.verifyPin(pin)) {
                                authManager.saveCredentials(authManager.getUsername()!!, pin)
                                onSignInSuccess()
                            } else {
                                error = "Incorrect PIN"
                            }
                        } else {
                            if (username.isNotEmpty() && pin.length == 4) {
                                authManager.saveCredentials(username, pin)
                                onSignInSuccess()
                            } else {
                                error = "Enter ID & 4 Digits"
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login/Sign Up",
                    color = Color.Black,
                    fontSize = 28.sp,
                    fontFamily = InstrumentSerifFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            // Row 13: 50/50 split beige
            Row(Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                PillBox(Modifier.weight(0.5f).fillMaxHeight(), LightBeige)
                PillBox(Modifier.weight(0.5f).fillMaxHeight(), LightBeige)
            }

            // Row 14: 30/70 split beige with bite
            Row(Modifier.fillMaxWidth().height(70.dp), horizontalArrangement = Arrangement.spacedBy(gap)) {
                Box(modifier = Modifier.weight(0.3f).fillMaxHeight()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val r = 26.dp.toPx()
                        val g = gap.toPx()
                        val br = 38.dp.toPx()
                        val fr = 20.dp.toPx()
                        
                        // Bite center: x is at size.width + g/2
                        val bx = size.width + g / 2
                        val by = size.height + g / 2
                        
                        val path = Path().apply {
                            moveTo(0f, r)
                            arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false)
                            lineTo(size.width, 0f)
                            lineTo(size.width, size.height - r) // Straight edge meeting bite
                            addRoundedBiteToPath(this, Offset(bx, by), br, fr, size, side = "bottom")
                            lineTo(r, size.height)
                            arcTo(Rect(0f, size.height - 2 * r, 2 * r, size.height), 90f, 90f, false)
                            close()
                        }
                        drawPath(path, color = LightBeige)
                    }
                }
                PillBox(Modifier.weight(0.7f).fillMaxHeight(), LightBeige)
            }

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red, fontSize = 14.sp, modifier = Modifier.fillMaxWidth().padding(top = 4.dp), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))
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
                    if (actionId != "profile") onNavigateBack()
                }
            )
        }
    }
}

@Composable
fun PillBox(modifier: Modifier, color: Color) {
    Box(modifier = modifier.clip(RoundedCornerShape(26.dp)).background(color))
}

@Composable
fun SocialButton(type: String, modifier: Modifier, authManager: AuthManager, onSignInSuccess: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Orange)
            .clickable {
                authManager.saveCredentials("$type User", "", type)
                onSignInSuccess()
            },
        contentAlignment = Alignment.Center
    ) {
        if (type == "Google") {
            Canvas(modifier = Modifier.size(32.dp)) {
                val s = size.width
                // Draw a simple bold 'G'
                val path = Path().apply {
                    addOval(Rect(0f, 0f, s, s))
                }
                drawPath(path, Color.Black)
                drawRect(Color.Black, Offset(s/2, s/2 - 2.dp.toPx()), Size(s/2, 4.dp.toPx()))
                // This is a very simplified G, I'll use text for better clarity
            }
            Text("G", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        } else {
            // GitHub Logo Placeholder
            GitHubLogo(Modifier.size(32.dp))
        }
    }
}

@Composable
fun GitHubLogo(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val s = size.width
        val center = Offset(s / 2, s / 2)
        drawCircle(Color.Black, radius = s / 2)
        
        // Simple cat ear shape
        val path = Path().apply {
            moveTo(s * 0.3f, s * 0.4f)
            lineTo(s * 0.35f, s * 0.2f)
            lineTo(s * 0.45f, s * 0.35f)
            lineTo(s * 0.55f, s * 0.35f)
            lineTo(s * 0.65f, s * 0.2f)
            lineTo(s * 0.7f, s * 0.4f)
        }
        drawPath(path, Color(0xFFFFF9F0)) // Light color for the cutout
    }
}
