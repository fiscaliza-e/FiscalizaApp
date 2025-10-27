package br.edu.ifal.fiscalizaapp.screens.categories

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.composables.card.CategoryItem
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType

data class CategoryInfo(
    @DrawableRes val icon: Int,
    val title: String,
    val description: String
)

private val categoriesList = listOf(
    CategoryInfo(
        icon = R.drawable.ic_poste,
        title = "Iluminação",
        description = "Postes apagados, lâmpadas queimadas ou piscando, e problemas na iluminação de praças e parques.",
    ),
    CategoryInfo(
        icon = R.drawable.ic_pavimentacao,
        title = "Pavimentação",
        description = "Buracos, asfalto irregular, lajotas soltas ou problemas de drenagem em ruas e avenidas."
    ),
    CategoryInfo(
        icon = R.drawable.ic_saneamento,
        title = "Saneamento",
        description = "Vazamentos de água ou esgoto na rua, bueiros entupidos ou falta de coleta de esgoto.",
    ),
    CategoryInfo(
        icon = R.drawable.ic_orgaos_publicos,
        title = "Órgãos públicos",
        description = "Problemas no atendimento ou na infraestrutura de prédios da prefeitura, postos de saúde e escolas.",
    ),
    CategoryInfo(
        icon = R.drawable.ic_transporte_coletivo,
        title = "Transporte coletivo",
        description = "Atrasos, superlotação, estado de conservação dos ônibus e problemas em pontos de parada.",
    ),
    CategoryInfo(
        icon = R.drawable.ic_espaco_publico,
        title = "Espaço público",
        description = "Manutenção de praças, parques, calçadas, terrenos baldios e outras áreas de uso comum.",
    ),
    CategoryInfo(
        icon = R.drawable.ic_semaforo_apagado,
        title = "Trânsito",
        description = "Semáforos com defeito, falta de sinalização, ou problemas na organização do tráfego de veículos.",
    ),
    CategoryInfo(
        icon = R.drawable.ic_outras_categorias,
        title = "Outros",
        description = "Não encontrou o seu problema? Descreva sua solicitação nesta categoria.",
    )
)

@Composable
fun CategoryListScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            AppHeader(
                type = AppHeaderType.INTERN_SCREEN,
                title = "Todas as Categorias",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categoriesList) { category ->
                CategoryItem(
                    icon = painterResource(id = category.icon),
                    title = category.title,
                    description = category.description,
                    onClick = { }
                )
            }
        }
    }
}