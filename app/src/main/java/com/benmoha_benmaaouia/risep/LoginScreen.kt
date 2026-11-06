package com.benmoha_benmaaouia.risep

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    // Couleurs basées sur ta maquette
    val buttonGreen = Color(0xFF388E3C)
    val textColor = Color.White
    val fieldBg = Color(0xFFF2F2F2)

    Box(modifier = Modifier.fillMaxSize()) {
        // Fond avec l'image Monstera
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "Fond d'écran Monstera",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Pour que l'image remplisse bien tout l'écran
        )
        
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

            Spacer(modifier = Modifier.height(32.dp))

            // Bouton de connexion
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        errorMessage = null
                        
                        // Appel réseau à Firebase
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    onLoginSuccess() // Succès : on passe au Dashboard
                                } else {
                                    // Échec : on affiche l'erreur
                                    errorMessage = task.exception?.localizedMessage ?: "Erreur de connexion"
                                }
                            }
                    } else {
                        errorMessage = "Veuillez remplir tous les champs"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading // Désactive le bouton pendant le chargement
            ) {
                Text(
                    text = if (isLoading) "CONNEXION..." else "SE CONNECTER", 
                    fontSize = 16.sp, 
                    fontWeight = FontWeight.Bold
                )
            }

            // Affichage du message d'erreur juste sous le bouton
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}