package com.example.trilby.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trilby.ui.navigation.TrilbyNavHost
import com.example.trilby.ui.navigation.topLevelRoutes
import com.example.trilby.ui.theme.TrilbyTheme

@Composable
fun TrilbyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("Dictionary") }
    var isShowSearchBar by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TrilbyAppTopBar(
                title = title,
                showSearchBar = isShowSearchBar,
            )
        },
        bottomBar = {
            TrilbyBottomNavigationBar(
                navController = navController,
                onNavigation = { route, name, showSearchBar->
                    title = name
                    isShowSearchBar = showSearchBar
                    Log.i("TAG", "route: $route")
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
    ) { innerPadding ->
        TrilbyNavHost(
            navController = navController,
            Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrilbyAppTopBar(
    title: String,
    showSearchBar: Boolean,
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
        if (showSearchBar) {
            TrilbySearchBar()
        }
    }
}

@Composable
fun TrilbySearchBar(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
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
                if (text.isEmpty()) {
                    Text(
                        text = "Search",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = { newValue ->
                        text = newValue
                        Log.i("TAG", "正在搜尋(正在輸入): $newValue")
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
                            Log.i("TAG", "正在搜尋: $text")
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

@Composable
fun TrilbyBottomNavigationBar (
    navController: NavHostController,
    onNavigation: (route: Any, name: String, showSearchBar: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        containerColor = Color(0xFF7988A9),
        modifier = modifier
    ) {
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        topLevelRoute.route::class
                    )
                } == true,
                onClick = {
                    Log.i(
                        "TAG",
                        "Current Destination Hierarchy: ${
                            currentDestination?.hierarchy?.any {
                                it.hasRoute(topLevelRoute.route::class)
                            }
                        }"
                    )
                    if (currentDestination?.hierarchy?.any {
                            it.hasRoute(
                                topLevelRoute.route::class
                            )
                        } == false) {
                        onNavigation(topLevelRoute.route, topLevelRoute.name, topLevelRoute.showSearchBar)
                    }
                },
                icon = {
                    Icon(
                        if (currentDestination?.hierarchy?.any {
                                it.hasRoute(
                                    topLevelRoute.route::class
                                )
                            } == true) {
                            topLevelRoute.selectedIcon
                        } else {
                            topLevelRoute.unselectedIcon
                        },
                        contentDescription = topLevelRoute.name
                    )
                },
                label = { Text(topLevelRoute.name) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFCEFBC),
                    unselectedIconColor = Color(0xFFFFFFFF),
                    selectedTextColor = Color(0xFFFCEFBC),
                    unselectedTextColor = Color(0xFFFFFFFF),
                    indicatorColor = Color.Transparent,
                )
            )
        }
    }
}


@Preview
@Composable
private fun TrilbyAppPreview() {
    TrilbyTheme {
        TrilbyApp()
    }
}

@Preview(name = "Dictionary App Bar")
@Composable
private fun TrilbyAppTopBarPreview() {
    val title by remember { mutableStateOf("Dictionary") }
    val isShowSearchBar by remember { mutableStateOf(true) }
    TrilbyAppTopBar(
        title = title,
        showSearchBar = isShowSearchBar,
    )
}

@Preview
@Composable
private fun TrilbyBottomNavigationBarPreview() {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("Dictionary") }
    var isShowSearchBar by remember { mutableStateOf(true) }
    TrilbyBottomNavigationBar(
        navController = navController,
        onNavigation = { route, name, showSearchBar->
            title = name
            isShowSearchBar = showSearchBar
            Log.i("TAG", "route: $route")
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}