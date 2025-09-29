package com.richi_mc.socketstore.interfaz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.richi_mc.socketstore.R
import com.richi_mc.socketstore.ui.theme.SocketStoreTheme
import com.richi_mc.socketstore.viewmodels.CartItemState // <-- Importa la nueva clase de estado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    cartItems: List<CartItemState>,
    onIncreaseQuantity: (CarritoItem) -> Unit,
    onDecreaseQuantity: (CarritoItem) -> Unit,
    onRemoveItem: (CarritoItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tu Carrito") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cartItems) { stateItem ->
                        CarritoItemCard(
                            item = stateItem.product,
                            quantity = stateItem.quantity,
                            onIncrease = { onIncreaseQuantity(stateItem.product) },
                            onDecrease = { onDecreaseQuantity(stateItem.product) },
                            onRemove = { onRemoveItem(stateItem.product) }
                        )
                    }
                }
                Button(
                    onClick = { /* TODO: Navegar a Checkout */ },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Finalizar Compra", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun CarritoItemCard(
    item: CarritoItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$${"%.2f".format(item.costo)}", style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) {
                    Icon(Icons.Default.Remove, contentDescription = "Quitar uno")
                }
                Text("$quantity", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                IconButton(onClick = onIncrease) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir uno")
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar item", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

