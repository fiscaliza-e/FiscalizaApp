package br.edu.ifal.fiscalizaapp.routes

import FaqScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val faqRoute = "faqRoute"

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.faqScreen(navController: NavController) {
    composable(route = faqRoute) {
        FaqScreen(navController = navController)
    }
}

fun NavController.navigateToFaqScreen() {
    navigate(faqRoute)
}

