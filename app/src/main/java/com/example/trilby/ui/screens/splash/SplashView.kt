package com.example.trilby.ui.screens.splash

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashView(
//    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToTrilby: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .background(color = Color(0xFF7988A9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.i("TAG", "SplashView: Loading....")
        CircularProgressIndicator(color = Color(0xFFFCEFBC))
    }

}

@Preview(showBackground = true)
@Composable
private fun SplashViewPreview() {
    SplashView(
        onNavigateToLogin = {},
        onNavigateToTrilby = {},
    )
}