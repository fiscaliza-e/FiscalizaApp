package br.edu.ifal.fiscalizaapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.profile.ProfileScreen

const val profileRoute = "profileScreen"

fun NavGraphBuilder.profileScreen(navController: NavController) {
    composable(profileRoute) {
        ProfileScreen()
    }
}

fun NavController.navigateToProfileScreen() {
    navigate(profileRoute)
}

