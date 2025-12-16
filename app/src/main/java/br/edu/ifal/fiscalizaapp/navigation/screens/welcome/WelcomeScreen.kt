package br.edu.ifal.fiscalizaapp.navigation.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.navigation.routes.loginRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.registerRoute
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

private val ILLUSTRATION_RES_ID = R.drawable.ic_welcome

@Composable
fun WelcomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_app),
                    contentDescription = "Brasão da Prefeitura",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fiscaliza-e",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryGreen
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Bem-vindo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "O canal direto para resolver problemas da sua cidade.",
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                Image(
                    painter = painterResource(id = ILLUSTRATION_RES_ID),
                    contentDescription = "Ilustração de boas-vindas",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = "Entrar",
                    onClick = { navController.navigate(loginRoute) },
                    variant = ButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    text = "Cadastrar",
                    onClick = { navController.navigate(registerRoute) },
                    variant = ButtonVariant.Primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}