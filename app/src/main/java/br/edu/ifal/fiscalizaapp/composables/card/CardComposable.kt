package br.edu.ifal.fiscalizaapp.composables.card
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBase(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardShape = RoundedCornerShape(12.dp)
    val cardColors = CardDefaults.cardColors(containerColor = Color.White)
    val cardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    val paddingModifier = Modifier.padding(16.dp)
    Card(
        modifier = modifier,
        onClick = onClick ?: {},
        shape = cardShape,
        colors = cardColors,
        elevation = cardElevation
    ) {
        Box(
            modifier = paddingModifier,
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

