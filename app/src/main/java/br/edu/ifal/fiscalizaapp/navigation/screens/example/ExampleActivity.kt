package br.edu.ifal.fiscalizaapp.navigation.screens.example

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.accordion.AccordionList
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.imagepicker.ImagePicker
import br.edu.ifal.fiscalizaapp.composables.textarea.TextArea
import br.edu.ifal.fiscalizaapp.model.AccordionItem
import br.edu.ifal.fiscalizaapp.navigation.screens.example.composables.exampleComposable.ExampleCard
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme

@Composable
fun ExampleScreen(navController: NavController, modifier: Modifier = Modifier) {
    var uris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var description by remember { mutableStateOf("") }

    val pickImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { result ->
        if (result != null) uris = uris + result
    }

    val faqItems = listOf(
        AccordionItem(
            question = "Há algum custo para utilizar o aplicativo?",
            answer = "Não, o aplicativo é totalmente gratuito para todos os usuários."
        ),
        AccordionItem(
            question = "É possível enviar uma solicitação em nome de outra pessoa?",
            answer = "Para garantir a precisão e segurança, solicitamos que as reclamações sejam feitas em nome do próprio usuário cadastrado. Dessa forma, conseguimos manter a transparência e o histórico de cada solicitação."
        ),
        AccordionItem(
            question = "Posso acompanhar o andamento da minha solicitação?",
            answer = "Sim, você poderá acompanhar o status e receber atualizações diretamente pelo aplicativo."
        ),
        AccordionItem(
            question = "Quem tem acesso às reclamações que eu envio?",
            answer = "A equipe responsável pelo atendimento tem acesso às informações para análise e resposta. Seus dados são tratados conforme a nossa política de privacidade."
        )
    )

    val scrollState = rememberScrollState()

    FiscalizaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Exemplo de Protocolo",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "2. Adicione fotos (Opcional)",
                            style = MaterialTheme.typography.titleMedium,
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
                            onClick = {
                                navController.navigate("categories")
                            },
                            variant = ButtonVariant.Link(),
                            rightIcon = Icons.Default.ChevronRight,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        TextArea(
                            value = description,
                            onValueChange = { description = it },
                            label = "Descrição",
                            maxLength = 500
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "3. FAQ - Accordion",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(8.dp))

                        AccordionList(
                            items = faqItems,
                            modifier = Modifier.fillMaxWidth(),
                            itemSpacing = 12
                        )

                        Spacer(Modifier.height(24.dp))
                    }
                }
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