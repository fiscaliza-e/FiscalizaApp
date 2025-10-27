package br.edu.ifal.fiscalizaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.categories.CategoryListScreen

const val categoryListRoute = "categories"


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = homeRoute) {
        homeScreen(navController)
        exampleScreen(navController)
        loginScreen(navController)

        composable(route = categoryListRoute) {
            CategoryListScreen(navController = navController)

        }
    }
}