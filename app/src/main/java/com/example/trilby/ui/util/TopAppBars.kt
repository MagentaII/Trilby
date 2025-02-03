package com.example.trilby.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.R
import com.example.trilby.ui.TrilbyAppViewModel
import com.example.trilby.ui.theme.TrilbyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Column(
        modifier = modifier
            .background(Color(0xFF7988A9))
            .padding(all = 16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier.padding(
                        top = 16.dp,
                    )
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF7988A9),
                titleContentColor = Color.White,
            ),
            scrollBehavior = scrollBehavior,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopAppBar(
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Column(
        modifier = modifier
            .background(Color(0xFF3C4A68))
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Box(
                    contentAlignment = Alignment.BottomCenter, // 讓圖片貼齊底部
                    modifier = Modifier
                        .size(width = 140.dp, height = 37.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.trilby_image),
                        contentDescription = "trilby_image",
                    )
                }
            },
            navigationIcon = {
                TextButton(
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxHeight() // 讓按鈕填滿 TopAppBar 高度
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.White,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF3C4A68),
                titleContentColor = Color.White,
            ),
            scrollBehavior = scrollBehavior,
            modifier = modifier.fillMaxWidth()
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSearchBarTopAppBar(
    viewModel: TrilbyAppViewModel = hiltViewModel(),
    title: String,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Column(
        modifier = modifier
            .background(Color(0xFF7988A9))
            .padding(all = 16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF7988A9),
                titleContentColor = Color.White,
            ),
            scrollBehavior = scrollBehavior,
        )
        TrilbySearchBar(
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    title: String,
    onPopBack: () -> Unit,
    saveWord: () -> Unit,
    deleteWord: () -> Unit,
    isSaveWord: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Column(
        modifier = modifier
            .background(Color(0xFF7988A9))
            .padding(all = 16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF7988A9),
                titleContentColor = Color.White,
            ),
            navigationIcon = {
                IconButton(
                    onClick = { onPopBack() },
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(36.dp),
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        if (isSaveWord) {
                            deleteWord()
                        } else {
                            saveWord()
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                ) {
                    if (isSaveWord) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "",
                            modifier = Modifier
                                .size(36.dp),
                            tint = Color(0xFFFCEFBC)
                        )
                    } else {
                        Icon(
                            Icons.Filled.StarOutline,
                            contentDescription = "",
                            modifier = Modifier
                                .size(36.dp),
                            tint = Color.White
                        )
                    }
                }

            },
            scrollBehavior = scrollBehavior,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun DefaultTopAppBarPreview() {
    TrilbyTheme {
        Surface {
            DefaultTopAppBar(title = "Favorites")
        }
    }
}

@Preview
@Composable
private fun AuthTopAppBarPreview() {
    TrilbyTheme {
        Surface {
            AuthTopAppBar(
                onCancelClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun AddSearchBarTopAppBarPreview() {
    AddSearchBarTopAppBar(title = "Dictionary")
}

@Preview
@Composable
private fun DetailTopAppBarPreview() {
    TrilbyTheme {
        Surface {
            DetailTopAppBar(
                title = "Apple",
                onPopBack = {},
                saveWord = {},
                deleteWord = {},
                isSaveWord = true,
            )
        }
    }
}