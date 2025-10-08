package br.edu.ifal.fiscalizaapp.composables.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import androidx.compose.foundation.shape.RoundedCornerShape
sealed class ButtonVariant {
    data object Primary : ButtonVariant()
    data object Secondary : ButtonVariant()

    data object Danger : ButtonVariant()

    data object Disabled : ButtonVariant()
}

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true
) {
    val primaryColor = PrimaryGreen
    val buttonShape = RoundedCornerShape(8.dp)

    val colors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = primaryColor,
            contentColor = Color.White
        )
        ButtonVariant.Secondary -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = primaryColor
        )
        ButtonVariant.Danger -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Red
        )
        ButtonVariant.Disabled -> ButtonDefaults.buttonColors(
            containerColor = Color.Gray,
            contentColor = Color.White
        )
    }

    val border = when (variant) {
        ButtonVariant.Primary -> null
        ButtonVariant.Secondary -> BorderStroke(
            width = 1.dp,
            color = primaryColor
        )
        ButtonVariant.Danger -> BorderStroke(
            width = 1.dp,
            color = Color.Red
        )
        ButtonVariant.Disabled -> null
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        shape = buttonShape
    ) {
        Text(text = text.uppercase())
    }
}