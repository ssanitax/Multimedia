package com.example.listacompra.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listacompra.R
import com.example.listacompra.data.Producto
import com.example.listacompra.ui.theme.ListaCompraTheme

@Composable
fun ListaCompraScreen(
    viewModel: ListaCompraViewModel = viewModel()
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    ListaCompraContenido(
        productos = viewModel.productos,
        nombre = nombre,
        onNombreChange = { nombre = it },
        onAnadir = {
            viewModel.agregar(nombre)
            nombre = ""
        },
        onAlternar = viewModel::alternarComprado,
        onEliminar = viewModel::eliminar
    )
}

@Composable
private fun ListaCompraContenido(
    productos: List<Producto>,
    nombre: String,
    onNombreChange: (String) -> Unit,
    onAnadir: () -> Unit,
    onAlternar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    val margenPantalla = dimensionResource(R.dimen.margen_pantalla)
    val espacioControles = dimensionResource(R.dimen.espacio_entre_controles)
    val pendientes = productos.count { !it.comprado }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = margenPantalla)
        ) {
            Text(
                text = pluralStringResource(
                    R.plurals.productos_pendientes,
                    pendientes,
                    pendientes
                ),
                modifier = Modifier
                    .padding(top = margenPantalla)
                    .semantics { heading() },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = espacioControles),
                horizontalArrangement = Arrangement.spacedBy(espacioControles),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = onNombreChange,
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.nombre_producto)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onAnadir() })
                )
                Button(
                    onClick = onAnadir,
                    enabled = nombre.isNotBlank()
                ) {
                    Text(stringResource(R.string.anadir))
                }
            }
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.lista_vacia),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(vertical = margenPantalla),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.espacio_entre_productos)
                    )
                ) {
                    items(
                        items = productos,
                        key = { it.id }
                    ) { producto ->
                        TarjetaProducto(
                            producto = producto,
                            onAlternar = { onAlternar(producto) },
                            onEliminar = { onEliminar(producto) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaProducto(
    producto: Producto,
    onAlternar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (producto.comprado) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(R.dimen.altura_minima_tarjeta))
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.margen_tarjeta))
                    .padding(end = dimensionResource(R.dimen.espacio_acciones)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = producto.nombre,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onAlternar),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (producto.comprado) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (producto.comprado) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(
                            R.string.cd_eliminar_producto,
                            producto.nombre
                        ),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            IconButton(
                onClick = onAlternar,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (producto.comprado) {
                        Icons.Filled.CheckCircle
                    } else {
                        Icons.Outlined.RadioButtonUnchecked
                    },
                    contentDescription = stringResource(
                        if (producto.comprado) {
                            R.string.cd_producto_comprado
                        } else {
                            R.string.cd_producto_pendiente
                        },
                        producto.nombre
                    ),
                    tint = if (producto.comprado) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Composable
private fun ListaCompraPreviewClaro() {
    ListaCompraTheme(darkTheme = false, dynamicColor = false) {
        ListaCompraContenido(
            productos = productosDeEjemplo(),
            nombre = "",
            onNombreChange = {},
            onAnadir = {},
            onAlternar = {},
            onEliminar = {}
        )
    }
}

@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaCompraPreviewOscuro() {
    ListaCompraTheme(darkTheme = true, dynamicColor = false) {
        ListaCompraContenido(
            productos = productosDeEjemplo(),
            nombre = "",
            onNombreChange = {},
            onAnadir = {},
            onAlternar = {},
            onEliminar = {}
        )
    }
}

@Preview(name = "Lista vacia", showBackground = true)
@Composable
private fun ListaCompraPreviewVacia() {
    ListaCompraTheme(darkTheme = false, dynamicColor = false) {
        ListaCompraContenido(
            productos = emptyList(),
            nombre = "",
            onNombreChange = {},
            onAnadir = {},
            onAlternar = {},
            onEliminar = {}
        )
    }
}

@Composable
private fun productosDeEjemplo(): List<Producto> {
    return listOf(
        Producto(
            id = 1,
            nombre = stringResource(R.string.producto_ejemplo_leche)
        ),
        Producto(
            id = 2,
            nombre = stringResource(R.string.producto_ejemplo_pan),
            comprado = true
        ),
        Producto(
            id = 3,
            nombre = stringResource(R.string.producto_ejemplo_huevos)
        )
    )
}
