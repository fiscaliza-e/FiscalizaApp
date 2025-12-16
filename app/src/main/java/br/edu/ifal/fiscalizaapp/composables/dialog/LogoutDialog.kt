package br.edu.ifal.fiscalizaapp.composables.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = Color.White,
        title = {
            Text(text = "Sair da conta", fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "Tem certeza que deseja sair do aplicativo?")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sair", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}