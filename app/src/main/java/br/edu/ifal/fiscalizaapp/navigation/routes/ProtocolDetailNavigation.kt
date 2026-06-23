package br.edu.ifal.fiscalizaapp.navigation.routes

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.edu.ifal.fiscalizaapp.data.db.DatabaseHelper
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.navigation.screens.protocols.ProtocolDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val protocolDetailRoute = "protocolDetailScreen"

fun NavGraphBuilder.protocolDetailScreen(navController: NavController) {
    composable(
        route = "$protocolDetailRoute/{protocolNumber}",
        arguments = listOf(
            navArgument("protocolNumber") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val encodedNumber = backStackEntry.arguments?.getString("protocolNumber") ?: ""
        val protocolNumber = URLDecoder.decode(encodedNumber, StandardCharsets.UTF_8.name())

        val context = LocalContext.current
        var protocol by remember { mutableStateOf<ProtocolEntity?>(null) }

        LaunchedEffect(protocolNumber) {
            protocol = withContext(Dispatchers.IO) {
                DatabaseHelper.getInstance(context).protocolDao().getByProtocolNumber(protocolNumber)
            }
        }

        protocol?.let {
            ProtocolDetailScreen(protocol = it, navController = navController)
        }
    }
}

fun NavController.navigateToProtocolDetail(protocolNumber: String) {
    val encoded = URLEncoder.encode(protocolNumber, StandardCharsets.UTF_8.name())
    navigate("$protocolDetailRoute/$encoded")
}
