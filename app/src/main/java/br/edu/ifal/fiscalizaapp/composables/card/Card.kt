package br.edu.ifal.fiscalizaapp.composables.card

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.ui.unit.dp

@Composable
fun CardBase(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    showRipple: Boolean = true,
    colors: CardColors = CardDefaults.cardColors(containerColor = Color.White),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val interactionSource = remember { MutableInteractionSource() }

    val clickModifier = onClick?.let { click ->
        modifier.clickable(
            enabled = true,
            interactionSource = interactionSource,
            indication = if (showRipple) LocalIndication.current else null,
            onClick = click
        )
    } ?: modifier

    Card(
        modifier = clickModifier,
        colors = colors,
        elevation = elevation,
        shape = shape
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}