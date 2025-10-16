package br.edu.ifal.fiscalizaapp.composables.accordion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.composables.card.CardBase
import br.edu.ifal.fiscalizaapp.ui.theme.FiscalizaTheme

@Composable
fun Accordion(
    question: String,
    answer: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    CardBase(
        onClick = onToggle,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF424242),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Recolher" else "Expandir",
                    tint = Color(0xFF666666)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)
                )
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666),
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccordionPreview() {
    FiscalizaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Accordion(
                question = "Há algum custo para utilizar o aplicativo?",
                answer = "Não, o aplicativo é totalmente gratuito para todos os usuários.",
                expanded = false,
                onToggle = { }
            )
            Accordion(
                question = "É possível enviar uma solicitação em nome de outra pessoa?",
                answer = "Para garantir a precisão e segurança, solicitamos que as reclamações sejam feitas em nome do próprio usuário cadastrado. Dessa forma, conseguimos manter a transparência e o histórico de cada solicitação.",
                expanded = true,
                onToggle = { }
            )
        }
    }
}