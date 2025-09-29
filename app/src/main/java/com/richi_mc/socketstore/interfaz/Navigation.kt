package com.richi_mc.socketstore.interfaz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.richi_mc.socketstore.R
import com.richi_mc.socketstore.interfaz.CartScreen
import com.richi_mc.socketstore.interfaz.CarritoItem
import com.richi_mc.socketstore.interfaz.ProductListScreen
import com.richi_mc.socketstore.viewmodels.CartViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val cartItems by cartViewModel.cartItems.collectAsState()

    val productList = listOf(
        CarritoItem("Laptop Pro", "Potencia para profesionales.", 1299.99, R.drawable.ic_shopping_bag_filled, "Electrónica"),
        CarritoItem("Auriculares Bluetooth", "Sonido envolvente.", 49.99, R.drawable.ic_shopping_bag_filled, "Audio"),
        CarritoItem("Teclado Mecánico", "Switches marrones, RGB.", 75.50, R.drawable.ic_shopping_bag_filled, "Periféricos"),
        CarritoItem("Mouse Gamer", "Alta precisión, 6 botones.", 29.00, R.drawable.ic_shopping_bag_filled, "Periféricos")
    )

    NavHost(
        navController = navController,
        startDestination = "productList"
    ) {
        composable("productList") {
            ProductListScreen(
                onNavigateToCart = { navController.navigate("cart") },
                onAddToCart = { item -> cartViewModel.addItemToCart(item) },
                cartItemCount = cartItems.sumOf { it.quantity }, // Sumamos cantidades
                productList = productList
            )
        }
        composable("cart") {
            CartScreen(
                onNavigateBack = { navController.popBackStack() },
                cartItems = cartItems, // Pasamos la lista de CartItemState
                // --- Pasamos las acciones del ViewModel a la pantalla ---
                onIncreaseQuantity = { item -> cartViewModel.increaseQuantity(item) },
                onDecreaseQuantity = { item -> cartViewModel.decreaseQuantity(item) },
                onRemoveItem = { item -> cartViewModel.removeItemFromCart(item) }
            )
        }
    }
}