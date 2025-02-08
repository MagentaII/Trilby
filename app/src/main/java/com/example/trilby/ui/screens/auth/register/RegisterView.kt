package com.example.trilby.ui.screens.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.R
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.util.AuthTopAppBar
import com.example.trilby.ui.util.SocialIconButton

@Composable
fun RegisterView(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onSignUpNavigate: (route: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AuthTopAppBar(
                onCancelClick = onNavigateBack
            )
        },
        containerColor = Color(0xFF7988A9)

    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFF7988A9)
        ) {
            Box( // 讓按鈕可以固定在底部
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp)
                ) {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = "Create your account",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(Modifier.height(24.dp))

                    // Email 輸入框
                    CustomOutlinedTextField(viewModel = viewModel, label = "Name")
                    Spacer(Modifier.height(24.dp))

                    // Email 輸入框
                    CustomOutlinedTextField(viewModel = viewModel, label = "Email")
                    Spacer(Modifier.height(24.dp))

                    // Password 輸入框
                    CustomOutlinedTextField(viewModel = viewModel, label = "Password")
                    Spacer(Modifier.height(24.dp))

                    // Confirm password 輸入框
                    CustomOutlinedTextField(viewModel = viewModel, label = "Confirm password")
                    Spacer(Modifier.height(24.dp))

                    // 第三方登入區塊
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "- Or sign up with -",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            )

                            Spacer(Modifier.height(24.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                SocialIconButton(iconResId = R.drawable.google)
                                SocialIconButton(iconResId = R.drawable.facebook)
                                SocialIconButton(iconResId = R.drawable.x)
                            }
                        }
                    }

                    // 這個 Spacer 會把按鈕推到底部
                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp), // 讓按鈕貼近螢幕底部
                    ) {
                        // 登入按鈕
                        Button(
                            onClick = {
                                viewModel.onSignUpClick(onSignUpNavigate)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(
                                text = "Sign up",
                                color = Color(0xFF3C4A68),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    viewModel: RegisterViewModel,
    label: String
) {
    OutlinedTextField(
        value = when (label) {
            "Name" -> viewModel.name
            "Email" -> viewModel.email
            "Password" -> viewModel.password
            else -> viewModel.confirmPassword
        },
        onValueChange = { newValue ->
            when (label) {
                "Name" -> viewModel.updateName(newValue)
                "Email" -> viewModel.updateEmail(newValue)
                "Password" -> viewModel.updatePassword(newValue)
                else -> viewModel.updateConfirmPassword(newValue)
            }
        },
        label = {
            Text(
                text = label,
                color = Color.White
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = Color.White
        ),
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                drawLine(
                    color = Color.White,
                    start = Offset(0f, size.height - strokeWidth),
                    end = Offset(size.width, size.height - strokeWidth),
                    strokeWidth = strokeWidth
                )
            }
    )
}

@Preview
@Composable
private fun RegisterViewPreview() {
    RegisterView(
        onNavigateBack = {},
        onSignUpNavigate = {},
    )
}