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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.dialog.LogoutDialog
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.composables.protocollist.ProtocolList
import br.edu.ifal.fiscalizaapp.navigation.routes.loginRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.newProtocolRoute
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.RefreshState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProtocolViewModel = viewModel(factory = ViewModelFactory(context))

    val protocols by viewModel.protocols.collectAsStateWithLifecycle(initialValue = emptyList())
    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()

    LaunchedEffect(refreshState) {
        if (refreshState is RefreshState.Error) {
            Toast.makeText(context, (refreshState as RefreshState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

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
            ProtocolList(
                protocols = protocols,
                modifier = Modifier.fillMaxSize(),
                onNewProtocolClick = {
                    navController.navigate(newProtocolRoute)
                }
            )

            if (refreshState is RefreshState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}