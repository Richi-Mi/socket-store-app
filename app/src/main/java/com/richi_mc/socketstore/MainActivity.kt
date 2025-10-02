package com.richi_mc.socketstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.richi_mc.socketstore.network.SocketClient
import com.richi_mc.socketstore.ui.SocketStoreNavigation
import com.richi_mc.socketstore.ui.theme.SocketStoreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            SocketStoreTheme {
                SocketStoreNavigation(this@MainActivity)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sc = SocketClient.getInstance()

        lifecycleScope.launch {
            sc.close()
        }
    }
}