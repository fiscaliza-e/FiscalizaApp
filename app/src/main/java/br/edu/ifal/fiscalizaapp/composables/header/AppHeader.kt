package br.edu.ifal.fiscalizaapp.composables.header

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

enum class AppHeaderType {
    TELA_PRINCIPAL,
    TELA_INTERNA
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    type: AppHeaderType,
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier,
        title = {
            when (type) {
                AppHeaderType.TELA_PRINCIPAL -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logo_app),
                            contentDescription = "Logo do App",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fiscaliza-e",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                AppHeaderType.TELA_INTERNA -> {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        navigationIcon = {
            when (type) {
                AppHeaderType.TELA_INTERNA -> {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = PrimaryGreen
                        )
                    }
                }
                AppHeaderType.TELA_PRINCIPAL -> { }
            }
        },
        actions = {
            when (type) {
                AppHeaderType.TELA_PRINCIPAL -> {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            tint = PrimaryGreen
                        )
                    }
                }
                AppHeaderType.TELA_INTERNA -> { }
            }
        }
    )
}

@Preview(showBackground = true, name = "Header: Tela Principal")
@Composable
fun AppHeaderPreview_MainScreen() {
    FiscalizaTheme {
        AppHeader(
            type = AppHeaderType.TELA_PRINCIPAL,
            onActionClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Header: Tela Interna")
@Composable
fun AppHeaderPreview_InternalScreen() {
    FiscalizaTheme {
        AppHeader(
            type = AppHeaderType.TELA_INTERNA,
            title = "Detalhes do Protocolo",
            onBackClick = {}
        )
    }
}