package br.edu.ifal.fiscalizaapp.navigation.screens.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.card.CategoryItem
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.setValue
import br.edu.ifal.fiscalizaapp.data.db.DatabaseHelper
import br.edu.ifal.fiscalizaapp.data.db.data.categoryList
import br.edu.ifal.fiscalizaapp.data.db.entities.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CategoryListScreen(navController: NavController) {

    val context = LocalContext.current
    val dao = DatabaseHelper.getInstance(context).categoryDao()
    var categories by remember { mutableStateOf(emptyList<CategoryEntity>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            if (dao.getCount() == 0) {
                dao.insertAll(categoryList)
            }
            categories = dao.getAll()
        }
    }

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
            items(categories) { category ->
                CategoryItem(
                    icon = painterResource(id = category.iconResId),
                    title = category.title,
                    description = category.description,
                    onClick = { }
                )
            }
        }
    }
}