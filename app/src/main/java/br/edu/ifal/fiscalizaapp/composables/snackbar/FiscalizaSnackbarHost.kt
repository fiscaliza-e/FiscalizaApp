package br.edu.ifal.fiscalizaapp.composables.snackbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

private const val SUCCESS_PREFIX = "SUCCESS:"
private const val ERROR_PREFIX = "ERROR:"

@Composable
fun FiscalizaSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState) { data ->
        val raw = data.visuals.message
        val isSuccess = raw.startsWith(SUCCESS_PREFIX)
        val message = raw.removePrefix(SUCCESS_PREFIX).removePrefix(ERROR_PREFIX)
        val bgColor = if (isSuccess) PrimaryGreen else MaterialTheme.colorScheme.error
        val icon = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Cancel

        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = bgColor,
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
