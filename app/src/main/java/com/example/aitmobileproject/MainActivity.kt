package com.example.aitmobileproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aitmobileproject.ui.theme.AITMobileProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AITMobileProjectTheme {
                DashboardScreen()
            }
        }
    }
}

@Composable
fun DashboardScreen() {
    val orange = Color(0xFFEC6C03)
    val lightBeige = Color(0xFFFEF7EC)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .border(3.dp, Color(0xFFA2A2A2), RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Orange Header Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(orange, RoundedCornerShape(26.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Good Morning,",
                        color = Color.Black,
                        fontSize = 25.sp,
                        lineHeight = 30.sp
                    )
                    Text(
                        text = "Max",
                        color = Color.Black,
                        fontSize = 43.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 50.sp
                    )
                }
            }

            // First Row: Two Small Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(lightBeige, RoundedCornerShape(26.dp)))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(lightBeige, RoundedCornerShape(26.dp)))
            }

            // Bottom Section: Tall Card and two small ones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left Tall Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(lightBeige, RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.BottomStart
                ) {
                    // Orange Circle at bottom
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(75.dp)
                            .background(orange, CircleShape)
                    )
                }

                // Right Column
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1.5f).fillMaxWidth().background(lightBeige, RoundedCornerShape(26.dp)))
                    Box(modifier = Modifier.weight(1f).fillMaxWidth().background(lightBeige, RoundedCornerShape(26.dp)))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    AITMobileProjectTheme {
        DashboardScreen()
    }
}
