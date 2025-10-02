package com.richi_mc.socketstore.ui.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import org.json.JSONObject

data class Producto(
    val name        : String,
    val desciption  : String,
    val price       : Double,
    val stock       : Int
) {
    var quantity    : Int = 0

    fun parseToJSON() : JSONObject {
        val product = JSONObject()

        product.put("name", name)
        product.put("description", desciption)
        product.put("price", price)
        product.put("stock", stock)
        product.put("quantity", quantity)

        return product
    }
}
