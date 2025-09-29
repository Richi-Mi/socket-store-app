package com.richi_mc.socketstore.interfaz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.richi_mc.socketstore.R
import com.richi_mc.socketstore.ui.theme.SocketStoreTheme

data class CarritoItem(
    val nombre: String,
    val descripcion: String,
    val costo: Double,
    val imagenResId: Int,
    val categoria: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateToCart: () -> Unit,
    onAddToCart: (CarritoItem) -> Unit,
    cartItemCount: Int,
    productList: List<CarritoItem>
) {
    var searchText by remember { mutableStateOf("") }

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
                            Icon(Icons.Default.Search, contentDescription = "Ícono de búsqueda")
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        )
                    )
                },
                actions = {
                    BadgedBox(badge = { if (cartItemCount > 0) { Badge { Text("$cartItemCount") } } }) {
                        IconButton(onClick = onNavigateToCart) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Ver Carrito")
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
            items(productList) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { onAddToCart(product) }
                )
            }
        }
    }
}

// --- FUNCIÓN 'ProductCard' QUE FALTABA ---
@Composable
fun ProductCard(
    product: CarritoItem,
    onAddToCart: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = product.imagenResId),
                contentDescription = product.nombre,
                modifier = Modifier
                    .size(60.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = product.categoria,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.descripcion,
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
                        text = "$${"%.2f".format(product.costo)}",
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
}

// --- VISTA PREVIA QUE FALTABA ---
@Preview(showBackground = true)
@Composable
fun PreviewProductListScreen() {
    val productosDeEjemplo = listOf(
        CarritoItem("Laptop Pro", "Potencia y rendimiento.", 1299.99, R.drawable.ic_shopping_bag_filled, "Electrónica"),
        CarritoItem("Auriculares Pro", "Cancelación de ruido activa.", 299.99, R.drawable.ic_shopping_bag_filled, "Audio")
    )
    SocketStoreTheme {
        ProductListScreen(
            onNavigateToCart = {},
            onAddToCart = {},
            cartItemCount = 2,
            productList = productosDeEjemplo
        )
    }
}