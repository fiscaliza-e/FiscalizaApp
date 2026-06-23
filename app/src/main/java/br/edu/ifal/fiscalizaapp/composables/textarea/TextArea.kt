package br.edu.ifal.fiscalizaapp.composables.textarea

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

private val HorizontalPadding = 16.dp
private val VerticalPadding = 16.dp
private val FieldShape = RoundedCornerShape(8.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextArea(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLength: Int = 500,
    placeholderText: String = "Escreva em detalhes o que deseja"
) {
    val actualCharacters = value.length
    val isError = actualCharacters > maxLength
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val surface = MaterialTheme.colorScheme.surface
    val counterColor = if (isError) MaterialTheme.colorScheme.error
                       else MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(surface, shape = FieldShape)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newText ->
                    if (newText.length <= maxLength) {
                        onValueChange(newText)
                    }
                },
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth(),
                minLines = 5,
                maxLines = 8,
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        isError = isError,
                        placeholder = {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholderText,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = surface,
                            focusedContainerColor = surface,
                            errorContainerColor = surface
                        ),
                        contentPadding = OutlinedTextFieldDefaults.contentPadding(
                            start = HorizontalPadding,
                            end = HorizontalPadding,
                            top = VerticalPadding,
                            bottom = VerticalPadding
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = isError,
                                interactionSource = interactionSource,
                                shape = FieldShape,
                                focusedBorderThickness = 2.dp,
                                unfocusedBorderThickness = 1.dp
                            )
                        }
                    )
                }
            )

            Text(
                text = "$actualCharacters / $maxLength",
                color = counterColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 4.dp)
            )
        }
    }
}
