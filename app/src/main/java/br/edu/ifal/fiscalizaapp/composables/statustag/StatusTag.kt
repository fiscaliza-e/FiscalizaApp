package br.edu.ifal.fiscalizaapp.composables.statustag

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.ui.theme.*

sealed class StatusTagVariant {
    data object Viewed : StatusTagVariant()
    data object Pending : StatusTagVariant()
    data object Resolved : StatusTagVariant()
}

data class StatusTagStyle(
    val variant: StatusTagVariant = StatusTagVariant.Viewed,
)

private data class StatusTagColors(
    val text: Color,
    val background: Color,
)

@Composable
private fun StatusTagVariant.getColors(): StatusTagColors {
    return when (this) {
        StatusTagVariant.Pending -> StatusTagColors(Pending, PendingBackGround)
        StatusTagVariant.Resolved -> StatusTagColors(Resolved, ResolvedBackGround)
        StatusTagVariant.Viewed -> StatusTagColors(Viewed, ViewedBackGround)
    }
}

@Composable
fun StatusTag(
    status: String,
    style: StatusTagStyle = StatusTagStyle(StatusTagVariant.Viewed)
) {
    val colors = style.variant.getColors()

    Surface(
        color = colors.background,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(end = 8.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
            Text(
                text = status,
                color = colors.text,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

