package br.edu.ifal.fiscalizaapp.composables.accordion
import br.edu.ifal.fiscalizaapp.model.AccordionItem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccordionList(
    items: List<AccordionItem>,
    modifier: Modifier = Modifier,
    itemSpacing: Int = 8
) {
    val expanded = remember {
        mutableStateListOf<Boolean>().apply { repeat(items.size) { add(false) } }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemSpacing.dp)
    ) {
        items.forEachIndexed { index, item ->
            Accordion(
                question = item.question,
                answer = item.answer,
                expanded = expanded[index],
                onToggle = { expanded[index] = !expanded[index] },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
