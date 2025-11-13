package br.edu.ifal.fiscalizaapp.screens.protocols

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.protocolList.ProtocolList
import br.edu.ifal.fiscalizaapp.data.model.Protocol
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModelV2
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModelFactoryV2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProtocolViewModelV2 = viewModel(factory = ProtocolViewModelFactoryV2(context))

    LaunchedEffect(Unit) {
        viewModel.fetchUserProtocols()
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(containerColor = Color.White) { innerPadding ->
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
                        modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}