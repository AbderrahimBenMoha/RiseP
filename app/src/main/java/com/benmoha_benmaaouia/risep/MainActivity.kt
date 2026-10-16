package com.benmoha_benmaaouia.risep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.benmoha_benmaaouia.risep.ui.theme.RisePTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RisePTheme {
                RisePApp()
            }
        }
    }
}

@Composable
fun RisePApp() {
    val navController = rememberNavController()

    // C'est ici que la magie opère : startDestination DOIT être "login"
    NavHost(navController = navController, startDestination = "login") {
        
        // 1. La route de l'écran de connexion
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        
        // 2. La route de la liste des plantes (ton Dashboard)
        composable("dashboard") {
            PlantListScreen(
                onNavigateToDetails = { plantId ->
                    // Plus tard, on naviguera vers la page spécifique de la plante avec son ID
                    navController.navigate("plant_detail/$plantId")
                }
            )
        }
        
        // 3. La route de détail de la plante
        composable("plant_detail/{plantId}") { backStackEntry ->
            // On extrait l'ID de la plante depuis l'URL de navigation
            val plantId = backStackEntry.arguments?.getString("plantId") ?: return@composable
            
            PlantDetailScreen(
                plantId = plantId,
                onNavigateBack = { navController.popBackStack() } // Action pour le bouton "<"
            )
        }
    }
}
