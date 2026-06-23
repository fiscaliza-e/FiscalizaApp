package br.edu.ifal.fiscalizaapp.composables.dropdown

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
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
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrow_rotation"
    )
    var triggerWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val borderColor = if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val borderWidth = if (expanded) 2.dp else 1.dp

    Box(modifier = modifier.onSizeChanged { triggerWidth = it.width }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
                .clickable { onExpandedChange(!expanded) }
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = selectedOption ?: placeholderText,
                color = if (selectedOption != null)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .rotate(arrowRotation)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.width(with(density) { triggerWidth.toDp() })
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = option == selectedOption
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailingIcon = if (isSelected) ({
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }) else null,
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    }
                )
                if (index < options.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
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
