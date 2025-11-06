package br.edu.ifal.fiscalizaapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.register.RegisterScreen

const val registerRoute = "registerRoute"

fun NavGraphBuilder.registerScreen(navController: NavController) {
    composable(route = registerRoute) {
        RegisterScreen(navController = navController)
    }
}

fun NavController.navigateToRegisterScreen() {
    navigate(registerRoute)
}