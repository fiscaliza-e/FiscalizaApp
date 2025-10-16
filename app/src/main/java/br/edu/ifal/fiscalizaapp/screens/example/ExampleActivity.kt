package br.edu.ifal.fiscalizaapp.screens.example

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.imagepicker.ImagePicker
import br.edu.ifal.fiscalizaapp.composables.textarea.TextArea
import br.edu.ifal.fiscalizaapp.screens.example.composables.exampleComposable.ExampleCard
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme
import br.edu.ifal.fiscalizaapp.ui.theme.SecondaryGreen

@Composable
fun ExampleScreen(modifier: Modifier = Modifier) {
    var uris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    
    val pickImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { result ->
        if (result != null) uris = uris + result
    }

    FiscalizaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "2. Adicione fotos (Opcional)",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = Color(0xFF424242)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ImagePicker(
                    onClick = {
                        pickImages.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    selectedImages = uris,
                    onRemove = { toRemove -> uris = uris.filterNot { it == toRemove } }
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Selecionadas: ${uris.size}")
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    text = "Cadastrar Reclamação",
                    onClick = { /* TODO: Implementar navegação */ },
                    variant = ButtonVariant.Primary,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    text = "Ver Reclamações",
                    onClick = { /* TODO: Implementar navegação */ },
                    variant = ButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    text = "Excluir Reclamações",
                    onClick = { /* TODO: Implementar navegação */ },
                    variant = ButtonVariant.Danger,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    text = "Cadastrar Reclamação",
                    onClick = { /* TODO: Implementar navegação */ },
                    variant = ButtonVariant.Disabled,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    text = "Esqueci minha senha",
                    onClick = { /* TODO: Implementar navegação */ },
                    variant = ButtonVariant.Link(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    text = "Ver todas as categorias",
                    onClick = { /* TODO: Implementar navegação */ },
                    variant = ButtonVariant.Link(),
                    rightIcon = Icons.Default.ChevronRight,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))
                var description by remember { mutableStateOf("") }
                TextArea(
                    value = description,
                    onValueChange = { description = it },
                    label = "Descrição",
                    maxLength = 500
                )

            }
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
