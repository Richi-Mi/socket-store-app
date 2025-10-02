package com.richi_mc.socketstore.ui

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.richi_mc.socketstore.R
import com.richi_mc.socketstore.ui.presentation.cartScreen.CartScreen
import com.richi_mc.socketstore.ui.presentation.productList.ProductListScreen

@Composable
fun SocketStoreNavigation(activity : Activity) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "productList"
    ) {
        composable("productList") {
            ProductListScreen(onNavigateToCart = { navController.navigate("cart") })
        }
        composable("cart") {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                    activity.finish()
                }
            )
        }
    }
}