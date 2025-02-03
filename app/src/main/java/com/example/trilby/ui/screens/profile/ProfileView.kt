package com.example.trilby.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.ui.navigation.Route

@Composable
fun ProfileView(
    onNavigate: (route: Route) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val profileUiState by viewModel.uiState.collectAsState()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = profileUiState.currentUser?.email ?: "No user is signed in.",
                style = TextStyle(fontSize = 16.sp)
            )
            SignInAndOutButton(
                viewModel = viewModel,
                hasUser = profileUiState.currentUser != null,
                onNavigate = onNavigate
            )
            ElevatedButton(
                onClick = {
                    onNavigate(Route.Register)
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFFA8A4BB),
                    contentColor = Color.White,
                ),
                border = BorderStroke(2.dp, Color.White),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
                modifier = Modifier.size(width = 200.dp, height = 64.dp),
            ) {
                Text(
                    text = "Sign up",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun SignInAndOutButton(
    viewModel: ProfileViewModel,
    hasUser: Boolean,
    onNavigate: (route: Route) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Confirm Sign Out",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            },
            text = {
                Text("Are you sure you want to sign out?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.signOut()
                        showDialog = false
                    }
                ) {
                    Text("Sign out", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    ElevatedButton(
        onClick = {
            handleButtonClick(
                hasUser = hasUser,
                onNavigate = onNavigate,
                showSignOutDialog = { showDialog = true }
            )
        },
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFFA8A4BB),
            contentColor = Color.White,
        ),
        border = BorderStroke(2.dp, Color.White),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
        modifier = Modifier.size(width = 200.dp, height = 64.dp),
    ) {
        Text(
            text = if (hasUser) "Sign out" else "Sign in",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
    }
}

private fun handleButtonClick(
    hasUser: Boolean,
    onNavigate: (route: Route) -> Unit,
    showSignOutDialog: () -> Unit
) {
    if (hasUser) {
        showSignOutDialog()
    } else {
        onNavigate(Route.Login)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileViewPreview() {
    ProfileView(
        onNavigate = {},
    )
}