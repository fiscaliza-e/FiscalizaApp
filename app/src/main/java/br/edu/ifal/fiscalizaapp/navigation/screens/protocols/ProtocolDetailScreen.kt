package br.edu.ifal.fiscalizaapp.navigation.screens.protocols

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTag
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTagStyle
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTagVariant
import br.edu.ifal.fiscalizaapp.composables.protocolcard.mapStatusToVariant
import br.edu.ifal.fiscalizaapp.data.api.nominatim.NominatimRetrofitHelper
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

@Composable
fun ProtocolDetailScreen(
    protocol: ProtocolEntity,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val statusVariant = mapStatusToVariant(protocol.status)

    val photos = remember(protocol.photoUris) {
        protocol.photoUris.split(",").filter { it.isNotBlank() }
    }

    val fullAddress = remember(protocol) {
        if (protocol.useMyLocation) ""
        else listOfNotNull(
            protocol.rua.takeIf { it.isNotBlank() },
            protocol.numero.takeIf { it.isNotBlank() },
            protocol.bairro.takeIf { it.isNotBlank() },
            protocol.cep.takeIf { it.isNotBlank() },
            "Brasil"
        ).joinToString(", ")
    }

    var mapCoords by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var isGeocodingLoading by remember { mutableStateOf(false) }

    LaunchedEffect(fullAddress) {
        if (fullAddress.isNotBlank()) {
            isGeocodingLoading = true
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    NominatimRetrofitHelper.getAPI().search(fullAddress).firstOrNull()
                }.getOrNull()
            }
            result?.let {
                mapCoords = Pair(it.lat.toDouble(), it.lon.toDouble())
            }
            isGeocodingLoading = false
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                type = AppHeaderType.INTERN_SCREEN,
                title = "Detalhes do Protocolo",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = protocol.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusTag(protocol.status, style = StatusTagStyle(statusVariant))
                Text(
                    text = "Registrado em: ${protocol.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (photos.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((((photos.size + 1) / 2) * 160).dp.coerceAtMost(340.dp))
                ) {
                    items(photos) { path ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            AsyncImage(
                                model = File(path),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            val isTransporte = "transporte" in protocol.title.lowercase()

            DetailSection(title = "Sua solicitação") {
                DetailField(label = "Descrição", value = protocol.description)

                // Campos específicos por categoria
                if (protocol.numeroPoste.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    DetailField(label = "Número do poste", value = protocol.numeroPoste)
                }
                if (protocol.nomeOrgao.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    DetailField(label = "Órgão público", value = protocol.nomeOrgao)
                }
                if (protocol.areaSaneamento.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    DetailField(label = "Área do saneamento", value = protocol.areaSaneamento)
                }
                if (protocol.numeroTransporte.isNotBlank() || protocol.linhaTransporte.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    DetailField(label = "Número do veículo", value = protocol.numeroTransporte)
                    Spacer(Modifier.height(8.dp))
                    DetailField(label = "Linha", value = protocol.linhaTransporte)
                    if (protocol.horarioTransporte.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        DetailField(label = "Horário", value = protocol.horarioTransporte)
                    }
                }

                if (!isTransporte && !protocol.useMyLocation && fullAddress.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Endereço",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Column {
                            if (protocol.rua.isNotBlank()) {
                                Text("${protocol.rua}, ${protocol.numero}", color = MaterialTheme.colorScheme.onSurface)
                            }
                            if (protocol.bairro.isNotBlank()) {
                                Text(protocol.bairro, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                            }
                            if (protocol.pontoReferencia.isNotBlank()) {
                                Text(
                                    "Ponto de Referência: ${protocol.pontoReferencia}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            isGeocodingLoading -> CircularProgressIndicator(modifier = Modifier.size(32.dp))
                            mapCoords != null -> {
                                val (lat, lon) = mapCoords!!
                                OsmMapView(lat = lat, lon = lon)
                            }
                            else -> {
                                Text(
                                    text = "Mapa indisponível",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                } else if (!isTransporte && protocol.useMyLocation) {
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Localização atual do usuário",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (protocol.latitude != 0.0 || protocol.longitude != 0.0) {
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            OsmMapView(lat = protocol.latitude, lon = protocol.longitude)
                        }
                    }
                }
            }

            DetailSection(title = "Acompanhamento da Prefeitura") {
                StatusTimelineItem(
                    title = statusTitleFor(protocol.status),
                    date = protocol.date,
                    description = statusDescriptionFor(protocol.status),
                    isFirst = true,
                    isResolved = statusVariant == StatusTagVariant.Resolved
                )
                Spacer(Modifier.height(4.dp))
                StatusTimelineItem(
                    title = "Reclamação Registrada",
                    date = protocol.date,
                    description = "Seu protocolo foi criado com sucesso.",
                    isFirst = false,
                    isResolved = false
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun OsmMapView(lat: Double, lon: Double) {
    val context = LocalContext.current
    val mapView = remember {
        Configuration.getInstance().apply {
            load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
            userAgentValue = context.packageName
        }
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(false)
            isFocusable = false
            isClickable = false
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(lat, lon))
            val marker = Marker(this)
            marker.position = GeoPoint(lat, lon)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            overlays.add(marker)
        }
    }

    DisposableEffect(Unit) {
        mapView.onResume()
        onDispose { mapView.onPause() }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(22.dp)
                    .background(PrimaryGreen, RoundedCornerShape(2.dp))
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StatusTimelineItem(
    title: String,
    date: String,
    description: String,
    isFirst: Boolean,
    isResolved: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (isResolved && isFirst) PrimaryGreen else MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            )
            if (!isFirst) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun statusTitleFor(status: String): String = when (status.uppercase()) {
    "RESOLVIDO", "RESOLVED" -> "Status alterado para Resolvido"
    "VISUALIZADO", "VIEWED" -> "Solicitação encaminhada"
    else -> "Reclamação Pendente"
}

private fun statusDescriptionFor(status: String): String = when (status.uppercase()) {
    "RESOLVIDO", "RESOLVED" -> "O problema foi resolvido pela equipe de manutenção."
    "VISUALIZADO", "VIEWED" -> "Sua solicitação foi recebida e encaminhada ao setor responsável."
    else -> "Sua reclamação está aguardando análise."
}
