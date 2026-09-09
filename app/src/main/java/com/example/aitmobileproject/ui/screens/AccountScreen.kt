package com.example.aitmobileproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.data.local.AuthManager
import com.example.aitmobileproject.ui.components.*
import com.example.aitmobileproject.ui.theme.InstrumentSerifFontFamily

@Composable
fun AccountScreen(onLogout: () -> Unit, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val username = authManager.getUsername() ?: "User"
    val provider = authManager.getProvider()

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
            // Profile Header Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Orange)
                    .padding(24.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Text(
                        text = "Active Profile",
                        fontFamily = InstrumentSerifFontFamily,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                    Text(
                        text = username,
                        fontFamily = InstrumentSerifFontFamily,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        lineHeight = 52.sp
                    )
                    Text(
                        text = "Linked via $provider",
                        fontFamily = InstrumentSerifFontFamily,
                        fontSize = 18.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Column(
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ActionTile(
                        subtitle = "Identity",
                        title = "Security\nSettings",
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(26.dp))
                            .background(LightBeige)
                    )
                }

                Column(
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)
                            .clip(RoundedCornerShape(26.dp))
                            .background(LightBeige)
                    )
                    
                    ActionTile(
                        subtitle = "Account",
                        title = "Sign\nOut",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Orange)
                            .clickable { onLogout() }
                    )
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
                   if (actionId == "profile") { /* Stay here */ }
                   else { onNavigateBack() }
                }
            )
        }
    }
}
