package com.benmoha_benmaaouia.risep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsScreen(onLogout: () -> Unit) {
    // Récupération de l'utilisateur connecté via Firebase
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: "utilisateur@polytech.fr"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Réglages",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Carte unique : Profil et Déconnexion
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Info utilisateur
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(48.dp).background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profil", tint = Color(0xFF388E3C))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Connecté en tant que", fontSize = 12.sp, color = Color.Gray)
                        Text(userEmail, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF0F0F0))
                
                // Bouton Déconnexion
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout() // Renvoie vers l'écran de login
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout, 
                        contentDescription = "Déconnexion", 
                        tint = Color(0xFFD32F2F), 
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Se déconnecter", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Version de l'application en bas de l'écran
        Text(
            text = "RiseP v1.0.0 - Polytech Lyon",
            color = Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
        )
    }
}