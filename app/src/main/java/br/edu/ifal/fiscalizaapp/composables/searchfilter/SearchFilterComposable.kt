package br.edu.ifal.fiscalizaapp.composables.searchfilter


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen


@Composable
fun SearchFilter(
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sort_rounded),
                    contentDescription = "Order By",
                    modifier = Modifier.size(28.dp),
                    tint = PrimaryGreen
                )
                Icon(
                    painter = painterResource(id = R.drawable.mdi_filter),
                    contentDescription = "Filter",
                    modifier = Modifier.size(28.dp),
                    tint = PrimaryGreen
                )
            }
        }

    }}