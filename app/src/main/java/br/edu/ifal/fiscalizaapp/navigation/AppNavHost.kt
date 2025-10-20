package br.edu.ifal.fiscalizaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = homeRoute) {
        homeScreen(navController)
        exampleScreen(navController)
    }
}