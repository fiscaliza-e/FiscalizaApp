package br.edu.ifal.fiscalizaapp.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.composables.card.CategoryCard

data class Categories(@DrawableRes val icon: Int, val text: String)

private val categories = listOf(
    Categories(icon = R.drawable.ic_buraco_na_via, text = "Buraco na Via"),
    Categories(icon = R.drawable.ic_poste, text = "Poste sem Luz"),
    Categories(icon = R.drawable.ic_lixo_acumulado, text = "Lixo Acumulado"),
    Categories(icon = R.drawable.ic_semaforo_apagado, text = "Semáforo Apagado"),

)
@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    modifier = Modifier.defaultMinSize(100.dp),
                    icon = painterResource(id = category.icon),
                    text = category.text,
                    onClick = { navController.navigate("example") }
                )
            }
        }
    }
}



