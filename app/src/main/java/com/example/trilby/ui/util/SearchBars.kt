package com.example.trilby.ui.util

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
//    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val dictionaryUiState by viewModel.uiState.collectAsState()
    Box(
        modifier = modifier
            .fillMaxWidth()
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
//                        text = newValue
                        viewModel.changeSearchQuery(newValue)
                        Log.i("TAG", "正在搜尋(正在輸入): $newValue")
                        viewModel.search()
                        Log.d("TAG", "TrilbySearchBar, : ${dictionaryUiState.words}")
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
                            viewModel.search()
                            Log.i("TAG", "正在搜尋: ${viewModel.searchQuery}")
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrilbySearchBarPreview() {
    TrilbySearchBar()
}