package com.example.trilby.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.util.ProfileTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToEditProfile: (route: Route) -> Unit,
    onNavigateToLogin: (route: Route) -> Unit,
    onNavigateToRegister: (route: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val profileUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    var showDialog by remember { mutableStateOf(false) }
    val isLoading = profileUiState.isLoading

    LaunchedEffect(profileUiState.showBottomSheet) {
        if (profileUiState.showBottomSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    if (profileUiState.showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.hideBottomSheet()
            },
            sheetState = sheetState,
            containerColor = Color(0xFF3A5069),
            contentColor = Color.White,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp) // 在上方增加間距
                        .width(40.dp)
                        .height(5.dp)
                        .background(Color.White, shape = RoundedCornerShape(50))
                )
            }
        ) {
            BottomSheetContent(
                showSingOutDialog = {
                    showDialog = true
                }
            )
        }
    }

    if (showDialog) {
        ShowSignOutDialog(
            onClickSignOut = {
                viewModel.signOut()
                showDialog = false
                viewModel.hideBottomSheet()
            },
            onDismissDialog = {
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            ProfileTopAppBar(
                showBottomSheet = { viewModel.showBottomSheet() },
                onNavigateToEditProfile = onNavigateToEditProfile,
                hasUser = profileUiState.currentUser != null,
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                Text(
                    text = profileUiState.currentUser?.name ?: "Trilby Friend",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = profileUiState.currentUser?.email ?: "@trilby_friend123456",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                )

                Spacer(Modifier.height(24.dp))

                if (profileUiState.currentUser != null) {
                    ProfileContentAfterSignIn()
                } else {
                    ProfileContentBeforeSignIn(
                        onNavigateToLogin = onNavigateToLogin,
                        onNavigateToRegister = onNavigateToRegister,
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContentAfterSignIn(
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Overview",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Black
            )
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            contentColor = Color.Black,
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp,
                            brush = SolidColor(Color.Black)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                    ) {
                        Text(
                            text = "Item A",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }
                item {
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            contentColor = Color.Black,
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp,
                            brush = SolidColor(Color.Black)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                    ) {
                        Text(
                            text = "Item B",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }
                item {
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            contentColor = Color.Black,
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp,
                            brush = SolidColor(Color.Black)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                    ) {
                        Text(
                            text = "Item C",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }
                item {
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            contentColor = Color.Black,
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp,
                            brush = SolidColor(Color.Black)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                    ) {
                        Text(
                            text = "Item D",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun ProfileContentBeforeSignIn(
    onNavigateToLogin: (route: Route) -> Unit,
    onNavigateToRegister: (route: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Sign in to get full feature",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Black
            )
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                OutlinedButton(
                    onClick = {
                        onNavigateToRegister(Route.Register)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        contentColor = Color.Black,
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = SolidColor(Color.Black)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                ) {
                    Text(
                        text = "Create a new account",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                }

                Spacer(Modifier.height(24.dp))

                OutlinedButton(
                    onClick = {
                        onNavigateToLogin(Route.Login)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        contentColor = Color.Black,
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = SolidColor(Color.Black)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                ) {
                    Text(
                        text = "Add an existing account",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                }
            }
        }
    }


}

@Composable
fun BottomSheetContent(
    showSingOutDialog: () -> Unit,
) {
    Surface(
        color = Color(0xFF3A5069)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Account",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                    )
                )
                Spacer(Modifier.height(24.dp))
                Box(
                    contentAlignment = Alignment.TopStart
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Sign out of current account",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                            ),
                            modifier = Modifier.clickable {
                                showSingOutDialog()
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ShowSignOutDialog(
    onClickSignOut: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
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
                    onClickSignOut()
                    onDismissDialog()
                }
            ) {
                Text("Sign out", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissDialog() }) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileViewPreview() {
    ProfileView(
        onNavigateToEditProfile = {},
        onNavigateToLogin = {},
        onNavigateToRegister = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentBeforeSignInPreview() {
    ProfileContentBeforeSignIn(
        onNavigateToLogin = {},
        onNavigateToRegister = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentAfterSignInPreview() {
    ProfileContentAfterSignIn()
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent(
        showSingOutDialog = {}
    )
}