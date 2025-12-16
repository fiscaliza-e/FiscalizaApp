package br.edu.ifal.fiscalizaapp.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.navigation.screens.welcome.WelcomeScreen

const val welcomeRoute = "welcomeScreen"

fun NavGraphBuilder.welcomeScreen(navController: NavController) {
    composable(welcomeRoute) {
        WelcomeScreen(navController = navController)
    }
}