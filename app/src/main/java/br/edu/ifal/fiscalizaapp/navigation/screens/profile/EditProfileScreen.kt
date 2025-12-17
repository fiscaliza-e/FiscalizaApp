package br.edu.ifal.fiscalizaapp.navigation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.composables.input.Input
import br.edu.ifal.fiscalizaapp.composables.input.InputStyle
import br.edu.ifal.fiscalizaapp.composables.input.InputType
import br.edu.ifal.fiscalizaapp.composables.input.InputVariant
import br.edu.ifal.fiscalizaapp.composables.input.cepMask
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.HomeViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UpdateProfileState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

@Composable
fun EditProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current)),
    cepViewModel: CepViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val updateState by homeViewModel.updateState.collectAsState()
    val cepUiState by cepViewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var uf by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Dispara busca de endereço quando o CEP muda
    LaunchedEffect(cep) {
        if (cep.length == 8) {
            cepViewModel.fetchAddressByCep(cep)
        } else {
            cepViewModel.resetCepState()
        }
    }

    // Atualiza campos de endereço quando a API de CEP retorna
    LaunchedEffect(cepUiState) {
        when (val state = cepUiState) {
            is CepUiState.Success -> {
                street = state.address.street
                neighborhood = state.address.neighborhood
                city = state.address.city
                uf = state.address.state
            }
            else -> Unit
        }
    }

    // Carrega os dados do usuário quando a tela é exibida
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is UiState.Success -> {
                val user = state.data
                name = user.name

                val addr = user.address
                if (addr.isNotBlank()) {
                    // Tenta quebrar o endereço no formato:
                    // Rua, Número, Bairro - Cidade/UF
                    val hyphenParts = addr.split(" - ", limit = 2)
                    val beforeHyphen = hyphenParts[0]
                    val afterHyphen = hyphenParts.getOrNull(1)

                    val beforeParts = beforeHyphen.split(",").map { it.trim() }
                    street = beforeParts.getOrNull(0) ?: ""
                    number = beforeParts.getOrNull(1) ?: ""
                    neighborhood = beforeParts.getOrNull(2) ?: neighborhood

                    if (afterHyphen != null) {
                        val cityStateParts = afterHyphen.split("/", limit = 2).map { it.trim() }
                        if (cityStateParts.size == 2) {
                            city = cityStateParts[0]
                            uf = cityStateParts[1]
                        } else {
                            // Caso só tenha bairro ou cidade depois do hífen
                            if (neighborhood.isBlank()) {
                                neighborhood = afterHyphen.trim()
                            } else {
                                city = afterHyphen.trim()
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }

    // Trata o resultado da atualização
    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is UpdateProfileState.Success -> {
                homeViewModel.resetUpdateState()
                navController.popBackStack()
            }
            is UpdateProfileState.Error -> {
                errorMessage = state.message
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                type = AppHeaderType.INTERN_SCREEN,
                title = "Editar Perfil",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
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
                when (val state = uiState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Error -> {
                        Text(
                            text = state.message,
                            color = Color.Red,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    is UiState.Success -> {
                        Input(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nome completo",
                            placeholder = "Digite seu nome completo",
                            type = InputType.Text,
                            style = InputStyle(variant = InputVariant.Primary, heightDp = 52),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // CEP
                        Input(
                            value = cep,
                            onValueChange = { cep = it },
                            label = "CEP",
                            placeholder = "00000-000",
                            type = InputType.Number,
                            mask = ::cepMask,
                            style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
                        )

                        when (val cepState = cepUiState) {
                            is CepUiState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            is CepUiState.Error -> {
                                Text(
                                    text = cepState.message,
                                    color = Color.Red,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            else -> {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        // Rua
                        Input(
                            value = street,
                            onValueChange = { street = it },
                            label = "Rua",
                            placeholder = "Digite o nome da rua",
                            type = InputType.Text,
                            style = InputStyle(variant = InputVariant.Primary, heightDp = 52),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Bairro e Número
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Input(
                                modifier = Modifier.weight(1f),
                                value = neighborhood,
                                onValueChange = { neighborhood = it },
                                label = "Bairro",
                                placeholder = "Bairro",
                                type = InputType.Text,
                                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Input(
                                modifier = Modifier.width(100.dp),
                                value = number,
                                onValueChange = { number = it },
                                label = "Nº",
                                placeholder = "Nº",
                                type = InputType.Text,
                                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Cidade e UF
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Input(
                                modifier = Modifier.weight(1f),
                                value = city,
                                onValueChange = { city = it },
                                label = "Cidade",
                                placeholder = "Cidade",
                                type = InputType.Text,
                                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Input(
                                modifier = Modifier.width(80.dp),
                                value = uf,
                                onValueChange = { uf = it },
                                label = "UF",
                                placeholder = "UF",
                                type = InputType.Text,
                                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (updateState is UpdateProfileState.Loading) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            Button(
                                text = "Salvar alterações",
                                onClick = {
                                    if (name.isBlank()) {
                                        errorMessage = "O nome é obrigatório"
                                        return@Button
                                    }

                                    if (street.isBlank() || number.isBlank() || neighborhood.isBlank() || city.isBlank() || uf.isBlank()) {
                                        errorMessage = "Preencha todos os campos de endereço"
                                        return@Button
                                    }

                                    val fullAddress = "${street.trim()}, ${number.trim()}, ${neighborhood.trim()} - ${city.trim()}/${uf.trim()}"

                                    errorMessage = null
                                    homeViewModel.updateProfile(name, fullAddress)
                                },
                                variant = ButtonVariant.Primary,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

