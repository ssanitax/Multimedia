package com.example.listacompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.listacompra.ui.ListaCompraScreen
import com.example.listacompra.ui.theme.ListaCompraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaCompraTheme {
                ListaCompraScreen()
            }
        }
    }
}
