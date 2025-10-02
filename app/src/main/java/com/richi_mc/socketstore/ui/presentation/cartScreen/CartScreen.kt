package com.richi_mc.socketstore.ui.presentation.cartScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExposureNeg1
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.richi_mc.socketstore.ui.model.Producto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit
) {
    val cartViewModel = viewModel<CartViewModel>()
    val cartItems   = cartViewModel.products.collectAsState()
    val payDoIt     = cartViewModel.payDoit.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tu Carrito") }
                )
            }
        ) { paddingValues ->
            PagoExitosoDialog(payDoIt.value, onConfirm = { onNavigateBack() }, onDismiss = { onNavigateBack() })

            if (cartItems.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cartItems.value) { stateItem ->
                        CarritoItemCard(
                            item = stateItem,
                            onDelete = {
                                cartViewModel.removeProduct(it)
                            }
                        )
                    }
                    item {
                        Button(
                            onClick = { cartViewModel.doPayProducts() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Finalizar Compra", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }

@Composable
fun CarritoItemCard(
    item: Producto,
    onDelete: (product: Producto) -> Unit
) {
    val quantity = remember { mutableIntStateOf(item.quantity) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("$${"%.2f".format(item.price * item.quantity)}", style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    quantity.intValue--
                    item.quantity = quantity.intValue
                }) {
                    Icon(Icons.Default.Clear, contentDescription = "Quitar uno")
                }
                Text(
                    "${quantity.intValue}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    quantity.intValue++
                    item.quantity = quantity.intValue
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir uno")
                }
                IconButton(onClick = { onDelete(item) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
@Composable
fun PagoExitosoDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            // La acción que se ejecuta al intentar salir del diálogo (por ejemplo, pulsando fuera)
            onDismissRequest = onDismiss,

            // Título del diálogo
            title = {
                Text(text = "¡Pago realizado!")
            },

            // Contenido/mensaje principal
            text = {
                Text("Tu compra se ha completado con éxito.")
            },

            // Botones de acción
            confirmButton = {
                Button(
                    onClick = onConfirm // Llama a la acción para volver atrás
                ) {
                    Text("Volver atrás")
                }
            }
            // No incluimos 'dismissButton' porque solo queremos un botón de acción.
        )
    }
}