package br.edu.ifal.fiscalizaapp.navigation.screens.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import br.edu.ifal.fiscalizaapp.composables.input.Input
import br.edu.ifal.fiscalizaapp.composables.input.InputStyle
import br.edu.ifal.fiscalizaapp.composables.input.InputType
import br.edu.ifal.fiscalizaapp.composables.input.InputVariant
import br.edu.ifal.fiscalizaapp.composables.input.cepMask
import br.edu.ifal.fiscalizaapp.composables.input.cpfMask
import br.edu.ifal.fiscalizaapp.navigation.routes.loginRoute
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.InsertUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UserViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

data class RegisterFormState(
    val fullName: String = "",
    val cpf: String = "",
    val cep: String = "",
    val street: String = "",
    val neighborhood: String = "",
    val number: String = "",
    val city: String = "",
    val state: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreesToPolicy: Boolean = false
)

@Composable
fun RegisterScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    cepViewModel: CepViewModel = viewModel(factory = ViewModelFactory(LocalContext.current)),
    userViewModel: UserViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    Scaffold(containerColor = Color.White) { innerPadding ->

        var formState by remember { mutableStateOf(RegisterFormState()) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val cepUiState by cepViewModel.uiState.collectAsState()
        val insertState by userViewModel.insertState.collectAsState()

        LaunchedEffect(formState.cep) {
            if (formState.cep.length == 8) {
                cepViewModel.fetchAddressByCep(formState.cep)
            } else {
                cepViewModel.resetCepState()
            }
        }

        LaunchedEffect(cepUiState) {
            when (val state = cepUiState) {
                is CepUiState.Success -> {
                    formState = formState.copy(
                        street = state.address.street,
                        neighborhood = state.address.neighborhood,
                        city = state.address.city,
                        state = state.address.state
                    )
                }
                is CepUiState.Idle, is CepUiState.Error -> {
                    if (state is CepUiState.Idle) {
                        formState = formState.copy(street = "", neighborhood = "", city = "", state = "")
                    }
                }
                is CepUiState.Loading -> { }
            }
        }

        LaunchedEffect(insertState) {
            when (insertState) {
                is InsertUiState.Success -> {
                    userViewModel.resetInsertState()
                    navController.navigate(loginRoute) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                is InsertUiState.Error -> {
                    errorMessage = (insertState as InsertUiState.Error).message
                }
                else -> { }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Crie sua conta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
            Text(
                text = "Preencha seus dados para começar.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Input(
                value = formState.fullName,
                onValueChange = { formState = formState.copy(fullName = it) },
                label = "Nome completo",
                placeholder = "Digite seu nome completo",
                type = InputType.Text,
                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Input(
                value = formState.cpf,
                onValueChange = { formState = formState.copy(cpf = it) },
                label = "CPF",
                placeholder = "000.000.000-00",
                type = InputType.Number,
                mask = ::cpfMask,
                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Input(
                value = formState.cep,
                onValueChange = { formState = formState.copy(cep = it) },
                label = "CEP",
                placeholder = "00000-000",
                type = InputType.Number,
                mask = ::cepMask,
                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
            )

            when (val state = cepUiState) {
                is CepUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is CepUiState.Error -> {
                    Text(text = state.message, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp))
                }
                else -> Spacer(modifier = Modifier.height(16.dp))
            }

            if (cepUiState is CepUiState.Success) {
                Input(
                    value = formState.street,
                    onValueChange = { formState = formState.copy(street = it) },
                    label = "Rua",
                    placeholder = "Digite o nome da rua",
                    type = InputType.Text,
                    style = InputStyle(variant = InputVariant.Primary, heightDp = 52),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Input(
                        modifier = Modifier.weight(1f),
                        value = formState.neighborhood,
                        onValueChange = { formState = formState.copy(neighborhood = it) },
                        label = "Bairro",
                        placeholder = "Bairro",
                        type = InputType.Text,
                        style = InputStyle(variant = InputVariant.Primary, heightDp = 52),
                        enabled = false
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Input(
                        modifier = Modifier.width(100.dp),
                        value = formState.number,
                        onValueChange = { formState = formState.copy(number = it) },
                        label = "Nº",
                        placeholder = "Nº",
                        type = InputType.Text,
                        style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Input(
                        modifier = Modifier.weight(1f),
                        value = formState.city,
                        onValueChange = { formState = formState.copy(city = it) },
                        label = "Cidade",
                        placeholder = "Cidade",
                        type = InputType.Text,
                        style = InputStyle(variant = InputVariant.Primary, heightDp = 52),
                        enabled = false
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Input(
                        modifier = Modifier.width(80.dp),
                        value = formState.state,
                        onValueChange = { formState = formState.copy(state = it) },
                        label = "UF",
                        placeholder = "UF",
                        type = InputType.Text,
                        style = InputStyle(variant = InputVariant.Primary, heightDp = 52),
                        enabled = false
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Input(
                value = formState.email,
                onValueChange = { formState = formState.copy(email = it) },
                label = "E-mail",
                placeholder = "Digite seu e-mail",
                type = InputType.Email,
                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Input(
                value = formState.password,
                onValueChange = { formState = formState.copy(password = it) },
                label = "Senha",
                placeholder = "Digite sua senha",
                type = InputType.Password,
                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Input(
                value = formState.confirmPassword,
                onValueChange = { formState = formState.copy(confirmPassword = it) },
                label = "Confirmar senha",
                placeholder = "Confirme sua senha",
                type = InputType.Password,
                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = formState.agreesToPolicy,
                    onCheckedChange = { formState = formState.copy(agreesToPolicy = it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Li e concordo com a Política de Privacidade.", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (insertState is InsertUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                Button(
                    text = "Cadastrar",
                    onClick = {
                        val validationError = validateForm(formState)
                        if (validationError != null) {
                            errorMessage = validationError
                            return@Button
                        }
                        errorMessage = null
                        userViewModel.insert(formState)
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

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(text = "Já possui uma conta? ", fontSize = 14.sp)
                Text(
                    text = "Entre",
                    fontSize = 14.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate(loginRoute) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun validateForm(formState: RegisterFormState): String? {
    if (formState.fullName.isBlank() || formState.cpf.isBlank() || formState.cep.isBlank() || formState.email.isBlank() || formState.password.isBlank() || formState.number.isBlank()) {
        return "Por favor, preencha todos os campos."
    }
    if (formState.password != formState.confirmPassword) {
        return "As senhas não conferem."
    }
    if (!formState.agreesToPolicy) {
        return "Você deve aceitar a Política de Privacidade."
    }
    return null
}