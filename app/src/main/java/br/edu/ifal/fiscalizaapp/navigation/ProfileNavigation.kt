package br.edu.ifal.fiscalizaapp.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.profile.ProfileScreen
import br.edu.ifal.fiscalizaapp.session.SessionManager

const val profileRoute = "profileScreen"

fun NavGraphBuilder.profileScreen(navController: NavController) {
    composable(profileRoute) {
        val context = LocalContext.current
        ProfileScreen(
            onLogout = {
                val sessionManager = SessionManager(context)
                sessionManager.clearSession()

                navController.navigate(loginRoute) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

fun NavController.navigateToProfileScreen() {
    navigate(profileRoute)
}
