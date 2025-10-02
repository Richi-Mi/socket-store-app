package com.richi_mc.socketstore.data

import com.richi_mc.socketstore.ui.model.Producto

class ListDataSource {
    private val products = mutableListOf<Producto>()

    companion object {
        private var instance: ListDataSource? = null

        fun getInstance() : ListDataSource {
            if (instance == null)
                instance = ListDataSource()
            return instance!!
        }
    }

    fun addProduct(product: Producto) = products.add(product)

    fun getProducts() = products.toList()

    fun clear() = products.clear()

    fun removeProduct(product: Producto) = products.remove(product)

}