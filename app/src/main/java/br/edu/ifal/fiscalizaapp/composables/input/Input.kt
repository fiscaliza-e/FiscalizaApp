package br.edu.ifal.fiscalizaapp.composables.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

sealed class InputVariant {
    data object Primary : InputVariant()
    data object Secondary : InputVariant()
    data object Danger : InputVariant()
    data class Link(val color: Color = PrimaryGreen) : InputVariant()
}

data class InputStyle(
    val variant: InputVariant = InputVariant.Primary,
    val widthFraction: Float = 1f,
    val heightDp: Int = 52
)

sealed class InputType(
    val keyboardType: KeyboardType,
    val defaultIcon: ImageVector? = null
) {
    data object Text : InputType(KeyboardType.Text)
    data object Email : InputType(KeyboardType.Email)
    data object Password : InputType(KeyboardType.Password)
    data object Number : InputType(KeyboardType.Number)
}

private data class InputColors(
    val border: Color,
    val text: Color,
    val background: Color,
    val placeholder: Color
)

@Composable
private fun InputVariant.getColors(enabled: Boolean): InputColors {
    if (!enabled) {
        return InputColors(
            border = Color.LightGray,
            text = Color.DarkGray,
            background = Color(0xFFF5F5F5),
            placeholder = Color.LightGray
        )
    }

    return when (this) {
        InputVariant.Primary -> InputColors(Color.Gray, Color.Black, Color.White, Color.Gray)
        InputVariant.Secondary -> InputColors(Color.Gray, Color.Black, Color.White, Color.Gray)
        InputVariant.Danger -> InputColors(Color.Red, Color.Red, Color.White, Color.Gray)
        is InputVariant.Link -> InputColors(color, color, Color.Transparent, Color.Gray)
    }
}

fun cpfMask(input: String): String {
    val digits = input.filter { it.isDigit() }.take(11)
    return when {
        digits.length <= 3 -> digits
        digits.length <= 6 -> "${digits.take(3)}.${digits.drop(3)}"
        digits.length <= 9 -> "${digits.take(3)}.${digits.drop(3).take(3)}.${digits.drop(6)}"
        else -> "${digits.take(3)}.${digits.drop(3).take(3)}.${digits.drop(6).take(3)}-${digits.drop(9)}"
    }
}

fun phoneMask(input: String): String {
    val digits = input.filter { it.isDigit() }.take(11)
    return when {
        digits.length <= 2 -> "(${digits}"
        digits.length <= 7 -> "(${digits.take(2)}) ${digits.drop(2)}"
        digits.length <= 11 -> "(${digits.take(2)}) ${digits.drop(2).take(5)}-${digits.drop(7)}"
        else -> digits
    }
}

fun cepMask(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)
    return when {
        digits.length <= 5 -> digits
        else -> "${digits.take(5)}-${digits.drop(5)}"
    }
}

fun dateMask(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)
    return when {
        digits.length <= 2 -> digits
        digits.length <= 4 -> "${digits.take(2)}/${digits.drop(2)}"
        else -> "${digits.take(2)}/${digits.drop(2).take(2)}/${digits.drop(4)}"
    }
}

class MaskVisualTransformation(
    private val maskFunction: (String) -> String,
    private val maxDigits: Int
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.filter { it.isDigit() }.take(maxDigits)
        val masked = maskFunction(originalText)

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val cleanOffset = offset.coerceIn(0, originalText.length)
                return maskFunction(originalText.take(cleanOffset)).length
            }

            override fun transformedToOriginal(offset: Int): Int {
                val maskedOffset = offset.coerceIn(0, masked.length)
                return masked.take(maskedOffset).filter { it.isDigit() }.length
            }
        }

        return TransformedText(AnnotatedString(masked), offsetTranslator)
    }
}

@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    type: InputType = InputType.Text,
    enabled: Boolean = true,
    leftIcon: ImageVector? = null,
    mask: ((String) -> String)? = null,
    style: InputStyle = InputStyle()
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val colors = style.variant.getColors(enabled)
    val baseBorderColor = colors.border
    val textColor = colors.text
    val backgroundColor = colors.background
    val placeholderColor = colors.placeholder

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val currentBorderColor = if (isFocused && style.variant !is InputVariant.Link) {
        PrimaryGreen
    } else {
        baseBorderColor
    }

    val baseVisualTransformation =
        if (type is InputType.Password && !passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None

    val maskVisualTransformation = remember(mask) {
        if (mask == null || type.keyboardType != KeyboardType.Number && type.keyboardType != KeyboardType.Phone) {
            baseVisualTransformation
        } else {
            val maxDigits = when (mask) {
                ::cpfMask -> 11
                ::phoneMask -> 11
                ::cepMask -> 8
                ::dateMask -> 8
                else -> 50
            }
            MaskVisualTransformation(mask, maxDigits)
        }
    }

    val keyboardOptions = KeyboardOptions(keyboardType = type.keyboardType)

    val textFieldValue = remember(value, maskVisualTransformation) {
        if (maskVisualTransformation is MaskVisualTransformation) {
            value.filter { it.isDigit() }
        } else {
            value
        }
    }

    val finalVisualTransformation = when {
        type is InputType.Password -> baseVisualTransformation
        mask != null -> maskVisualTransformation
        else -> VisualTransformation.None
    }


    Column(
        modifier = modifier.fillMaxWidth(style.widthFraction)
    ) {
        if (!label.isNullOrEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(style.heightDp.dp),
            shape = RoundedCornerShape(8.dp),
            color = backgroundColor,
            border = BorderStroke(
                width = if (style.variant is InputVariant.Link) 1.dp else 2.dp,
                color = currentBorderColor
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, top = 5.dp, end = 16.dp, bottom = 5.dp)
            ) {
                val startIcon = leftIcon ?: type.defaultIcon
                if (startIcon != null) {
                    Icon(
                        imageVector = startIcon,
                        contentDescription = null,
                        tint = currentBorderColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }

                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { input ->
                        val filteredValue = if (maskVisualTransformation is MaskVisualTransformation) {
                            input.filter { it.isDigit() }
                        } else {
                            input
                        }

                        val maxLength = when (mask) {
                            ::cpfMask -> 11
                            ::phoneMask -> 11
                            ::cepMask -> 8
                            ::dateMask -> 8
                            else -> filteredValue.length
                        }

                        val trimmedValue = filteredValue.take(maxLength)

                        onValueChange(trimmedValue)
                    },
                    textStyle = TextStyle(color = textColor),
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = finalVisualTransformation,
                    keyboardOptions = keyboardOptions,
                    interactionSource = interactionSource,
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = placeholderColor,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                if (type is InputType.Password) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Alternar visibilidade da senha",
                        tint = if (enabled) currentBorderColor else Color.LightGray,
                        modifier = Modifier
                            .size(22.dp)
                            .then(
                                if (enabled) Modifier.clickable { passwordVisible = !passwordVisible }
                                else Modifier
                            )
                    )
                }
            }
        }
    }
}

