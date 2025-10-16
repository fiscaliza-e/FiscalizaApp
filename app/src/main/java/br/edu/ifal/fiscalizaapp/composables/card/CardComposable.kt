package br.edu.ifal.fiscalizaapp.composables.card

import androidx.annotation.OptIn
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBase(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(containerColor = Color.White),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    if (onClick != null) {
        // Card clicável somente quando onClick não é nulo
        Card(
            modifier = modifier,
            onClick = onClick,
            colors = colors,
            elevation = elevation,
            shape = shape
        ) {
            Box(
                // Sem padding aqui; o conteúdo (ex.: Accordion) decide o próprio padding
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    } else {
        // Versão não clicável (sem semântica de clique)
        Card(
            modifier = modifier,
            colors = colors,
            elevation = elevation,
            shape = shape
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBaseNoRipple(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(containerColor = Color.White),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val interactionSource = remember { MutableInteractionSource() }

    if (onClick != null) {
        Card(
            modifier = modifier,
            onClick = onClick,
            colors = colors,
            elevation = elevation,
            shape = shape,
            interactionSource = interactionSource
        ) {
            Box(contentAlignment = Alignment.Center) {
                content()
            }
        }
    } else {
        Card(
            modifier = modifier,
            colors = colors,
            elevation = elevation,
            shape = shape
        ) {
            Box(contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}