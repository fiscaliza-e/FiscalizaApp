package br.edu.ifal.fiscalizaapp.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.edu.ifal.fiscalizaapp.screens.categories.CategoryListScreen

const val categoryListRoute = "categories"

fun NavGraphBuilder.categoryListScreen(navController: NavController) {
    composable(route = categoryListRoute) {
        CategoryListScreen(navController = navController)
    }
}

fun NavController.navigateToCategoryListScreen() {
    navigate(categoryListRoute)
}