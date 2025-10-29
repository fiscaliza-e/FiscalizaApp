package br.edu.ifal.fiscalizaapp.composables.protocolcard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifal.fiscalizaapp.composables.card.CardBase
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTag
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTagStyle
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTagVariant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ProtocolCardStyle(
    val heightDp: Int = 110
)

fun mapStatusToVariant(statusText: String): StatusTagVariant {
    return when (statusText.uppercase()) {
        "PENDENTE" -> StatusTagVariant.Pending
        "PENDING" -> StatusTagVariant.Pending
        "VISUALIZADO" -> StatusTagVariant.Viewed
        "VIEWED" -> StatusTagVariant.Viewed
        "RESOLVIDO" -> StatusTagVariant.Resolved
        "RESOLVED" -> StatusTagVariant.Resolved
        else -> StatusTagVariant.Pending
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolCard(
    title: String,
    description: String,
    status: String,
    id: String,
    date: String,
    modifier: Modifier,
    style: ProtocolCardStyle = ProtocolCardStyle()
) {
    val displayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val inputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")

    val formattedDate: String = LocalDate.parse(date, inputFormatter).format(displayFormatter)

    val statusVariant = mapStatusToVariant(status)

    CardBase(modifier) {
        Column(
            modifier = Modifier
                .padding(20.dp, 16.dp)
                .fillMaxWidth().defaultMinSize(minHeight = style.heightDp.dp),
                    verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp), maxLines = 1 )

                Text(formattedDate, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                description,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(16.dp))

            StatusTag(status, style = StatusTagStyle(statusVariant))
        }
    }
}

