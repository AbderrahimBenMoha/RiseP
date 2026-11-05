package com.benmoha_benmaaouia.risep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
    val moistureHistory by viewModel.moistureHistory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Titre de la page (Le sélecteur a été supprimé pour ne garder que 24h implicitement)
        Text(
            text = "Historique (Dernières 24h)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Graphique Humidité du sol
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Humidité du sol", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(24.dp))
                
                if (moistureHistory.isEmpty()) {
                    Text(
                        "En attente des données du capteur...",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                } else {
                    // On découpe les dernières 24h en 7 tranches de 4h
                    val displayData = generate24hBuckets(moistureHistory)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        displayData.forEach { (timeLabel, avgMoisture) ->
                            // Normalisation (0 à 3000 environ selon le capteur analogique)
                            val normalizedHeight = (avgMoisture / 3000f).coerceIn(0f, 1f) * 100
                            
                            // Si on n'a pas de données (0f), on met une petite barre grise de 4dp
                            val barHeight = if (avgMoisture == 0f) 4.dp else normalizedHeight.dp
                            
                            val color = when {
                                avgMoisture == 0f -> Color(0xFFEEEEEE) // Gris : Pas de données
                                avgMoisture < 1000 -> Color(0xFFFFCDD2) // Rouge : Sol sec
                                else -> Color(0xFFA5D6A7) // Vert : Ok
                            }
                            
                            Bar(height = barHeight, color = color, label = timeLabel)
                        }
                    }
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
                // Selon le code de l'ESP32, les événements ont ce format :
                // "Arrosage automatique active (Sol sec)" ou "Arrosage automatique termine"
                val (icon, iconColor, bgColor) = when {
                    event.message.contains("termine") -> Triple(Icons.Default.WaterDrop, Color(0xFF2196F3), Color(0xFFE3F2FD)) // Bleu pour arrosage terminé
                    event.message.contains("active") -> Triple(Icons.Default.Warning, Color(0xFFF44336), Color(0xFFFFEBEE)) // Rouge pour alerte sol sec
                    else -> Triple(Icons.Default.Person, Color(0xFF9E9E9E), Color(0xFFF5F5F5)) // Gris par défaut
                }

                TimelineItem(
                    icon = icon,
                    iconColor = iconColor,
                    bgColor = bgColor,
                    title = event.message,
                    time = formatTime(event.timestamp, "dd/MM à HH:mm:ss") // Heure locale formatée
                )
            }
        }
    }
}

// --- Composants réutilisables pour cet écran ---



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

// Fonction utilitaire pour convertir "2026-11-06T14:30:00Z" (UTC) en heure locale lisible
fun formatTime(utcTimestamp: String, pattern: String): String {
    return try {
        val instant = Instant.parse(utcTimestamp)
        // On force le fuseau horaire français (Paris) car l'émulateur Android est souvent en UTC !
        val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("Europe/Paris"))
        formatter.format(instant)
    } catch (_: Exception) {
        utcTimestamp // Fallback si le format est inattendu
    }
}

// Fonction pour découper l'historique en 7 tranches sur 24 heures
fun generate24hBuckets(history: List<MoistureDataPoint>): List<Pair<String, Float>> {
    val now = Instant.now()
    val buckets = mutableListOf<Pair<String, Float>>()
    
    // 7 barres pour 24h (Toutes les 4h environ : -24h, -20h, -16h, -12h, -8h, -4h, Maintenant)
    for (i in 6 downTo 0) {
        val bucketStart = now.minusSeconds((i * 4 * 3600).toLong())
        val bucketEnd = now.minusSeconds(((i - 1) * 4 * 3600).toLong())
        
        val pointsInBucket = history.filter { 
            try {
                val t = Instant.parse(it.timestamp)
                (t.isAfter(bucketStart) || t == bucketStart) && t.isBefore(bucketEnd)
            } catch (_: Exception) {
                false
            }
        }
        
        val avgMoisture = if (pointsInBucket.isNotEmpty()) {
            pointsInBucket.map { it.moisture }.average().toFloat()
        } else {
            0f
        }
        
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Europe/Paris"))
        val timeLabel = formatter.format(bucketStart)
        
        buckets.add(Pair(timeLabel, avgMoisture))
    }
    return buckets
}