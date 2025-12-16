package br.edu.ifal.fiscalizaapp.navigation.routes

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.composables.session.SessionManager
import br.edu.ifal.fiscalizaapp.navigation.screens.profile.ProfileScreen

const val profileRoute = "profileScreen"

fun NavGraphBuilder.profileScreen(navController: NavController) {
    composable(profileRoute) {
        ProfileScreen(navController = navController)
    }
}

fun NavController.navigateToProfileScreen() {
    navigate(profileRoute)
}
