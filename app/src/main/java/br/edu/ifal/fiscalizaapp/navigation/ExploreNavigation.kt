package br.edu.ifal.fiscalizaapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.home.HomeScreen
import br.edu.ifal.fiscalizaapp.screens.protocols.ProtocolScreen

const val homeRoute = "homeRoute"

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(homeRoute) {
        HomeScreen(navController)
    }
}

fun NavController.navigateToHomeScreen() {
    navigate(homeRoute)
}