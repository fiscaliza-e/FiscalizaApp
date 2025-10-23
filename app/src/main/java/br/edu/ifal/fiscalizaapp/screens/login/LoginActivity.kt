package br.edu.ifal.fiscalizaapp.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.input.Input
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.input.InputType
import br.edu.ifal.fiscalizaapp.composables.input.InputStyle
import br.edu.ifal.fiscalizaapp.composables.input.InputVariant
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(containerColor = Color.White) { innerPadding ->

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Entre na sua conta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
            Spacer(modifier = Modifier.height(64.dp))
            Column () {
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
                    color = PrimaryGreen
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                text = "Entrar",
                onClick = { /* TODO: Implementar navegação */ },
                variant = ButtonVariant.Primary,
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                Text(
                    text = "Não possui uma conta?",
                    fontSize = 14.sp,
                )
                Text(
                    text = "Cadastre-se",
                    fontSize = 14.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
