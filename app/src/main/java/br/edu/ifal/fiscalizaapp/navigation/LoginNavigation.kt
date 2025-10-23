package br.edu.ifal.fiscalizaapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.login.LoginScreen

const val loginRoute = "loginRoute"

fun NavGraphBuilder.loginScreen(navController: NavController) {
    composable(route = loginRoute) {
        LoginScreen(navController = navController)
    }
}

fun NavController.navigateToLoginScreen() {
    navigate(loginRoute)
}