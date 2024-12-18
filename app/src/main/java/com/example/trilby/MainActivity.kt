package com.example.trilby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.trilby.ui.TrilbyApp
import com.example.trilby.ui.theme.TrilbyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrilbyTheme {
                TrilbyApp()
            }
        }
    }
}