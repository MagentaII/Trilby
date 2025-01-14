package com.example.trilby.ui.screens.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.R
import com.example.trilby.ui.navigation.Route

@Composable
fun LoginView(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: (route: Route) -> Unit,
    onSignInNavigate: (route: Route) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFF7988A9)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = 214.dp, height = 56.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trilby_image),
                    contentDescription = "trilby_image",
                )
            }
            Spacer(Modifier.height(64.dp))
            Text(
                text = "Login to your Account",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(Modifier.height(32.dp))
            TextField(
                value = viewModel.email,
                onValueChange = { newEmail ->
                    viewModel.updateEmail(newEmail)
                },
                label = {
                    Text(
                        text = "Email",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                ),
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = 376.dp, height = 64.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(50.dp),
                    ),
            )
            Spacer(Modifier.height(32.dp))
            TextField(
                value = viewModel.password,
                onValueChange = { newPassword ->
                    viewModel.updatePassword(newPassword)
                },
                label = {
                    Text(
                        text = "Password",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                ),
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = 376.dp, height = 64.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(50.dp),
                    ),
            )
            Spacer(Modifier.height(32.dp))
            ElevatedButton(
                onClick = {
                    viewModel.onSignInClick(onSignInNavigate)
                },
                colors = ButtonDefaults.elevatedButtonColors().copy(
                    containerColor = Color(0xFFA8A4BB),
                    contentColor = Color.White,
                ),
                border = BorderStroke(2.dp, Color.White),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 4.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = 376.dp, height = 64.dp),
            ) {
                Text(
                    text = "Sign in",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(Modifier.height(64.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "- Or sign in with -",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )

                    Spacer(Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SocialIconBox(iconResId = R.drawable.google)
                        SocialIconBox(iconResId = R.drawable.facebook)
                        SocialIconBox(iconResId = R.drawable.x)
                    }
                    Spacer(Modifier.height(64.dp))
                    Row {
                        Text(
                            text = "Don't have an account? ",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = "Sign up",
                            style = TextStyle(
                                color = Color(0xFFFCEFBC),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.clickable {
                                onNavigateToRegister(Route.Register)
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun SocialIconBox(iconResId: Int) {
    Box(
        modifier = Modifier
            .size(width = 92.dp, height = 53.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginViewPreview() {
    LoginView(
        onNavigateToRegister = {},
        onSignInNavigate = {},
    )
}