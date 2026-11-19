package com.example.listacompra.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.listacompra.data.Producto

class ListaCompraViewModel : ViewModel() {

    private val productosEstado = mutableStateListOf<Producto>()
    val productos: List<Producto> get() = productosEstado

    private var siguienteId = 0L

    fun agregar(nombre: String) {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isEmpty()) {
            return
        }
        siguienteId += 1
        productosEstado.add(
            Producto(
                id = siguienteId,
                nombre = nombreLimpio
            )
        )
        Log.d(TAG, "Producto añadido: $nombreLimpio")
    }

    fun eliminar(producto: Producto) {
        val eliminado = productosEstado.removeAll { it.id == producto.id }
        if (eliminado) {
            Log.d(TAG, "Producto eliminado: ${producto.nombre}")
        }
    }

    fun alternarComprado(producto: Producto) {
        val indice = productosEstado.indexOfFirst { it.id == producto.id }
        if (indice < 0) {
            return
        }
        val actual = productosEstado[indice]
        productosEstado[indice] = actual.copy(comprado = !actual.comprado)
    }

    private companion object {
        const val TAG = "ListaCompra"
    }
}
