package br.edu.ifal.fiscalizaapp.navigation.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity
import br.edu.ifal.fiscalizaapp.ui.viewmodels.HomeViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UserUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UserViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory
import coil.compose.AsyncImage

@Composable
fun ChooseAvatarScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(factory = ViewModelFactory(LocalContext.current)),
    homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val uiState by userViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchUsers()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                type = AppHeaderType.INTERN_SCREEN,
                title = "Escolher foto",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = Color.White
        ) {
            when (val state = uiState) {
                is UserUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UserUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }
                }

                is UserUiState.Success -> {
                    val users = state.users.filter { !it.profileImage.isNullOrBlank() }

                    if (users.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhuma foto disponível.",
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(users) { user ->
                                AvatarItem(
                                    user = user,
                                    onClick = {
                                        val apiId = user.apiId ?: user.id
                                        homeViewModel.updateProfilePicture(apiId)
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AvatarItem(
    user: UserEntity,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.LightGray.copy(alpha = 0.15f),
        shadowElevation = 2.dp
    ) {
        AsyncImage(
            model = user.profileImage,
            contentDescription = user.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}


