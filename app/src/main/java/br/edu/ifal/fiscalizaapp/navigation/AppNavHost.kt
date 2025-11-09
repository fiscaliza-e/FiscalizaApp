package br.edu.ifal.fiscalizaapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.categories.CategoryListScreen
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = homeRoute) {
        homeScreen(navController)
        protocolScreen(navController)
        exampleScreen(navController)
        loginScreen(navController)
        profileScreen(navController)
        faqScreen(navController)
        registerScreen(navController)
        categoryListScreen(navController)
    }
}