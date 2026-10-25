package com.benmoha_benmaaouia.risep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen(viewModel: PlantViewModel = viewModel()) {
    val historyEvents by viewModel.historyEvents.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Historique",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sélecteur temporel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SegmentButton("24 Heures", isSelected = true, modifier = Modifier.weight(1f))
            SegmentButton("7 Jours", isSelected = false, modifier = Modifier.weight(1f))
            SegmentButton("1 Mois", isSelected = false, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Graphique Humidité du sol
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Humidité du sol (%)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(24.dp))
                
                // Simulation du graphique en barres
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Bar(height = 70.dp, color = Color(0xFFA5D6A7), label = "Hier")
                    Bar(height = 60.dp, color = Color(0xFFA5D6A7), label = "")
                    Bar(height = 50.dp, color = Color(0xFFA5D6A7), label = "Nuit")
                    Bar(height = 20.dp, color = Color(0xFFFFCDD2), label = "") // Seuil critique
                    Bar(height = 90.dp, color = Color(0xFF64B5F6), label = "Ce matin") // Arrosage
                    Bar(height = 75.dp, color = Color(0xFFA5D6A7), label = "")
                    Bar(height = 70.dp, color = Color(0xFFA5D6A7), label = "Maintenant")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Journal d'activités", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(16.dp))

        // Liste des actions
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(historyEvents) { event ->
                val iconData = when (event.type) {
                    "WATERING" -> Triple(Icons.Default.WaterDrop, Color(0xFF2196F3), Color(0xFFE3F2FD))
                    "ALERT" -> Triple(Icons.Default.Warning, Color(0xFFF44336), Color(0xFFFFEBEE))
                    else -> Triple(Icons.Default.Person, Color(0xFF9E9E9E), Color(0xFFF5F5F5))
                }

                TimelineItem(
                    icon = iconData.first,
                    iconColor = iconData.second,
                    bgColor = iconData.third,
                    title = event.message,
                    time = event.timestamp // On affiche tel quel pour commencer
                )
            }
        }
    }
}

// --- Composants réutilisables pour cet écran ---

@Composable
fun SegmentButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(if (isSelected) Color.White else Color.Transparent, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF388E3C) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun Bar(height: Dp, color: Color, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(height)
                .background(color, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (label.isNotEmpty()) {
            Text(label, fontSize = 10.sp, color = Color.Gray)
        } else {
            Text(" ", fontSize = 10.sp) // Espace vide pour garder l'alignement
        }
    }
}

@Composable
fun TimelineItem(icon: ImageVector, iconColor: Color, bgColor: Color, title: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF333333))
            Text(time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}