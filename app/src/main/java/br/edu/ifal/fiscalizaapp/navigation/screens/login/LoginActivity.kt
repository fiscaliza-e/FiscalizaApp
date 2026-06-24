package br.edu.ifal.fiscalizaapp.navigation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.input.Input
import br.edu.ifal.fiscalizaapp.composables.input.InputStyle
import br.edu.ifal.fiscalizaapp.composables.input.InputType
import br.edu.ifal.fiscalizaapp.composables.input.InputVariant
import br.edu.ifal.fiscalizaapp.navigation.routes.homeRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.registerRoute
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.LoginViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = loginState is LoginViewModel.LoginState.Loading
    val errorMessage = (loginState as? LoginViewModel.LoginState.Error)?.message

    LaunchedEffect(loginState) {
        if (loginState is LoginViewModel.LoginState.Success) {
            navController.navigate(homeRoute)
        }
    }

    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_logo_app),
                contentDescription = "Logo Fiscaliza-e",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Fiscaliza-e",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Entre na sua conta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column {
                Input(
                    value = email,
                    onValueChange = { email = it },
                    label = "E-mail",
                    placeholder = "Digite seu e-mail",
                    type = InputType.Email,
                    style = InputStyle(
                        variant = InputVariant.Primary,
                        heightDp = 52
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Input(
                    value = password,
                    onValueChange = { password = it },
                    label = "Senha",
                    placeholder = "Digite sua senha",
                    type = InputType.Password,
                    style = InputStyle(
                        variant = InputVariant.Primary,
                        heightDp = 52
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Esqueceu sua senha?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                text = if (isLoading) "Entrando..." else "Entrar",
                onClick = { viewModel.login(email, password) },
                variant = ButtonVariant.Primary,
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "Não possui uma conta? ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Cadastre-se",
                    fontSize = 14.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(registerRoute)
                    }
                )
            }
        }
    }
}
