package com.example.trilby.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.trilby.ui.screens.auth.login.navigation.loginGraph
import com.example.trilby.ui.screens.auth.login.navigation.navigateToLogin
import com.example.trilby.ui.screens.auth.register.navigation.navigateToRegister
import com.example.trilby.ui.screens.auth.register.navigation.registerGraph
import com.example.trilby.ui.screens.dictionary.navigation.DictionaryBaseRoute
import com.example.trilby.ui.screens.dictionary.navigation.dictionaryGraph
import com.example.trilby.ui.screens.edit_profile.navigation.editProfileGraph
import com.example.trilby.ui.screens.edit_profile.navigation.navigateToEditProfile
import com.example.trilby.ui.screens.favorites.navigation.favoritesGraph
import com.example.trilby.ui.screens.practice.navigation.practiceGraph
import com.example.trilby.ui.screens.profile.navigation.profileGraph
import com.example.trilby.ui.screens.word_detail.navigation.navigateToWordDetail
import com.example.trilby.ui.screens.word_detail.navigation.wordDetailGraph

@Composable
fun TrilbyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DictionaryBaseRoute,
        modifier = modifier,
    ) {
        dictionaryGraph(
            onItemClick = navController::navigateToWordDetail,
        ) {
            wordDetailGraph(
                onBackClick = navController::popBackStack,
            )
        }
        favoritesGraph(
            onItemClick = navController::navigateToWordDetail,
        ) {
            wordDetailGraph(
                onBackClick = navController::popBackStack,
            )
        }
        practiceGraph()
        profileGraph(
            onEditProfileClick = navController::navigateToEditProfile,
            onLoginClick = navController::navigateToLogin,
            onRegisterClick = navController::navigateToRegister,
            editProfileDestination = {
                editProfileGraph(
                    onBackClick = navController::popBackStack,
                    onSaveClick = navController::popBackStack,
                )
            },
            loginDestination = {
                loginGraph(
                    onBackClick = navController::popBackStack,
                )
            },
            registerDestination = {
                registerGraph(
                    onBackClick = navController::popBackStack,
                )
            }
        )
    }
}

//@Composable
//fun TrilbyNavHost(
//    navController: NavHostController,
//    sharedViewModel: SharedViewModel = hiltViewModel(),
//    modifier: Modifier = Modifier
//) {
//    NavHost(
//        navController = navController,
//        startDestination = Route.InApp,
//    ) {
//        navigation<Route.InApp>(startDestination = Route.Dictionary) {
//            composable<Route.Dictionary> {
//                DictionaryView(
//                    onUpdateSharedWords = { sharedWord ->
//                        sharedViewModel.updateWords(sharedWord)
//                    },
//                    onNavigateToDetail = { route ->
//                        navController.navigate(route)
//                    }
//                )
//            }
//            composable<Route.Favorites> {
//                FavoritesView(
//                    onUpdateSharedWords = { sharedWord ->
//                        sharedViewModel.updateWords(sharedWord)
//                    },
//                    onNavigateToDetail = { route ->
//                        navController.navigate(route)
//                    }
//                )
//            }
//            composable<Route.Practice> {
//                PracticeView()
//            }
//            composable<Route.Profile> {
//                ProfileView(
//                    onNavigateToEditProfile = { route ->
//                        navController.navigate(route)
//                    },
//                    onNavigateToLogin = { route ->
//                        navController.navigate(route)
//                    },
//                    onNavigateToRegister = { route ->
//                        navController.navigate(route)
//                    },
//                )
//            }
//        }
//
//        composable<Route.WordDetail> { backStateEntry ->
//            val wordDetail: Route.WordDetail = backStateEntry.toRoute()
//            val uiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
//            WordDetailView(
//                wordDetail = wordDetail,
//                words = uiState.words,
//                onPopBack = {
//                    navController.popBackStack()
//                },
//            )
//        }
//
//        composable<Route.Login> {
//            LoginView(
//                onNavigateBack = {
//                    navController.popBackStack()
//                },
//                onSignInNavigate = { route ->
//                    navController.navigate(route) {
//                        popUpTo(route) {
//                            inclusive = true
//                        }
//                    }
//                },
//            )
//        }
//
//        composable<Route.Register> {
//            RegisterView(
//                onNavigateBack = {
//                    navController.popBackStack()
//                },
//                onSignUpNavigate = { route ->
//                    navController.navigate(route) {
//                        if (route == Route.Login) {
//                            popUpTo(Route.Register) {
//                                inclusive = true
//                            }
//                        } else {
//                            popUpTo(route) {
//                                inclusive = true
//                            }
//                        }
//                    }
//                },
//            )
//        }
//
//        composable<Route.EditProfile> {
//            EditProfileView(
//                onCancelClick = {
//                    navController.popBackStack()
//                },
//                onSaveClick = { }
//            )
//        }
//
//        composable<Route.Splash> {
//            SplashView()
//        }
//    }
//}