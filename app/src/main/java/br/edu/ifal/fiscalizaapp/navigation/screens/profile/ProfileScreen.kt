package br.edu.ifal.fiscalizaapp.navigation.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.card.CardBase
import br.edu.ifal.fiscalizaapp.data.db.DatabaseHelper
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    val context = LocalContext.current
    val dao = DatabaseHelper.getInstance(context).userDao()
    var user by remember { mutableStateOf<UserEntity?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            // TODO: Get the user logged in
            user = dao.getUser()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Meu Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        color = PrimaryGreen.copy(alpha = 0.2f)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Foto de perfil",
                                tint = PrimaryGreen,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                    
                    Surface(
                        modifier = Modifier
                            .offset(x = 40.dp, y = 40.dp)
                            .size(36.dp),
                        shape = CircleShape,
                        color = PrimaryGreen
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Alterar foto",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                user?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = it.getMaskedCpf(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 32.dp)
                    )
                }

                CardBase(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Meus Dados",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            Text(
                                text = "Editar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PrimaryGreen.copy(alpha = 0.5f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        user?.let {
                            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                                Text(
                                    text = "Nome completo",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Column {
                                Text(
                                    text = "Endereço",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = it.address.ifEmpty { "Não informado" },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Button(
                    text = "Sair do aplicativo",
                    onClick = onLogout,
                    variant = ButtonVariant.Danger,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Excluir minha conta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onDeleteAccount() }
                        .padding(bottom = 32.dp)
                )
            }
        }
    }
}

