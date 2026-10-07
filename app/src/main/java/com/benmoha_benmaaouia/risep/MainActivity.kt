package com.benmoha_benmaaouia.risep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.benmoha_benmaaouia.risep.ui.theme.RisePTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RisePTheme {
                LoginScreen(
                    onLoginSuccess = {
                        // Action de connexion à implémenter plus tard
                    }
                )
            }
        }
    }
}