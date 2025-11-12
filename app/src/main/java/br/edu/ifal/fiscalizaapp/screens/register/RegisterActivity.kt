package br.edu.ifal.fiscalizaapp.screens.register

import android.content.Context
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
import br.edu.ifal.fiscalizaapp.navigation.loginRoute
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UserUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.UserViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private data class RegisterFormState(
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
        var isLoadingSubmit by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val cepUiState by cepViewModel.uiState.collectAsState()
        val userUiState by userViewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

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
                    formState = formState.copy(
                        street = "",
                        neighborhood = "",
                        city = "",
                        state = ""
                    )
                }
                is CepUiState.Loading -> {
                }
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
                        text = state.message,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }
                else -> {
                    Spacer(modifier = Modifier.height(16.dp))
                }
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Input(
                        modifier = Modifier.weight(1f),
                        value = formState.neighborhood,
                        onValueChange = { formState = formState.copy(neighborhood = it) },
                        label = "Bairro",
                        placeholder = "Digite o nome do bairro",
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
                        placeholder = "N° / S/N",
                        type = InputType.Text,
                        style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Input(
                        modifier = Modifier.weight(1f),
                        value = formState.city,
                        onValueChange = { formState = formState.copy(city = it) },
                        label = "Cidade",
                        placeholder = "Arapiraca",
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
                        placeholder = "AL",
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
                placeholder = "Digite sua senha novamente",
                type = InputType.Password,
                style = InputStyle(variant = InputVariant.Primary, heightDp = 52)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = formState.agreesToPolicy,
                    onCheckedChange = { formState = formState.copy(agreesToPolicy = it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Li e concordo com a Política de Privacidade.",
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoadingSubmit) {
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

                        handleSubmitRegistration(
                            scope = scope,
                            context = context,
                            formState = formState,
                            userUiState = userUiState,
                            setIsLoading = { isLoadingSubmit = it },
                            setErrorMessage = { errorMessage = it },
                            onSuccess = {
                                navController.navigate(loginRoute) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
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
                Text(
                    text = "Já possui uma conta? ",
                    fontSize = 14.sp
                )
                Text(
                    text = "Entre",
                    fontSize = 14.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(loginRoute)
                    }
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

private fun handleSubmitRegistration(
    scope: CoroutineScope,
    context: Context,
    formState: RegisterFormState,
    userUiState: UserUiState,
    setIsLoading: (Boolean) -> Unit,
    setErrorMessage: (String?) -> Unit,
    onSuccess: () -> Unit
) {
    setIsLoading(true)
    setErrorMessage(null)

    val tag = "RegisterCheck"
    val formEmailCleaned = formState.email.trim()
    val formCpfCleaned = formState.cpf.filter { it.isDigit() }

    when (val state = userUiState) {
        is UserUiState.Loading -> {
            setErrorMessage("Aguarde o carregamento dos usuários.")
            setIsLoading(false)
        }

        is UserUiState.Error -> {
            setErrorMessage("Erro ao carregar usuários: ${state.message}")
            setIsLoading(false)
        }

        is UserUiState.Success -> {

            scope.launch(Dispatchers.IO) {
                var shouldNavigateToLogin = false
                var finalErrorMessage: String? = null

                try {
                    val db = br.edu.ifal.fiscalizaapp.db.DatabaseHelper.getInstance(context)
                    val userDao = db.userDao()

                    val apiUserEntities = state.users.map { apiUser ->
                        br.edu.ifal.fiscalizaapp.model.UserEntity(
                            id = 0,
                            apiId = apiUser.id,
                            name = apiUser.name,
                            cpf = apiUser.cpf.filter { it.isDigit() },
                            email = apiUser.email.trim(),
                            password = "12345678",
                            address = "test test test"
                        )
                    }

                    var syncCount = 0
                    for (apiUser in apiUserEntities) {
                        val existing = userDao.getUserByApiId(apiUser.apiId!!)
                        if (existing == null) {
                            userDao.insertUser(apiUser)
                            syncCount++
                        }
                    }

                    val existingByEmail = userDao.getUserByEmail(formEmailCleaned)
                    val existingByCpf = userDao.getUserByCpf(formCpfCleaned)

                    if (existingByEmail != null || existingByCpf != null) {
                        finalErrorMessage = "Este Email ou CPF já está cadastrado."
                    } else {
                        val newUser = br.edu.ifal.fiscalizaapp.model.UserEntity(
                            id = 0,
                            apiId = null,
                            name = formState.fullName.trim(),
                            cpf = formCpfCleaned,
                            email = formEmailCleaned,
                            password = formState.password,
                            address = "test test test"
                        )

                        userDao.insertUser(newUser)

                        shouldNavigateToLogin = true
                    }
                } catch (e: Exception) {
                    finalErrorMessage = "Erro ao salvar no banco de dados."
                }

                withContext(Dispatchers.Main) {
                    setIsLoading(false)
                    if (shouldNavigateToLogin) {
                        onSuccess()
                    } else {
                        setErrorMessage(finalErrorMessage)
                    }
                }
            }
        }
    }
}