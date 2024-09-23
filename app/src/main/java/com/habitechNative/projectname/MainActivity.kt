package com.habitechNative.projectname


import com.habitechNative.projectname.ui.theme.HabitechNativeTheme
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
//import com.habitechNative.projectname.ui.theme.HabitechTheme


class MainActivity : ComponentActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference()

        setContent {
            HabitechNativeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SensorDataScreen(database)
                }
            }
        }
    }
}

@Composable
fun SensorDataScreen(database: DatabaseReference) {
    // Variables de estado para almacenar los valores de humedad y temperatura
    var humedad by remember { mutableStateOf(0) }
    var temperatura by remember { mutableStateOf(0.0) }

    // Listener para obtener datos en tiempo real desde Firebase
    LaunchedEffect(Unit) {
        val sensorDataRef = database.child("sensorData")

        sensorDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Imprimir el snapshot en el Logcat para depuración
                //Log.d("FirebaseData", "Snapshot: ${snapshot.value}")

                // Obtener los valores del snapshot
                humedad = snapshot.child("humedad").getValue(Int::class.java) ?: 0
                temperatura = snapshot.child("temperatura").getValue(Double::class.java) ?: 0.0
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("FirebaseData", "Error al leer los datos", error.toException())
            }
        })
    }

    // Composición de la interfaz de usuario con los datos de humedad y temperatura
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Humedad: $humedad")
        Text(text = "Temperatura: $temperatura°C")
    }
}
