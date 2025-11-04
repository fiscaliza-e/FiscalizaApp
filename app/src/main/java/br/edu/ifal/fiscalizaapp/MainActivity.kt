package br.edu.ifal.fiscalizaapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.model.BottomNavBarItem
import br.edu.ifal.fiscalizaapp.navigation.AppNavHost
import br.edu.ifal.fiscalizaapp.navigation.exampleRoute
import br.edu.ifal.fiscalizaapp.navigation.faqRoute
import br.edu.ifal.fiscalizaapp.navigation.homeRoute
import br.edu.ifal.fiscalizaapp.navigation.loginRoute
import br.edu.ifal.fiscalizaapp.navigation.protocolRoute
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme
import br.edu.ifal.fiscalizaapp.ui.theme.DarkGray
import br.edu.ifal.fiscalizaapp.ui.theme.LightGray
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {

    val bottomNavBarItems = listOf(
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
            route = exampleRoute
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
                        val item = bottomNavBarItems.first()
                        mutableStateOf(item)
                    }
                    FiscalizaApp(
                        selectedItem = selectedItem,
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
        selectedItem: BottomNavBarItem,
        onBottomNavBarItemChange: (BottomNavBarItem) -> Unit,
        content: @Composable () -> Unit
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                AppHeader(AppHeaderType.MAIN_SCREEN) },
            bottomBar = {
                BottomNavBar(
                    modifier = Modifier,
                    selectedItem = selectedItem,
                    onItemChanged = onBottomNavBarItemChange
                )
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
                    selected = selectedItem.label == item.label,
                    onClick = {
                        Log.i("TAG", item.label)
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