package com.benmoha_benmaaouia.risep

// 1. La structure de la donnée
data class Plant(
    val id: String,
    val name: String,
    val moisture: Int?, // Nullable si le capteur est déconnecté
    val temperature: Double?,
    val light: Int?,
    val isCritical: Boolean,
    val lastSync: String? = null
)

// 2. Nos fausses données basées sur ta maquette Figma
val mockPlants = listOf(
    Plant(
        id = "esp_001",
        name = "Ficus Salon",
        moisture = 42,
        temperature = 22.4,
        light = 650,
        isCritical = false
    ),
    Plant(
        id = "esp_002",
        name = "Monstera Bureau",
        moisture = 18,
        temperature = 26.1,
        light = 920,
        isCritical = true // Sol très sec !
    ),
    Plant(
        id = "esp_003",
        name = "Cactus Entrée",
        moisture = null,
        temperature = null,
        light = null,
        isCritical = false,
        lastSync = "Il y a 2h"
    )
)