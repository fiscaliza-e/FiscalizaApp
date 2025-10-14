package br.edu.ifal.fiscalizaapp.screens.example.composables.exampleComposable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ifal.fiscalizaapp.composables.imagepicker.ImagePicker

@Composable
fun ImagePickerPlayground() {
    var uris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val pickImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { result ->
        if (result != null) uris = uris + result
    }

    Column(Modifier.padding(16.dp)) {
        ImagePicker(
            onClick = {
                pickImages.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            selectedImages = uris,
            onRemove = { toRemove -> uris = uris.filterNot { it == toRemove } }
        )
        Spacer(Modifier.height(12.dp))
        Text("Selecionadas: ${uris.size}")
    }
}