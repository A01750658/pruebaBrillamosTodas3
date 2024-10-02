package mx.tec.pruebabrillamostodas3.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mx.tec.pruebabrillamostodas3.viewmodel.BTVM
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign

/**
 * @author Santiago Chevez
 * @author Alan Vega
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tienda(viewModel: BTVM, modifier: Modifier, navController: NavHostController) {
    val estadoListaProducto = viewModel.estadoListaProducto.collectAsState()
    val estadoCantidad by viewModel.estadoCantidadProductosModelo.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    val estadoCategorias = viewModel.estadoCategorias.collectAsState() // Obtenemos las categorías
    var expanded by remember { mutableStateOf(false) } // Estado para mostrar el DropdownMenu
    var selectedCategoria by remember { mutableStateOf("Todas") } // Categoría seleccionada

    val configuration = LocalConfiguration.current
    val screenOrientation = configuration.orientation

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            // Encabezado de la tienda y filtro de categoría
            if (screenOrientation == 1) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(100.dp)
                        .fillMaxWidth(),
                )
                Titulo(titulo = "Catálogo", color = MaterialTheme.colorScheme.secondaryContainer, fontSize = 50)

                // Menú desplegable de categorías
                Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Filtrar por: $selectedCategoria")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas") }, // Corrección: se pasa el Text como lambda
                            onClick = {
                                selectedCategoria = "Todas"
                                viewModel.resetListaFiltradaPorCategoria()
                                expanded = false
                            }
                        )
                        estadoCategorias.value.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = {
                                    selectedCategoria = categoria
                                    viewModel.setListaFiltradaPorCategoria(categoria) // Aplicar el filtro
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(6.dp).fillMaxWidth())
                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)
                Spacer(modifier = Modifier.padding(8.dp).fillMaxWidth())
            }

            // Lista de productos filtrada
            LazyColumn {
                items((estadoListaProducto.value.size + 1) / 2) { rowIndex ->
                    LazyRow(Modifier.fillMaxWidth()) {
                        for (colIndex in 0..1) {
                            val index = rowIndex * 2 + colIndex
                            if (index < estadoListaProducto.value.size) {
                                item {
                                    BotonProducto(
                                        onClick = {
                                            viewModel.setEstadoSeleccionado(index)
                                            showMenu = true
                                        },
                                        imagen = estadoListaProducto.value[index].imagen,
                                        nombre = estadoListaProducto.value[index].nombre,
                                        precio_n = estadoListaProducto.value[index].precio_normal,
                                        precio_r = estadoListaProducto.value[index].precio_rebajado,
                                        rebaja = estadoListaProducto.value[index].rebaja,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Indicador de carga si la lista está vacía
            if (estadoListaProducto.value.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

        // Botón flotante para navegar al carrito
        FloatingActionButton(
            onClick = { navController.navigate("Carrito") },
            containerColor = MaterialTheme.colorScheme.tertiary,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
        ) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Generar")
        }

        // Modal para mostrar el detalle del producto
        if (showMenu) {
            ModalBottomSheet(onDismissRequest = { showMenu = false }) {
                Producto(viewModel, modifier, navController)
            }
        }
    }
}
