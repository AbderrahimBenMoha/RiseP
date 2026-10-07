package com.benmoha_benmaaouia.risep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("ahdi.ben-maaouia@polytech.fr") }
    var password by remember { mutableStateOf("...............") }

    // Couleurs basées sur ta maquette
    val darkGreenBg = Color(0xFF1E2E22)
    val buttonGreen = Color(0xFF388E3C)
    val textColor = Color.White
    val fieldBg = Color(0xFFF2F2F2)

    Box(modifier = Modifier.fillMaxSize()) {
        // Fond (À remplacer par R.drawable.ton_image_de_fond)
        Box(modifier = Modifier.fillMaxSize().background(darkGreenBg))
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            // Logo "Placeholder" circulaire
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(50))
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Titres
            Text("RiseP", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(
                "Système d'Arrosage Connecté", 
                fontSize = 14.sp, 
                color = textColor.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 60.dp)
            )

            // Champ Email
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Email", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = fieldBg,
                        unfocusedContainerColor = fieldBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Champ Mot de passe
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Mot de passe", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = fieldBg,
                        unfocusedContainerColor = fieldBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
            
            // Mot de passe oublié
            Text(
                "Mot de passe oublié ?",
                color = Color(0xFF64B5F6),
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bouton de connexion
            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SE CONNECTER", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Lien Inscription
            Row(modifier = Modifier.padding(bottom = 32.dp)) {
                Text("Pas encore de compte ? ", color = textColor.copy(alpha = 0.8f), fontSize = 14.sp)
                Text("S'inscrire", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}