package br.edu.ifal.fiscalizaapp.composables.searchfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ProtocolFilter
import br.edu.ifal.fiscalizaapp.ui.viewmodels.SortOrder

@Composable
fun SearchFilter(
    modifier: Modifier = Modifier,
    title: String = "",
    filterStatus: ProtocolFilter = ProtocolFilter.ALL,
    sortOrder: SortOrder = SortOrder.NEWEST_FIRST,
    onFilterChange: (ProtocolFilter) -> Unit = {},
    onSortChange: (SortOrder) -> Unit = {}
) {
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box {
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Ordenar",
                        modifier = Modifier.size(24.dp),
                        tint = PrimaryGreen
                    )
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Mais recente") },
                        onClick = {
                            onSortChange(SortOrder.NEWEST_FIRST)
                            showSortMenu = false
                        },
                        trailingIcon = if (sortOrder == SortOrder.NEWEST_FIRST) ({
                            Text("✓", color = PrimaryGreen)
                        }) else null
                    )
                    DropdownMenuItem(
                        text = { Text("Mais antigo") },
                        onClick = {
                            onSortChange(SortOrder.OLDEST_FIRST)
                            showSortMenu = false
                        },
                        trailingIcon = if (sortOrder == SortOrder.OLDEST_FIRST) ({
                            Text("✓", color = PrimaryGreen)
                        }) else null
                    )
                }
            }

            Box {
                IconButton(onClick = { showFilterMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filtrar",
                        modifier = Modifier.size(24.dp),
                        tint = if (filterStatus != ProtocolFilter.ALL) PrimaryGreen
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false }
                ) {
                    val options = listOf(
                        ProtocolFilter.ALL to "Todos",
                        ProtocolFilter.PENDING to "Pendente",
                        ProtocolFilter.VIEWED to "Visualizada",
                        ProtocolFilter.PROCESSED to "Processada",
                        ProtocolFilter.ARCHIVED to "Arquivada",
                    )
                    options.forEach { (filter, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                onFilterChange(filter)
                                showFilterMenu = false
                            },
                            trailingIcon = if (filterStatus == filter) ({
                                Text("✓", color = PrimaryGreen)
                            }) else null
                        )
                    }
                }
            }
        }
    }
}
