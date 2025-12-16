package br.edu.ifal.fiscalizaapp.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.protocols.NewProtocolScreen

const val newProtocolRoute = "newProtocolScreen"

fun NavGraphBuilder.newProtocolScreen(navController: NavController) {
    composable(route = newProtocolRoute) {
        NewProtocolScreen(navController = navController)
    }
}

fun NavController.navigateToNewProtocolScreen() {
    navigate(newProtocolRoute)
}