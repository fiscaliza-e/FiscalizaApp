package br.edu.ifal.fiscalizaapp.navigation.routes

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.edu.ifal.fiscalizaapp.composables.session.SessionManager // Importe o SessionManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {

    val context = LocalContext.current

    val sessionManager = remember { SessionManager(context) }

    val startRoute = if (sessionManager.getUserApiId() != -1) {
        homeRoute
    } else {
        welcomeRoute
    }

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        welcomeScreen(navController)
        loginScreen(navController)
        registerScreen(navController)
        homeScreen(navController)
        protocolScreen(navController)
        newProtocolScreen(navController)
        categoryListScreen(navController)
        profileScreen(navController)
        chooseAvatarScreen(navController)
        editProfileScreen(navController)
        faqScreen(navController)
    }
}