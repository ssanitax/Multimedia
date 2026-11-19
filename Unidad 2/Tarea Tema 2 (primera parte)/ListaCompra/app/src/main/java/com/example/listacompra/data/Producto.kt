package com.example.listacompra.data

data class Producto(
    val id: Long,
    val nombre: String,
    val comprado: Boolean = false
)
