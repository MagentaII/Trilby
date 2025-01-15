package com.example.trilby.ui.util

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.ui.TrilbyAppViewModel

@Composable
fun TrilbySearchBar(
    viewModel: TrilbyAppViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Log.d("TAG", "TrilbySearchBar ViewModel: ${viewModel.hashCode()}")
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var isFocused by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.White, shape = RoundedCornerShape(50.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(50.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    if (viewModel.searchQuery.isEmpty()) {
                        Text(
                            text = "Search",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    }
                    BasicTextField(
                        value = viewModel.searchQuery,
                        onValueChange = { newValue ->
                            // text = newValue
                            viewModel.changeSearchQuery(newValue)
                            Log.i("TAG", "正在搜尋(正在輸入): $newValue")
                            // viewModel.search()
                            // Log.d("TAG", "TrilbySearchBar, : ${trilbyAppUiState.words}")
                        },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.search(viewModel.searchQuery)
                                Log.i("TAG", "正在搜尋: ${viewModel.searchQuery}")
                                focusManager.clearFocus()
                                isFocused = false
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            }
                    )
                }
            }
        }
        if (isFocused) {
            Text(
                text = "Cancel",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        focusManager.clearFocus()
                        isFocused = false
                    }
            )
        }
    }
}

@Preview
@Composable
private fun TrilbySearchBarPreview() {
    TrilbySearchBar(
        viewModel = hiltViewModel()
    )
}