package br.edu.ifal.fiscalizaapp.navigation.screens.example.composables.exampleComposable

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTag
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTagStyle
import br.edu.ifal.fiscalizaapp.composables.statustag.StatusTagVariant

@Preview(showBackground = true)
@Composable
fun PreviewStatusTags() {
    Row {
        StatusTag(
            status = "PENDENTE",
            style = StatusTagStyle(StatusTagVariant.Pending)
        )
        StatusTag(
            status = "VISUALIZADA",
            style = StatusTagStyle(StatusTagVariant.Viewed)
        )
        StatusTag(
            status = "RESOLVIDO",
            style = StatusTagStyle(StatusTagVariant.Resolved)
        )
    }
}