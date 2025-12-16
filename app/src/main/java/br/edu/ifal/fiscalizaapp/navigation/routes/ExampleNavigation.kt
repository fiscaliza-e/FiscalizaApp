package br.edu.ifal.fiscalizaapp.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.navigation.screens.example.ExampleScreen

const val exampleRoute = "exampleScreen"

fun NavGraphBuilder.exampleScreen(navController: NavController) {
    composable(exampleRoute) {
        ExampleScreen(navController = navController)
    }
}

fun NavController.navigateToExampleScreen() {
    navigate(exampleRoute)
}