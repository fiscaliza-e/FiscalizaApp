package br.edu.ifal.fiscalizaapp.composables.protocollist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.edu.ifal.fiscalizaapp.composables.protocolcard.ProtocolCard
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.searchfilter.SearchFilter
import br.edu.ifal.fiscalizaapp.model.Protocol

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolList(protocols: List<Protocol>, modifier: Modifier = Modifier) {

    if (protocols.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.curiosity_search_cuate),
                    contentDescription = "Nenhum Protocolo Encontrado",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Nenhum protocolo encontrado.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Quando você registrar uma reclamação, ela aparecerá aqui para acompanhamento.",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    text = "Fazer Primeira Reclamação",
                    onClick = { println("Oi") },
                    variant = ButtonVariant.Primary
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SearchFilter(title = "Meus Protocolos")
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(protocols) { protocol ->
                ProtocolCard(
                    title = protocol.title,
                    description = protocol.description,
                    status = protocol.status,
                    id = protocol.protocolNumber,
                    date = protocol.date,
                    modifier = Modifier
                )
            }
        }
    }
}