import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun SearchBarPreview() {
    SearchBar(
        value = "Texto de teste",
        onValueChange = {},
        label = "Qual a sua dúvida?"
    )
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun SearchBarEmptyPreview() {
    SearchBar(
        value = "",
        onValueChange = {},
        label = "Qual a sua dúvida?"
    )
}