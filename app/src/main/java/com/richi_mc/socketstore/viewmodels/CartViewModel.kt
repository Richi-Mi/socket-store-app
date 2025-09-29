package com.richi_mc.socketstore.viewmodels

import androidx.lifecycle.ViewModel
import com.richi_mc.socketstore.interfaz.CarritoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. Creamos una nueva clase para representar el estado de un item EN el carrito
data class CartItemState(
    val product: CarritoItem,
    val quantity: Int
)

class CartViewModel : ViewModel() {

    // 2. Nuestro estado ahora será una lista de 'CartItemState'
    private val _cartItems = MutableStateFlow<List<CartItemState>>(emptyList())
    val cartItems: StateFlow<List<CartItemState>> = _cartItems.asStateFlow()

    // 3. Modificamos la lógica para manejar cantidades
    fun addItemToCart(item: CarritoItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.nombre == item.nombre }

            if (existingItem != null) {
                // Si el item ya existe, incrementamos su cantidad
                currentList.map { cartItem ->
                    if (cartItem.product.nombre == item.nombre) {
                        cartItem.copy(quantity = cartItem.quantity + 1)
                    } else {
                        cartItem
                    }
                }
            } else {
                // Si es un item nuevo, lo añadimos con cantidad 1
                currentList + CartItemState(product = item, quantity = 1)
            }
        }
    }

    // 4. Añadimos funciones para los botones '+' y '-'
    fun increaseQuantity(item: CarritoItem) {
        _cartItems.update { currentList ->
            currentList.map { cartItem ->
                if (cartItem.product.nombre == item.nombre) {
                    cartItem.copy(quantity = cartItem.quantity + 1)
                } else {
                    cartItem
                }
            }
        }
    }

    fun decreaseQuantity(item: CarritoItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.nombre == item.nombre }
            if (existingItem != null && existingItem.quantity > 1) {
                // Si la cantidad es mayor a 1, la reducimos
                currentList.map { cartItem ->
                    if (cartItem.product.nombre == item.nombre) {
                        cartItem.copy(quantity = cartItem.quantity - 1)
                    } else {
                        cartItem
                    }
                }
            } else {
                // Si la cantidad es 1, eliminamos el producto del carrito
                currentList.filterNot { it.product.nombre == item.nombre }
            }
        }
    }

    fun removeItemFromCart(item: CarritoItem) {
        _cartItems.update { currentList ->
            currentList.filterNot { it.product.nombre == item.nombre }
        }
    }
}