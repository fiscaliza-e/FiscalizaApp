package br.edu.ifal.fiscalizaapp.composables.protocolList

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
import br.edu.ifal.fiscalizaapp.composables.protocolcard.ProtocolCardStyle
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

data class ProtocolMock(
    val id: String,
    val title: String,
    val description: String,
    val status: String,
    val date: String,
    val style: ProtocolCardStyle = ProtocolCardStyle()
)

val mockProtocols = listOf(
    ProtocolMock(
        id = "P-0001",
        title = "Buraco na rua principal",
        description = "Buraco de grande porte próximo à esquina da Av. Central com a Rua das Flores, dificultando o tráfego.",
        status = "PENDENTE",
        date = "10-20-2025"
    ),
    ProtocolMock(
        id = "P-0002",
        title = "Lâmpada queimada em poste público",
        description = "Poste de iluminação em frente à escola municipal está apagado há mais de uma semana.",
        status = "VISUALIZADO",
        date = "10-18-2025"
    ),
    ProtocolMock(
        id = "P-0003",
        title = "Acúmulo de lixo em terreno baldio",
        description = "Moradores estão jogando lixo em um terreno baldio ao lado do mercado local.",
        status = "RESOLVIDO",
        date = "09-25-2025"
    ),
    ProtocolMock(
        id = "P-0004",
        title = "Sinal de trânsito quebrado",
        description = "O semáforo da esquina da Av. Brasil com a Rua das Palmeiras está apagado e sem manutenção.",
        status = "PENDENTE",
        date = "10-10-2025"
    ),
    ProtocolMock(
        id = "P-0005",
        title = "Esgoto a céu aberto",
        description = "Há um vazamento de esgoto em frente ao número 221 da Rua Verde Mar, causando mau cheiro.",
        status = "VISUALIZADO",
        date = "10-12-2025"
    ),
    ProtocolMock(
        id = "P-0006",
        title = "Calçada danificada",
        description = "Calçada da praça central com vários buracos, dificultando o acesso de cadeirantes.",
        status = "RESOLVIDO",
        date = "09-30-2025"
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolList(modifier: Modifier = Modifier) {
    //val protocols = emptyList<ProtocolMock>()
    val protocols = mockProtocols

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
                    id = protocol.id,
                    date = protocol.date,
                    modifier = Modifier,
                    style = protocol.style
                )
            }
        }
    }
}

