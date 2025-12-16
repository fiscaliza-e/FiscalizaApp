package br.edu.ifal.fiscalizaapp.navigation.screens.protocols

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.protocollist.ProtocolList
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.RefreshState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProtocolViewModel = viewModel(factory = ProtocolViewModelFactory(context))

    val protocols by viewModel.protocols.collectAsStateWithLifecycle(initialValue = emptyList())
    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()

    LaunchedEffect(refreshState) {
        if (refreshState is RefreshState.Error) {
            Toast.makeText(context, (refreshState as RefreshState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(containerColor = Color.White) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            ProtocolList(
                protocols = protocols,
                modifier = Modifier.fillMaxSize()
            )

            if (refreshState is RefreshState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}