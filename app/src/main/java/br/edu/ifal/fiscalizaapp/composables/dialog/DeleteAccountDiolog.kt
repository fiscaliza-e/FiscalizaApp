package br.edu.ifal.fiscalizaapp.composables.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = Color.White,
        title = {
            Text(text = "Excluir Conta", fontWeight = FontWeight.Bold, color = Color.Red)
        },
        text = {
            Text(text = "Tem certeza que deseja excluir sua conta permanentemente? Todos os seus dados e protocolos serão perdidos. Essa ação não pode ser desfeita.")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sim, excluir", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}