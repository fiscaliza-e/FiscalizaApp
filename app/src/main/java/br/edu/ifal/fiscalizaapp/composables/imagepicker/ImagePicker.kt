package br.edu.ifal.fiscalizaapp.composables.imagepicker

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.fillMaxSize

data class ImagePickerStrings(
    val title: String = "Toque para adicionar fotos",
    val contentDescription: String = "Abrir câmera ou galeria",
)

private fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp,
    dashLength: Dp,
    gapLength: Dp,
    shape: Shape
): Modifier = this.then(
    Modifier.drawBehind {
        val outline = shape.createOutline(size, layoutDirection, this)
        val path = Path().apply {
            when (outline) {
                is androidx.compose.ui.graphics.Outline.Rectangle -> addRect(outline.rect)
                is androidx.compose.ui.graphics.Outline.Rounded -> addRoundRect(outline.roundRect)
                is androidx.compose.ui.graphics.Outline.Generic -> addPath(outline.path)
            }
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(dashLength.toPx(), gapLength.toPx()),
                    0f
                )
            )
        )
    }
)

@Composable
fun ImagePicker(
    onClick: () -> Unit,
    selectedImages: List<Uri> = emptyList(),
    onRemove: ((Uri) -> Unit)? = null,
    modifier: Modifier = Modifier,
    strings: ImagePickerStrings = ImagePickerStrings()
) {
    val cornerRadius = 12.dp
    val shape = RoundedCornerShape(cornerRadius)
    val dashedColor = Color(0xFFBDBDBD)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFFF9F9F9), shape)
            .dashedBorder(
                color = dashedColor,
                strokeWidth = 1.dp,
                dashLength = 8.dp,
                gapLength = 6.dp,
                shape = shape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = strings.contentDescription,
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = strings.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9E9E9E)
            )
        }
    }
    if (selectedImages.isNotEmpty()) {
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 360.dp)
        ) {
            items(selectedImages, key = { it.toString() }) { uri ->
                ThumbnailCard(
                    uri = uri,
                    onRemove = onRemove
                )
            }
        }
    }
}

@Composable
private fun ThumbnailCard(
    uri: Uri,
    onRemove: ((Uri) -> Unit)?
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(120.dp)
    ) {
        Box {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (onRemove != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(20.dp)
                        .background(Color(0xAA000000), RoundedCornerShape(50))
                        .clickable { onRemove(uri) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("×", color = Color.White, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}