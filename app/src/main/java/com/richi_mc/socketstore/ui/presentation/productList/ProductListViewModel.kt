package com.richi_mc.socketstore.ui.presentation.productList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richi_mc.socketstore.data.ListDataSource
import com.richi_mc.socketstore.network.SocketClient
import com.richi_mc.socketstore.ui.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel (
    private val listDataSource: ListDataSource = ListDataSource.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState

    private val socketClient = SocketClient.getInstance()

    init {
        getProducts()
    }

    fun getProducts() {
        try {
            _uiState.value = ProductListUiState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                socketClient.connect()
                val products : List<Producto> = socketClient.getProducts()

                withContext(Dispatchers.Main) {
                    Log.d("SocketClient", "Productos recibidos: $products")
                    _uiState.value = ProductListUiState.Success(products)
                    Log.d("SocketClient", "Cambie el estado a Success")
                }
            }
        }
        catch (e : Exception) {
            _uiState.value = ProductListUiState.Error
        }
    }
    fun itemCount() : Int{
        var count = 0
        listDataSource.getProducts().forEach {
            count += it.quantity
        }
        return count
    }

    fun addProductToCart( producto: Producto ) {
        val item = listDataSource.getProducts().find { it.name == producto.name }
        if(item != null) {
            item.quantity++
        } else {
            producto.quantity++
            listDataSource.addProduct(producto)
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { socketClient.close() }
    }
}