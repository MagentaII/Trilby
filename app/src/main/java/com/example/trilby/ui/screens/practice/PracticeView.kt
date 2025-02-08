package com.example.trilby.ui.screens.practice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trilby.ui.util.DefaultTopAppBar

@Composable
fun PracticeView(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Practice",
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Practice Screen")
        }
    }
}