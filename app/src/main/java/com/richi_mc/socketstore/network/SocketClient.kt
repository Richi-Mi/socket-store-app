package com.richi_mc.socketstore.network

import android.util.Log
import com.richi_mc.socketstore.ui.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.Scanner

class SocketClient {
    private val HOST = "192.168.1.66"

    private lateinit var socket : Socket
    private lateinit var pw     : PrintWriter
    private lateinit var scan   : Scanner

    companion object {
        private var instance: SocketClient? = null
        fun getInstance() : SocketClient{
            if (instance == null) {
                instance = SocketClient()
            }

            return instance!!
        }
    }

    suspend fun connect() {

        socket = Socket(HOST, 8090)
        Log.d("SocketClient", "Conectado al servidor ${socket.inetAddress.hostName}")
        pw      = PrintWriter(OutputStreamWriter(socket.outputStream))
        scan    = Scanner(InputStreamReader(socket.inputStream))

    }

    suspend fun getProducts() : List<Producto> {
        Log.d("SocketClient", "Recibiendo productos...")
        val response = scan.nextLine()

        val products = mutableListOf<Producto>()
        val jsonArr = JSONArray(response)

        for( i in 0 until jsonArr.length()) {
            val item = jsonArr.getJSONObject(i)
            val p = Producto(item["name"] as String, item["description"] as String, item["price"] as Double, item["stock"] as Int)
            products.add(p)
        }
        return products
    }
    suspend fun close() {
        withContext(Dispatchers.IO) {
            try {
                pw?.close()
                scan?.close()
                socket?.close()
                Log.d("SocketClient", "Socket cerrado.")
            } catch (e: Exception) {
                Log.e("SocketClient", "Error al cerrar socket: ${e.message}", e)
            }
        }
    }
    suspend fun doPayment( data : String ) {
        pw.println(data)
        pw.flush()
        Log.d("SocketClient", "Realizando pago...")
    }
}