package com.richi_mc.socketstore.ui.presentation.cartScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richi_mc.socketstore.data.ListDataSource
import com.richi_mc.socketstore.network.SocketClient
import com.richi_mc.socketstore.ui.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class CartViewModel(
    val listDataSource: ListDataSource = ListDataSource.getInstance(),
    val socketClient: SocketClient = SocketClient.getInstance()
) : ViewModel() {

    private val _products = MutableStateFlow(listDataSource.getProducts())
    val products = _products

    val payDoit = MutableStateFlow(false)

    fun removeProduct( producto: Producto ) {
        listDataSource.removeProduct(producto)
        _products.value = listDataSource.getProducts().toList()
    }

    fun doPayProducts() {
        val array = JSONArray()

        _products.value.forEach {
            array.put(it.parseToJSON())
        }

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("CartViewModel", "Realizando pago... ${array.toString()}")
            socketClient.doPayment(array.toString())

            withContext(Dispatchers.Main) {
                listDataSource.clear()
                _products.value = listDataSource.getProducts().toList()

                payDoit.value = true
            }
        }
    }
}