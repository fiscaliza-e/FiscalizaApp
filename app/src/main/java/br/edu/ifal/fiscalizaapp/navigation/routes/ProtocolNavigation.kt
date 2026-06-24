package br.edu.ifal.fiscalizaapp.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.edu.ifal.fiscalizaapp.navigation.screens.protocols.ProtocolScreen

const val protocolRoute = "protocolRoute"

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.protocolScreen(navController: NavController) {
    composable(
        route = "$protocolRoute?created={created}",
        arguments = listOf(navArgument("created") {
            type = NavType.BoolType
            defaultValue = false
        })
    ) { backStackEntry ->
        val showCreated = backStackEntry.arguments?.getBoolean("created") ?: false
        ProtocolScreen(navController = navController, showCreated = showCreated)
    }
}

fun NavController.navigateToProtocolScreen() {
    navigate(protocolRoute)
}
