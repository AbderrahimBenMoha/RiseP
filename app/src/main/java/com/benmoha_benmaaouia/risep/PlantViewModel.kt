package com.benmoha_benmaaouia.risep

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// On adapte le modèle Plant à la structure exacte de ton Firebase JSON
data class FirebasePlantData(
    val soilMoisture: Int = 0,
    val temperature: Double = 0.0,
    val airHumidity: Double = 0.0,
    val lightLevel: Int = 0,
    val waterLevelOk: Boolean = true
)

data class MoistureDataPoint(
    val moisture: Int = 0,
    val timestamp: String = ""
)

data class HistoryEvent(
    val type: String = "",
    val message: String = "",
    val timestamp: String = ""
)

class PlantViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val deviceRef = database.getReference("devices/esp32_plant_01")
    private val telemetryRef = deviceRef.child("telemetry")
    private val commandsRef = deviceRef.child("commands")
    private val configRef = deviceRef.child("config")
    private val historyRef = deviceRef.child("historique")
    private val moistureHistoryRef = deviceRef.child("moistureHistory")

    // État observé par l'UI
    private val _plantData = MutableStateFlow(FirebasePlantData())
    val plantData: StateFlow<FirebasePlantData> = _plantData.asStateFlow()

    private val _isPumpActive = MutableStateFlow(false)
    val isPumpActive: StateFlow<Boolean> = _isPumpActive.asStateFlow()

    private val _autoWatering = MutableStateFlow(true)
    val autoWatering: StateFlow<Boolean> = _autoWatering.asStateFlow()

    private val _historyEvents = MutableStateFlow<List<HistoryEvent>>(emptyList())
    val historyEvents: StateFlow<List<HistoryEvent>> = _historyEvents.asStateFlow()

    private val _moistureHistory = MutableStateFlow<List<MoistureDataPoint>>(emptyList())
    val moistureHistory: StateFlow<List<MoistureDataPoint>> = _moistureHistory.asStateFlow()

    init {
        startListeningToTelemetry()
        startListeningToCommands()
        startListeningToConfig()
        startListeningToHistory()
        startListeningToMoistureHistory()
    }

    private fun startListeningToTelemetry() {
        telemetryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase map directement le JSON dans notre classe Kotlin
                val data = snapshot.getValue(FirebasePlantData::class.java)
                if (data != null) {
                    _plantData.value = data
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur (ex: log)
            }
        })
    }

    private fun startListeningToCommands() {
        commandsRef.child("pumpActive").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isActive = snapshot.getValue(Boolean::class.java) ?: false
                _isPumpActive.value = isActive
            }
            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })
    }

    // Fonction appelée par le bouton "Arroser" de l'UI
    fun triggerManualWatering() {
        commandsRef.child("pumpActive").setValue(true)
    }

    fun setAutoWatering(enabled: Boolean) {
        configRef.child("autoWatering").setValue(enabled)
    }

    private fun startListeningToConfig() {
        configRef.child("autoWatering").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isAuto = snapshot.getValue(Boolean::class.java) ?: true
                _autoWatering.value = isAuto
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun startListeningToHistory() {
        historyRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventsList = mutableListOf<HistoryEvent>()
                for (childSnapshot in snapshot.children) {
                    val event = childSnapshot.getValue(HistoryEvent::class.java)
                    if (event != null) {
                        eventsList.add(event)
                    }
                }
                // On trie par date décroissante (le plus récent en haut)
                _historyEvents.value = eventsList.sortedByDescending { it.timestamp }
            }
            override fun onCancelled(error: DatabaseError) {
                // Gestion erreur
            }
        })
    }

    private fun startListeningToMoistureHistory() {
        // L'ESP32 envoie 1 point / 30s. Pour afficher 24h, il faut 2880 points (24*60*2).
        moistureHistoryRef.limitToLast(2880).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataPoints = mutableListOf<MoistureDataPoint>()
                for (childSnapshot in snapshot.children) {
                    val point = childSnapshot.getValue(MoistureDataPoint::class.java)
                    if (point != null) {
                        dataPoints.add(point)
                    }
                }
                // On garde l'ordre chronologique pour le graphique (plus ancien à gauche, plus récent à droite)
                _moistureHistory.value = dataPoints.sortedBy { it.timestamp }
            }
            override fun onCancelled(error: DatabaseError) {
                // Gestion erreur
            }
        })
    }
}