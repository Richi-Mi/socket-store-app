package com.richi_mc.socketstore.ui.presentation.productList

import com.richi_mc.socketstore.ui.model.Producto

sealed class ProductListUiState {
    object Loading : ProductListUiState()
    data class Success( val products : List<Producto>) : ProductListUiState()
    object Error : ProductListUiState()
}