package com.benmoha_benmaaouia.risep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    
    // On observe la route actuelle pour savoir sur quel écran on se trouve
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // On n'affiche le menu en bas QUE sur le Dashboard, l'Historique et les Réglages
    val showBottomBar = currentRoute in listOf("dashboard", "history", "settings")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color(0xFF388E3C)
                ) {
                    // Onglet 1 : Plantes (Dashboard)
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Eco, contentDescription = "Plantes") },
                        label = { Text("Plantes") },
                        selected = currentRoute == "dashboard",
                        onClick = {
                            navController.navigate("dashboard") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF388E3C),
                            selectedTextColor = Color(0xFF388E3C),
                            indicatorColor = Color(0xFFE8F5E9)
                        )
                    )
                    
                    // Onglet 2 : Historique
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.BarChart, contentDescription = "Historique") },
                        label = { Text("Historique") },
                        selected = currentRoute == "history",
                        onClick = {
                            navController.navigate("history") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF388E3C),
                            selectedTextColor = Color(0xFF388E3C),
                            indicatorColor = Color(0xFFE8F5E9)
                        )
                    )

                    // Onglet 3 : Réglages
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Réglages") },
                        label = { Text("Réglages") },
                        selected = currentRoute == "settings",
                        onClick = {
                            navController.navigate("settings") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF388E3C),
                            selectedTextColor = Color(0xFF388E3C),
                            indicatorColor = Color(0xFFE8F5E9)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // Le NavHost est englobé par le Scaffold et respecte son "innerPadding" (la hauteur du menu)
        NavHost(
            navController = navController, 
            startDestination = "login",
            modifier = Modifier.padding(innerPadding) 
        ) {
            
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            
            composable("dashboard") {
                PlantListScreen(
                    onNavigateToDetails = { 
                        navController.navigate("plant_detail")
                    }
                )
            }
            
            composable("plant_detail") {
                PlantDetailScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable("history") {
                HistoryScreen()
            }
            
            // 5. La route des réglages
            composable("settings") {
                SettingsScreen(
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true } // On vide tout l'historique de navigation
                        }
                    }
                )
            }
        }
    }
}
