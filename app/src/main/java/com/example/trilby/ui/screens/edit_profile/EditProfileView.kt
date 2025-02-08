package com.example.trilby.ui.screens.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trilby.ui.util.EditProfileTopAppBar

@Composable
fun EditProfileView(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            EditProfileTopAppBar(
                onCancelClick = onCancelClick,
                isDataChange = false,
                onSaveClick = onSaveClick
            )
        },
        contentColor = Color(0xFF8B9CBF),
        containerColor = Color(0xFF8B9CBF)
    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFF8B9CBF)
        ) {
            Column {
                Box {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(177.dp)
                                .background(Color(0xFF2B3240))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .background(Color(0xFF8B9CBF))
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(100.dp)
                            .background(Color.Black)
                            .align(Alignment.BottomStart)
                    )
                }

                Spacer(Modifier.height(24.dp))

                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.White
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(96.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "Name",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        OutlinedTextField(
                            value = "",
                            onValueChange = { newValue -> },
                            placeholder = {
                                Text(
                                    text = "Enter your name...",
                                    color = Color.LightGray,
                                    fontSize = 18.sp
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
                        )
                    }
                }
                HorizontalDivider(
                    color = Color.White,
                    thickness = 2.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(96.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "Username",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        OutlinedTextField(
                            value = "",
                            onValueChange = { newValue -> },
                            placeholder = {
                                Text(
                                    text = "Enter your username...",
                                    color = Color.LightGray,
                                    fontSize = 18.sp
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
                        )
                    }
                }
                HorizontalDivider(
                    color = Color.White,
                    thickness = 2.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(96.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "Password",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        OutlinedTextField(
                            value = "",
                            onValueChange = { newValue -> },
                            placeholder = {
                                Text(
                                    text = "Enter your password...",
                                    color = Color.LightGray,
                                    fontSize = 18.sp
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
                        )
                    }
                }
                HorizontalDivider(
                    color = Color.White,
                    thickness = 2.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(96.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "Email",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        OutlinedTextField(
                            value = "",
                            onValueChange = { newValue -> },
                            placeholder = {
                                Text(
                                    text = "Enter your email...",
                                    color = Color.LightGray,
                                    fontSize = 18.sp
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
                        )
                    }
                }
                HorizontalDivider(
                    color = Color.White,
                    thickness = 2.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditProfileViewPreview() {
    EditProfileView(
        onCancelClick = {},
        onSaveClick = {}
    )
}