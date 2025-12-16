package br.edu.ifal.fiscalizaapp.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.navigation.screens.protocols.ProtocolScreen

const val protocolRoute = "protocolRoute"

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.protocolScreen(navController: NavController) {
    composable(route = protocolRoute) {
        ProtocolScreen(navController = navController)
    }
}

fun NavController.navigateToProtocolScreen() {
    navigate(protocolRoute)
}