package br.edu.ifal.fiscalizaapp.screens.protocols

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.ifal.fiscalizaapp.composables.protocolList.ProtocolList

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProtocolScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(containerColor = Color.White) { innerPadding ->
        ProtocolList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProtocolScreenPreview() {
    val navController = rememberNavController()
    ProtocolScreen(navController = navController)
}