package com.benmoha_benmaaouia.risep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(plantId: String, onNavigateBack: () -> Unit) {
    // Récupération de la plante depuis nos fausses données
    val plant = mockPlants.find { it.id == plantId } ?: return
    var autoWatering by remember { mutableStateOf(true) }

    val isCritical = plant.isCritical
    val mainColor = if (isCritical) Color(0xFFFF5252) else Color(0xFF4CAF50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 1. En-tête (Retour, Titre, Réglages)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
            Text(
                text = plant.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            IconButton(
                onClick = { /* Action Réglages */ },
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Réglages")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Jauge circulaire d'humidité
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { (plant.moisture ?: 0) / 100f },
                modifier = Modifier.size(160.dp),
                color = mainColor,
                strokeWidth = 14.dp,
                trackColor = Color(0xFFE0E0E0),
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${plant.moisture}%",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainColor
                )
                Text("HUMIDITÉ SOL", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Badge critique
        if (isCritical) {
            Surface(
                color = Color(0xFFFFEBEE),
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
            ) {
                Text(
                    text = "⚠️ CRITIQUE (Seuil: 25%)",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Grille de statistiques (2x2)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(modifier = Modifier.weight(1f), title = "Température", value = "${plant.temperature} °C", icon = "🌡")
            StatCard(modifier = Modifier.weight(1f), title = "Humidité air", value = "48 %", icon = "💧") // Valeur fixe pour l'exemple
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(modifier = Modifier.weight(1f), title = "Luminosité", value = "${plant.light} lx", icon = "☀️")
            StatCard(modifier = Modifier.weight(1f), title = "Statut LED", value = if (isCritical) "Rouge" else "Vert", icon = "💡", valueColor = mainColor)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Bouton Arroser
        Button(
            onClick = { /* Action arrosage manuel via Firebase plus tard */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("💧 💦 Arroser maintenant (50ml)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Carte Arrosage Automatique
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Arrosage Automatique", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Géré par capteurs intelligents", color = Color.Gray, fontSize = 12.sp)
                    }
                    Switch(
                        checked = autoWatering,
                        onCheckedChange = { autoWatering = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF388E3C))
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))
                Text("🕒 Dernier arrosage : Aujourd'hui à 07:15", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

// Composant réutilisable pour les petites tuiles de statistiques
@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, icon: String, valueColor: Color = Color.Black) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, color = Color.Gray, fontSize = 12.sp)
                Text(icon, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = valueColor)
        }
    }
}