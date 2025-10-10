package br.edu.ifal.fiscalizaapp.composables.card
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen

@Composable
fun CategoryCard(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CardBase (
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                tint = PrimaryGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    FiscalizaTheme {
        CategoryCard(
            icon = painterResource(id = R.drawable.ic_buraco_na_via),
            text = "Buraco na Via",
            onClick = {}
        )
    }
}