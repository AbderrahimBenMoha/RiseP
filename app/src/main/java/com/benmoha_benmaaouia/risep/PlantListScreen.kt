package com.benmoha_benmaaouia.risep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlantListScreen(onNavigateToDetails: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Fond gris très clair
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // En-tête : Titre + Bouton Ajout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mes Plantes",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            // Bouton +
            FloatingActionButton(
                onClick = { /* Action pour ajouter une plante plus tard */ },
                containerColor = Color(0xFF388E3C),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Text("+", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Liste des plantes
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(mockPlants) { plant ->
                PlantCard(plant = plant, onNavigateToDetails = onNavigateToDetails)
            }
        }
    }
}

@Composable
fun PlantCard(plant: Plant, onNavigateToDetails: (String) -> Unit) {
    val statusColor = if (plant.isCritical) Color(0xFFE53935) else Color(0xFF4CAF50)
    val indicatorColor = if (plant.moisture == null) Color.LightGray else statusColor

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Bandeau couleur à gauche
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(indicatorColor)
            )
            
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                // Ligne 1 : Nom et point d'état
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌿 ${plant.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(indicatorColor, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ligne 2 : Données ou Statut de synchro
                if (plant.moisture != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "💧 ${plant.moisture}%", 
                            color = if (plant.isCritical) Color.Red else Color(0xFF2196F3),
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "🌡 ${plant.temperature}°C", color = Color.Gray)
                        Text(text = "☀️ ${plant.light}lx", color = Color.Gray)
                    }
                } else {
                    Text(
                        text = "Dernière synchro : ${plant.lastSync}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ligne 3 : Boutons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onNavigateToDetails(plant.id) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Détails", color = Color.Gray)
                    }
                    
                    // Le bouton arroser n'apparaît que si la plante est connectée
                    if (plant.moisture != null) {
                        Button(
                            onClick = { /* Action arrosage manuel */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text("💦 Arroser", color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}