package br.edu.ifal.fiscalizaapp.navigation.screens.protocols

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.dialog.LogoutDialog
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.composables.protocollist.ProtocolList
import br.edu.ifal.fiscalizaapp.model.Protocol
import br.edu.ifal.fiscalizaapp.navigation.routes.loginRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.newProtocolRoute
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModel
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProtocolViewModel = viewModel(factory = ProtocolViewModelFactory(context))

    LaunchedEffect(Unit) {
        viewModel.fetchUserProtocols()
    }

    val uiState by viewModel.uiState.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout()
                navController.navigate(loginRoute) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            AppHeader(
                type = AppHeaderType.MAIN_SCREEN,
                onActionClick = {
                    showLogoutDialog = true
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Success -> {
                    ProtocolList(
                        protocols = state.data,
                        modifier = Modifier.fillMaxSize(),
                        onNewProtocolClick = {
                            navController.navigate(newProtocolRoute)
                        }
                    )
                }
                is UiState.Error -> {
                    Text(text = "Erro: ${state.message}")
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProtocolScreenPreview() {
    val mockProtocols = listOf(
        Protocol(
            title = "Buraco na rua principal",
            description = "Buraco de grande porte próximo à esquina da Av. Central com a Rua das Flores, dificultando o tráfego.",
            status = "PENDENTE",
            protocolNumber = "P-0001",
            date = "10-20-2025",
            userId = 1
        ),
        Protocol(
            title = "Lâmpada queimada em poste",
            description = "Poste de iluminação em frente à escola municipal está apagado há mais de uma semana.",
            status = "VISUALIZADO",
            protocolNumber = "P-0002",
            date = "10-18-2025",
            userId = 2
        )
    )
    Scaffold(containerColor = Color.White) { innerPadding ->
        ProtocolList(
            protocols = mockProtocols,
            modifier = Modifier.padding(innerPadding),
            onNewProtocolClick = {}
        )
    }
}