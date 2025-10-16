package br.edu.ifal.fiscalizaapp.composables.dropdown

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme

@Composable
fun Dropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "Selecione a opção"
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                .clickable { onExpandedChange(true) }
                .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedOption ?: placeholderText,
                color = if (selectedOption != null) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Abrir opções",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Estado Padrão", group = "Dropdown States")
@Composable
fun DropdownPreview_Placeholder() {
    FiscalizaTheme {
        var selectedCategory by remember { mutableStateOf<String?>(null) }
        var expanded by remember { mutableStateOf(false) }
        val categories = listOf("Pavimentação", "Transporte Coletivo", "Iluminação Pública")

        Dropdown(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            options = categories,
            selectedOption = selectedCategory,
            onOptionSelected = { selectedCategory = it }
        )
    }
}

@Preview(showBackground = true, name = "Estado Selecionado", group = "Dropdown States")
@Composable
fun DropdownPreview_Selected() {
    FiscalizaTheme {
        var selectedCategory by remember { mutableStateOf("Transporte Coletivo") }
        var expanded by remember { mutableStateOf(false) }
        val categories = listOf("Pavimentação", "Transporte Coletivo", "Iluminação Pública")

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Categoria",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Dropdown(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                options = categories,
                selectedOption = selectedCategory,
                onOptionSelected = { selectedCategory = it }
            )
        }
    }
}

@Preview(showBackground = true, name = "Estado Aberto", group = "Dropdown States")
@Composable
fun DropdownPreview_Expanded() {
    FiscalizaTheme {
        var selectedCategory by remember { mutableStateOf("Pavimentação") }
        var expanded by remember { mutableStateOf(true) }
        val categories = listOf("Pavimentação", "Transporte Coletivo", "Iluminação Pública")

        Column(modifier = Modifier.padding(16.dp).height(250.dp)) {
            Text(
                text = "Categoria",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
           Dropdown(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                options = categories,
                selectedOption = selectedCategory,
                onOptionSelected = { selectedCategory = it }
            )
        }
    }
}