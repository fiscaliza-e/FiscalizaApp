package br.edu.ifal.fiscalizaapp.navigation.screens.protocols

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.dialog.LogoutDialog
import br.edu.ifal.fiscalizaapp.composables.snackbar.FiscalizaSnackbarHost
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.composables.protocollist.ProtocolList
import br.edu.ifal.fiscalizaapp.navigation.routes.loginRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.navigateToProtocolDetail
import br.edu.ifal.fiscalizaapp.navigation.routes.newProtocolRoute
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.RefreshState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolScreen(
    navController: NavController,
    showCreated: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProtocolViewModel = viewModel(factory = ViewModelFactory(context))

    val protocols by viewModel.filteredProtocols.collectAsStateWithLifecycle(initialValue = emptyList())
    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()
    val filterStatus by viewModel.filterStatus.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (showCreated) {
            snackbarHostState.showSnackbar("SUCCESS:Protocolo enviado com sucesso!")
        }
    }

    LaunchedEffect(refreshState) {
        if (refreshState is RefreshState.Error) {
            snackbarHostState.showSnackbar("ERROR:${(refreshState as RefreshState.Error).message}")
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
        topBar = {
            AppHeader(
                type = AppHeaderType.MAIN_SCREEN,
                onActionClick = {
                    showLogoutDialog = true
                }
            )
        },
        snackbarHost = { FiscalizaSnackbarHost(snackbarHostState) }
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
                filterStatus = filterStatus,
                sortOrder = sortOrder,
                onFilterChange = viewModel::setFilterStatus,
                onSortChange = viewModel::setSortOrder,
                onNewProtocolClick = {
                    navController.navigate(newProtocolRoute)
                },
                onProtocolClick = { protocolNumber ->
                    navController.navigateToProtocolDetail(protocolNumber)
                }
            )

            if (refreshState is RefreshState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}
