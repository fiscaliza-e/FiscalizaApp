package br.edu.ifal.fiscalizaapp.screens.example

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.screens.example.composables.exampleComposable.ExampleCard
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme


@Composable
fun ExampleScreen(modifier: Modifier = Modifier) {
    FiscalizaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ExampleCard(
                title = "Fiscaliza-E",
                body = "Clique para cadastrar uma reclamação.",
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CardPreview() {
    FiscalizaTheme {
        ExampleCard(
            title = "Visualização Prévia",
            body = "Isto é uma pré-visualização de como o seu Card ficará."
        )
    }
}