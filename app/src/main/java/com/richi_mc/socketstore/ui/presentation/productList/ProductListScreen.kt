package com.richi_mc.socketstore.ui.presentation.productList

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.richi_mc.socketstore.ui.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(onNavigateToCart: () -> Unit) {
    val productListViewModel = viewModel<ProductListViewModel>()
    val uiState = productListViewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var itemCount by remember { mutableIntStateOf(productListViewModel.itemCount()) }
    val context = LocalContext.current

    when (uiState.value) {
        ProductListUiState.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Cargando...")
                LinearProgressIndicator()
            }
        }

        is ProductListUiState.Success -> {
            val products = (uiState.value as ProductListUiState.Success).products
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            TextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Buscar por tipo...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Ícono de búsqueda"
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent
                                )
                            )
                        },
                        actions = {
                            BadgedBox(badge = {
                                if (itemCount > 0) {
                                    Badge { Text("$itemCount") }
                                }
                            }) {
                                IconButton(onClick = onNavigateToCart) {
                                    Icon(
                                        Icons.Default.ShoppingCart,
                                        contentDescription = "Ver Carrito"
                                    )
                                }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = {
                                itemCount++
                                if(!(product.stock - itemCount < 0)) {
                                    productListViewModel.addProductToCart(product)
                                }
                                else {
                                    Toast.makeText(context, "No hay mas ${product.name} disponibles", Toast.LENGTH_LONG).show()
                                    itemCount--
                                }
                            }
                        )
                    }
                }
            }
        }

        ProductListUiState.Error -> {
            Text("Error al cargar la lista de productos")
        }
    }
}

@Composable
fun ProductCard(
    product: Producto,
    onAddToCart: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Cantidad disponible: ${product.stock}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.desciption,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(onClick = onAddToCart) {
                    Text("Agregar")
                }
            }
        }
    }
}