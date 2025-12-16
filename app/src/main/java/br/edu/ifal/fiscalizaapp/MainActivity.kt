package br.edu.ifal.fiscalizaapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.model.BottomNavBarItem
import br.edu.ifal.fiscalizaapp.navigation.routes.AppNavHost
import br.edu.ifal.fiscalizaapp.navigation.routes.faqRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.homeRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.loginRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.profileRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.protocolRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.registerRoute
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme
import br.edu.ifal.fiscalizaapp.ui.theme.DarkGray
import br.edu.ifal.fiscalizaapp.ui.theme.LightGray
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.HomeViewModel
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {

    private val bottomNavBarItems = listOf(
        BottomNavBarItem(
            label = "Início",
            icon = Icons.Default.Home,
            route = homeRoute
        ),
        BottomNavBarItem(
            label = "Protocolos",
            icon = Icons.Default.Description,
            route = protocolRoute
        ),
        BottomNavBarItem(
            label = "Perfil",
            icon = Icons.Default.Person,
            route = profileRoute
        ),
        BottomNavBarItem(
            label = "FAQ",
            icon = Icons.Outlined.Info,
            route = faqRoute
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FiscalizaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    var selectedItem by remember {
                        mutableStateOf(bottomNavBarItems.first())
                    }

                    val homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
                    val userUiState by homeViewModel.uiState.collectAsState()

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val standaloneRoutes = listOf(loginRoute, registerRoute)
                    val shouldShowBars = currentRoute != null && currentRoute !in standaloneRoutes

                    FiscalizaApp(
                        shouldShowBars = shouldShowBars,
                        selectedItem = selectedItem,
                        userUiState = userUiState,
                        onBottomNavBarItemChange = { item ->
                            selectedItem = item
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        }
                    ) {
                        AppNavHost(navController)
                    }
                }
            }
        }
    }

    @Composable
    fun FiscalizaApp(
        modifier: Modifier = Modifier,
        shouldShowBars: Boolean, 
        selectedItem: BottomNavBarItem,
        userUiState: UiState<*>, 
        onBottomNavBarItemChange: (BottomNavBarItem) -> Unit,
        content: @Composable () -> Unit
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                    AppHeader(AppHeaderType.MAIN_SCREEN)
            },
            bottomBar = {
                if (shouldShowBars && userUiState is UiState.Success) {
                    BottomNavBar(
                        modifier = Modifier,
                        selectedItem = selectedItem,
                        onItemChanged = onBottomNavBarItemChange
                    )
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding)
            ) {
                content()
            }
        }
    }

    @Composable
    fun BottomNavBar(
        modifier: Modifier = Modifier,
        selectedItem: BottomNavBarItem,
        onItemChanged: (BottomNavBarItem) -> Unit,
    ) {
        NavigationBar(
            containerColor = Color.White
        ) {
            bottomNavBarItems.forEach { item ->
                NavigationBarItem(
                    enabled = true,
                    selected = selectedItem.label == item.label,
                    onClick = {
                        onItemChanged(item)
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = ""
                        )
                    },
                    label = { Text(item.label) },
                    colors = NavigationBarItemColors(
                        selectedIconColor = PrimaryGreen,
                        selectedTextColor = PrimaryGreen,
                        selectedIndicatorColor = Color.Transparent,
                        unselectedIconColor = DarkGray,
                        unselectedTextColor = DarkGray,
                        disabledIconColor = LightGray,
                        disabledTextColor = LightGray
                    )
                )
            }
        }
    }
}