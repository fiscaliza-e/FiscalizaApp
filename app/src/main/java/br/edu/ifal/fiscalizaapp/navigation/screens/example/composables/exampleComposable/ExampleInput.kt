package br.edu.ifal.fiscalizaapp.screens.example.ExampleScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import br.edu.ifal.fiscalizaapp.composables.input.Input
import br.edu.ifal.fiscalizaapp.composables.input.InputStyle
import br.edu.ifal.fiscalizaapp.composables.input.InputType
import br.edu.ifal.fiscalizaapp.composables.input.InputVariant
import br.edu.ifal.fiscalizaapp.composables.input.cepMask
import br.edu.ifal.fiscalizaapp.composables.input.cpfMask
import br.edu.ifal.fiscalizaapp.composables.input.dateMask
import br.edu.ifal.fiscalizaapp.composables.input.phoneMask


@Composable
fun InputExampleScreen() {
    var cpf by remember { mutableStateOf("12345678900") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        Input(
            label = "CPF (Disabled - Primary)",
            value = cpf,
            onValueChange = { cpf = it },
            placeholder = "000.000.000-00",
            type = InputType.Number,
            enabled = false,
            mask = ::cpfMask,
            style = InputStyle(widthFraction = 1f, variant = InputVariant.Primary)
        )

        Input(
            label = "Nome Completo (Danger)",
            value = name,
            onValueChange = { name = it },
            placeholder = "Digite seu nome",
            type = InputType.Text,
            style = InputStyle(widthFraction = 1f, variant = InputVariant.Danger)
        )

        Input(
            label = "Email (Primary)",
            value = email,
            onValueChange = { email = it },
            placeholder = "seu.email@exemplo.com",
            type = InputType.Email,
            style = InputStyle(widthFraction = 1f, variant = InputVariant.Primary)
        )

        Input(
            label = "Senha (Secondary)",
            value = password,
            onValueChange = { password = it },
            placeholder = "Digite sua senha",
            type = InputType.Password,
            style = InputStyle(widthFraction = 1f, variant = InputVariant.Secondary)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Inputs Lado a Lado (Row)")

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Input(
                label = "CEP (60%)",
                value = cep,
                onValueChange = { cep = it },
                placeholder = "00000-000",
                type = InputType.Number,
                mask = ::cepMask,
                modifier = Modifier.weight(0.6f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Input(
                label = "Nº (30%)",
                value = number,
                onValueChange = { number = it },
                placeholder = "Nº / S/N",
                type = InputType.Text,
                style = InputStyle(variant = InputVariant.Secondary),
                modifier = Modifier.weight(0.3f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Input(
                label = "Data de Nascimento",
                value = date,
                onValueChange = { date = it },
                placeholder = "dd/mm/aaaa",
                type = InputType.Number,
                mask = ::dateMask,
                modifier = Modifier.weight(1f)
            )

            Input(
                label = "Telefone",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "(00) 00000-0000",
                type = InputType.Number,
                mask = ::phoneMask,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputExampleScreen() {
    InputExampleScreen()
}