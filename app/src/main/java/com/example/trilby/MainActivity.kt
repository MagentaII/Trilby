package com.example.trilby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.trilby.ui.navigation.auth.AuthNavHost
import com.example.trilby.ui.theme.TrilbyTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val isLoggedIn = currentUser != null
        enableEdgeToEdge()
        setContent {
            TrilbyTheme {
                AuthNavHost(
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }
}