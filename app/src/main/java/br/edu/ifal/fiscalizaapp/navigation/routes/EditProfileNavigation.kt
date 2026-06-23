package br.edu.ifal.fiscalizaapp.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.navigation.screens.profile.EditProfileScreen

const val editProfileRoute = "editProfileScreen"

fun NavGraphBuilder.editProfileScreen(navController: NavController) {
    composable(editProfileRoute) {
        EditProfileScreen(navController = navController)
    }
}

fun NavController.navigateToEditProfileScreen() {
    navigate(editProfileRoute)
}

