package com.example.trilby.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.trilby.ui.screens.auth.login.LoginView
import com.example.trilby.ui.screens.auth.register.RegisterView
import com.example.trilby.ui.screens.dictionary.DictionaryView
import com.example.trilby.ui.screens.edit_profile.EditProfileView
import com.example.trilby.ui.screens.favorites.FavoritesView
import com.example.trilby.ui.screens.practice.PracticeView
import com.example.trilby.ui.screens.profile.ProfileView
import com.example.trilby.ui.screens.splash.SplashView
import com.example.trilby.ui.screens.word_detail.WordDetailView

@Composable
fun TrilbyNavHost(
    navController: NavHostController,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.InApp,
    ) {
        navigation<Route.InApp>(startDestination = Route.Dictionary) {
            composable<Route.Dictionary> {
                DictionaryView(
                    sharedViewModel = sharedViewModel,
                    onNavigateToDetail = { route ->
                        navController.navigate(route)
                    }
                )
            }
            composable<Route.Favorites> {
                FavoritesView(
                    sharedViewModel = sharedViewModel,
                    onNavigateToDetail = { route ->
                        navController.navigate(route)
                    }
                )
            }
            composable<Route.Practice> {
                PracticeView()
            }
            composable<Route.Profile> {
                ProfileView(
                    onNavigateToEditProfile = { route ->
                        navController.navigate(route)
                    },
                    onNavigateToLogin = { route ->
                        navController.navigate(route)
                    },
                    onNavigateToRegister = { route ->
                        navController.navigate(route)
                    },
                )
            }
        }

        composable<Route.WordDetail> { backStateEntry ->
            val wordDetail: Route.WordDetail = backStateEntry.toRoute()
            val uiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
            WordDetailView(
                wordDetail = wordDetail,
                words = uiState.words,
                onPopBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.Login> {
            LoginView(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignInNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(route) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable<Route.Register> {
            RegisterView(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignUpNavigate = { route ->
                    navController.navigate(route) {
                        if (route == Route.Login) {
                            popUpTo(Route.Register) {
                                inclusive = true
                            }
                        } else {
                            popUpTo(route) {
                                inclusive = true
                            }
                        }
                    }
                },
            )
        }

        composable<Route.EditProfile> {
            EditProfileView(
                onCancelClick = {
                    navController.popBackStack()
                },
                onSaveClick = { }
            )
        }

        composable<Route.Splash> {
            SplashView()
        }
    }
}