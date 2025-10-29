package br.edu.ifal.fiscalizaapp.composables.header

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
    MAIN_SCREEN,
    INTERN_SCREEN
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
            containerColor = Color.White
        ),
        modifier = modifier,
        title = {
            when (type) {
                AppHeaderType.MAIN_SCREEN -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logo_app),
                            contentDescription = "Logo do App",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(
                            text = "Fiscaliza-e",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                AppHeaderType.INTERN_SCREEN -> {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onBackClick,
                            modifier = Modifier.offset(x = (-12).dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Voltar",
                                tint = PrimaryGreen
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        navigationIcon = {
            when (type) {
                AppHeaderType.INTERN_SCREEN -> { }
                AppHeaderType.MAIN_SCREEN -> { }
            }
        },
        actions = {
            when (type) {
                AppHeaderType.MAIN_SCREEN -> {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_exit_button),
                            modifier = Modifier.size(21.dp),
                            contentDescription = "Sair",
                            tint = PrimaryGreen
                        )
                    }
                }
                AppHeaderType.INTERN_SCREEN -> { }
            }
        }
    )
}

@Preview(showBackground = true, name = "Header: Tela Principal")
@Composable
fun AppHeaderPreview_MainScreen() {
    FiscalizaTheme {
        AppHeader(
            type = AppHeaderType.MAIN_SCREEN,
            onActionClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Header: Tela Interna")
@Composable
fun AppHeaderPreview_InternalScreen() {
    FiscalizaTheme {
        AppHeader(
            type = AppHeaderType.INTERN_SCREEN,
            title = "Detalhes do Protocolo",
            onBackClick = {}
        )
    }
}