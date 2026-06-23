package br.edu.ifal.fiscalizaapp.navigation.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.card.CategoryCard
import br.edu.ifal.fiscalizaapp.composables.dialog.LogoutDialog
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.navigation.routes.categoryListRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.loginRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.newProtocolRoute
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.HomeViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory
import br.edu.ifal.fiscalizaapp.ui.state.UiState

data class Categories(
    val id: Int,
    @DrawableRes val icon: Int,
    val text: String
)

private val categories = listOf(
    Categories(id = 2, icon = R.drawable.ic_buraco_na_via, text = "Buraco na Via"),
    Categories(id = 1, icon = R.drawable.ic_poste, text = "Poste sem Luz"),
    Categories(id = 3, icon = R.drawable.ic_lixo_acumulado, text = "Lixo Acumulado"),
    Categories(id = 7, icon = R.drawable.ic_semaforo_apagado, text = "Semáforo Apagado"),
)

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val userUiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshUser()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item(span = { GridItemSpan(2) }) {
                val greetingName = when (val state = userUiState) {
                    is UiState.Loading -> ""
                    is UiState.Success -> state.data.name
                    is UiState.Error -> ""
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryGreen
                ) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                        Text(
                            text = if (greetingName.isNotEmpty()) "Olá, $greetingName!" else "Olá!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "O que você quer reportar hoje?",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            item(span = { GridItemSpan(2) }) {
                Button(
                    text = "Fazer Nova Reclamação",
                    onClick = {
                        navController.navigate(newProtocolRoute)
                    },
                    variant = ButtonVariant.Primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item(span = { GridItemSpan(2) }) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Reclamações Frequentes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            items(categories) { category ->
                CategoryCard(
                    modifier = Modifier.defaultMinSize(100.dp),
                    icon = painterResource(id = category.icon),
                    text = category.text,
                    onClick = {
                        navController.navigate("$newProtocolRoute?categoryId=${category.id}")
                    }
                )
            }

            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        text = "Ver todas as categorias",
                        onClick = {
                            navController.navigate(categoryListRoute)
                        },
                        variant = ButtonVariant.Link(color = PrimaryGreen),
                        rightIcon = Icons.Default.ChevronRight,
                        modifier = Modifier
                            .padding(top = 24.dp)
                    )
                }
            }
        }
    }
}
