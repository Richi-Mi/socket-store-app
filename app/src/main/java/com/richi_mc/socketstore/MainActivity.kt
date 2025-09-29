// MainActivity.kt
package com.richi_mc.socketstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.richi_mc.socketstore.interfaz.navigation.AppNavigation // Importamos nuestro navegador
import com.richi_mc.socketstore.ui.theme.SocketStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocketStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Ahora MainActivity solo se encarga de lanzar la navegación
                    AppNavigation()
                }
            }
        }
    }
}