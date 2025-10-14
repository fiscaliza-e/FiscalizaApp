package br.edu.ifal.fiscalizaapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.screens.example.composables.exampleComposable.ImagePickerPlayground

const val imagePickerPlaygroundRoute = "image_picker_Playground"

fun NavGraphBuilder.imagePickerPlaygroundScreen(navController: NavController) {
    composable(route = imagePickerPlaygroundRoute) {
        ImagePickerPlayground()
    }
}