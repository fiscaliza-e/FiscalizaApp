package br.edu.ifal.fiscalizaapp.screens.example

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button as M3Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.composables.accordion.Accordion
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.card.CardBase
import br.edu.ifal.fiscalizaapp.composables.imagepicker.ImagePicker
import br.edu.ifal.fiscalizaapp.composables.textarea.TextArea
import br.edu.ifal.fiscalizaapp.screens.example.composables.exampleComposable.ExampleCard
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme

@Composable
fun ExampleScreen(modifier: Modifier = Modifier) {
    var uris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val pickImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { result ->
        if (result != null) uris = uris + result
    }

    val faqs = listOf(
        "Há algum custo para utilizar o aplicativo?" to
                "Não, o aplicativo é totalmente gratuito para todos os usuários.",
        "É possível enviar uma solicitação em nome de outra pessoa?" to
                "Para garantir a precisão e segurança, solicitamos que as reclamações sejam feitas em nome do próprio usuário cadastrado. Dessa forma, conseguimos manter a transparência e o histórico de cada solicitação.",
        "Posso acompanhar o andamento da minha solicitação?" to
                "Sim, você poderá acompanhar o status e receber atualizações diretamente pelo aplicativo.",
        "Quem tem acesso às reclamações que eu envio?" to
                "A equipe responsável pelo atendimento tem acesso às informações para análise e resposta. Seus dados são tratados conforme a nossa política de privacidade."
    )
    val expanded = remember { mutableStateListOf<Boolean>().apply { repeat(faqs.size) { add(false) } } }

    val scrollState = rememberScrollState()
    var description by remember { mutableStateOf("") }

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
                            onClick = { /* TODO: Implementar navegação */ },
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

                        CardBase {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                faqs.forEachIndexed { index, (question, answer) ->
                                    Accordion(
                                        question = question,
                                        answer = answer,
                                        expanded = expanded[index],
                                        onToggle = { expanded[index] = !expanded[index] },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    uris = emptyList()
                                    description = ""
                                    for (i in expanded.indices) expanded[i] = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Limpar")
                            }
                            M3Button(
                                onClick = {
                                    // TODO: ação real (salvar/enviar)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Enviar")
                            }
                        }

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