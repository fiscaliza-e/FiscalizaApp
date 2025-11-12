import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.accordion.AccordionItem
import br.edu.ifal.fiscalizaapp.composables.accordion.AccordionList
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.FaqViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

@Composable
fun FaqScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FaqViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            Text(
                text = "Estamos aqui para te ajudar",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Encontre respostas para as perguntas mais comuns.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
            )

            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Qual a sua dúvida?",
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is UiState.Success -> {
                        val allApiItems = state.data

                        val filteredApiItems = if (searchQuery.isBlank()) {
                            allApiItems
                        } else {
                            allApiItems.filter {
                                it.question.contains(searchQuery, ignoreCase = true) ||
                                        it.answer.contains(searchQuery, ignoreCase = true)
                            }
                        }

                        val accordionItemsToShow = filteredApiItems.map { apiItem ->
                            AccordionItem(
                                question = apiItem.question,
                                answer = apiItem.answer
                            )
                        }

                        AccordionList(
                            items = accordionItemsToShow,
                            modifier = Modifier.fillMaxWidth(),
                            itemSpacing = 12
                        )

                    }

                    is UiState.Error -> {
                        Text(text = "Erro ao carregar FAQ: ${state.message}")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(16.dp))
            ContactUsText(
                onClicked = {}
            )
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun ContactUsText(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("Não encontrou sua resposta? ")
                }
                withStyle(style = SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                    append("Fale conosco")
                }
            },
            modifier = Modifier.clickable { onClicked() }
        )
    }
}

