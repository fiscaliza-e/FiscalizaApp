package br.edu.ifal.fiscalizaapp.composables.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = PrimaryGreen
            ),
            windowInsets = WindowInsets(0, 0, 0, 0),
            title = {
                when (type) {
                    AppHeaderType.MAIN_SCREEN -> {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logo_app),
                                contentDescription = "Logo do App",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Fiscaliza-e",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    AppHeaderType.INTERN_SCREEN -> {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            navigationIcon = {
                when (type) {
                    AppHeaderType.INTERN_SCREEN -> {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Voltar",
                                tint = PrimaryGreen
                            )
                        }
                    }
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
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
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