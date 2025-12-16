package br.edu.ifal.fiscalizaapp.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.edu.ifal.fiscalizaapp.navigation.screens.protocols.NewProtocolScreen

const val newProtocolRoute = "newProtocolScreen"

fun NavGraphBuilder.newProtocolScreen(navController: NavController) {
    composable(
        route = "$newProtocolRoute?categoryId={categoryId}",

        arguments = listOf(
            navArgument("categoryId") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) { backStackEntry ->
        val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: -1

        NewProtocolScreen(
            navController = navController,
            categoryIdFromRoute = categoryId
        )
    }
}

fun NavController.navigateToNewProtocolScreen() {
    navigate(newProtocolRoute)
}