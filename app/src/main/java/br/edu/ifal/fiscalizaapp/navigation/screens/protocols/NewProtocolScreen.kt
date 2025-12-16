package br.edu.ifal.fiscalizaapp.navigation.screens.protocols

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.dropdown.Dropdown
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.composables.imagepicker.ImagePicker
import br.edu.ifal.fiscalizaapp.composables.input.Input
import br.edu.ifal.fiscalizaapp.composables.input.InputType
import br.edu.ifal.fiscalizaapp.composables.input.cepMask
import br.edu.ifal.fiscalizaapp.composables.textarea.TextArea
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CategoryUiModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CategoryUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.InsertUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.NewProtocolViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

@Composable
fun NewProtocolScreen(
    navController: NavController,
    categoryIdFromRoute: Int = -1,
    modifier: Modifier = Modifier,
    viewModel: NewProtocolViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf<CategoryUiModel?>(null) }
    var description by remember { mutableStateOf("") }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    var useMyLocation by remember { mutableStateOf(false) }

    var cep by remember { mutableStateOf("") }
    var rua by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var pontoReferencia by remember { mutableStateOf("") }

    val categoryUiState by viewModel.categoryUiState.collectAsState()
    val insertUiState by viewModel.insertUiState.collectAsState()
    val preSelectedCategory by viewModel.preSelectedCategory.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategories(preSelectedId = categoryIdFromRoute)
    }

    LaunchedEffect(preSelectedCategory) {
        if (preSelectedCategory != null) {
            selectedCategory = preSelectedCategory
        }
    }

    LaunchedEffect(insertUiState) {
        when (val state = insertUiState) {
            is InsertUiState.Success -> {
                Toast.makeText(context, "Protocolo inserido com sucesso!", Toast.LENGTH_SHORT)
                    .show()
                navController.popBackStack()
            }

            is InsertUiState.Error -> {
                Toast.makeText(context, "Erro: ${state.message}", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            AppHeader(
                type = AppHeaderType.INTERN_SCREEN,
                title = "Nova Reclamação",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            val isFormValid by remember(
                selectedCategory,
                description,
                useMyLocation,
                cep,
                rua,
                bairro,
                numero
            ) {
                mutableStateOf(
                    selectedCategory != null && description.isNotBlank() &&
                            (useMyLocation || (cep.isNotBlank() && rua.isNotBlank() && bairro.isNotBlank() && numero.isNotBlank()))
                )
            }

            Button(
                onClick = {
                    viewModel.saveProtocol(
                        selectedCategory = selectedCategory,
                        description = description,
                        useMyLocation = useMyLocation,
                        cep = cep,
                        rua = rua,
                        bairro = bairro,
                        numero = numero,
                        pontoReferencia = pontoReferencia
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = insertUiState != InsertUiState.Loading && isFormValid,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (insertUiState == InsertUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Enviar Reclamação")
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SectionTitle(text = "1. Sobre o problema")
            Text(
                text = "Categoria",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (val state = categoryUiState) {
                is CategoryUiState.Loading -> {
                    OutlinedTextField(
                        value = "Carregando categorias...",
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is CategoryUiState.Success -> {
                    Dropdown(
                        expanded = isCategoryDropdownExpanded,
                        onExpandedChange = { isCategoryDropdownExpanded = it },
                        options = state.categories.map { it.title },
                        selectedOption = selectedCategory?.title,
                        onOptionSelected = { selectedTitle ->
                            selectedCategory = state.categories.find { it.title == selectedTitle }
                            isCategoryDropdownExpanded = false
                        }
                    )
                }

                is CategoryUiState.Error -> {
                    Text(text = "Erro ao carregar categorias: ${state.message}", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextArea(
                label = "Descrição",
                value = description,
                onValueChange = { if (it.length <= 500) description = it },
                placeholderText = "Descreva com detalhes o que está acontecendo. Ex: Existe um grande buraco na rua em frente ao número 123 que está causando transtornos."
            )
            Text(
                text = "${description.length}/500",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )


            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = "2. Adicione fotos (Opcional)")
            Spacer(modifier = Modifier.height(8.dp))
            ImagePicker(
                onClick = {
                    Toast
                        .makeText(context, "Abrir câmera/galeria...", Toast.LENGTH_SHORT)
                        .show()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = "3. Localização do problema")

            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = useMyLocation,
                        onClick = { useMyLocation = true }
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = useMyLocation,
                    onClick = { useMyLocation = true }
                )
                Text("Usar minha localização atual", color = Color.Black)
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = !useMyLocation,
                        onClick = { useMyLocation = false }
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = !useMyLocation,
                    onClick = { useMyLocation = false }
                )
                Text("Preencher manualmente", color = Color.Black)
            }


            Spacer(modifier = Modifier.height(16.dp))
            Input(
                label = "CEP",
                value = cep,
                onValueChange = { cep = it },
                type = InputType.Number,
                mask = ::cepMask,
                enabled = !useMyLocation,
                placeholder = "00000-000"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Input(
                label = "Rua",
                value = rua,
                onValueChange = { rua = it },
                type = InputType.Text,
                enabled = !useMyLocation,
                placeholder = "Digite o nome da rua"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Input(
                    label = "Bairro",
                    value = bairro,
                    onValueChange = { bairro = it },
                    modifier = Modifier.weight(1f),
                    type = InputType.Text,
                    enabled = !useMyLocation,
                    placeholder = "Digite o nome do bairro"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Input(
                    label = "Nº",
                    value = numero,
                    onValueChange = { numero = it },
                    modifier = Modifier.width(100.dp),
                    type = InputType.Number,
                    enabled = !useMyLocation,
                    placeholder = "Nº / S/N"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Input(
                label = "Ponto de referência (Opcional)",
                value = pontoReferencia,
                onValueChange = { pontoReferencia = it },
                type = InputType.Text,
                enabled = !useMyLocation,
                placeholder = "Digite o ponto de referência"
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}