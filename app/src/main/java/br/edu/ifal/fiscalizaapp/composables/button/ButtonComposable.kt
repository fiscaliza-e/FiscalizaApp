package br.edu.ifal.fiscalizaapp.composables.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ButtonVariant {
    data object Primary : ButtonVariant()
    data object Secondary : ButtonVariant()
    data object Danger : ButtonVariant()
    data object Disabled : ButtonVariant()
    data class Link(val color : Color = PrimaryGreen) : ButtonVariant()
}

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
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
        is ButtonVariant.Link -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = variant.color
        )
    }

    val border = when (variant) {
        ButtonVariant.Primary, ButtonVariant.Disabled, is ButtonVariant.Link -> null
        ButtonVariant.Secondary -> BorderStroke(
            width = 1.dp,
            color = primaryColor
        )
        ButtonVariant.Danger -> BorderStroke(
            width = 1.dp,
            color = Color.Red
        )
    }

    val contentPadding = when (variant) {
        is ButtonVariant.Link -> PaddingValues(horizontal = 0.dp, vertical = 0.dp)
        else -> PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        shape = buttonShape,
    ) {
        if (leftIcon != null) {
            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }

        Text(text = text.uppercase())

        if (rightIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Icon(
                imageVector = rightIcon,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }
    }
}