package br.edu.ifal.fiscalizaapp.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.navigation.screens.profile.ChooseAvatarScreen

const val chooseAvatarRoute = "chooseAvatarScreen"

fun NavGraphBuilder.chooseAvatarScreen(navController: NavController) {
    composable(chooseAvatarRoute) {
        ChooseAvatarScreen(navController = navController)
    }
}

fun NavController.navigateToChooseAvatarScreen() {
    navigate(chooseAvatarRoute)
}


