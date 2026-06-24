package br.edu.ifal.fiscalizaapp.navigation.screens.protocols

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.edu.ifal.fiscalizaapp.composables.button.Button
import br.edu.ifal.fiscalizaapp.composables.button.ButtonVariant
import br.edu.ifal.fiscalizaapp.composables.dropdown.Dropdown
import br.edu.ifal.fiscalizaapp.composables.header.AppHeader
import br.edu.ifal.fiscalizaapp.composables.header.AppHeaderType
import br.edu.ifal.fiscalizaapp.composables.imagepicker.ImagePicker
import br.edu.ifal.fiscalizaapp.composables.input.Input
import br.edu.ifal.fiscalizaapp.composables.input.InputType
import br.edu.ifal.fiscalizaapp.composables.input.cepMask
import br.edu.ifal.fiscalizaapp.composables.textarea.TextArea
import br.edu.ifal.fiscalizaapp.ui.theme.PrimaryGreen
import br.edu.ifal.fiscalizaapp.navigation.routes.homeRoute
import br.edu.ifal.fiscalizaapp.navigation.routes.protocolRoute
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CategoryUiModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CategoryUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.CepViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.InsertUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.LocationUiState
import br.edu.ifal.fiscalizaapp.ui.viewmodels.NewProtocolViewModel
import br.edu.ifal.fiscalizaapp.ui.viewmodels.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

private val SANEAMENTO_AREAS = listOf("Água", "Esgoto", "Bueiro / Drenagem", "Coleta de lixo")

private fun categoryKey(category: CategoryUiModel?): String =
    category?.title?.lowercase()?.trim() ?: ""

private fun isIluminacao(category: CategoryUiModel?) = "ilumina" in categoryKey(category)
private fun isSaneamento(category: CategoryUiModel?) = "saneamento" in categoryKey(category)
private fun isOrgaos(category: CategoryUiModel?) = "rgão" in categoryKey(category)
private fun isTransporte(category: CategoryUiModel?) = "transporte" in categoryKey(category)

@Composable
fun NewProtocolScreen(
    navController: NavController,
    categoryIdFromRoute: Int = -1,
    modifier: Modifier = Modifier,
    viewModel: NewProtocolViewModel = viewModel(factory = ViewModelFactory(LocalContext.current)),
    cepViewModel: CepViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedCategory by remember { mutableStateOf<CategoryUiModel?>(null) }
    var description by remember { mutableStateOf("") }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    var useMyLocation by remember { mutableStateOf(false) }

    var cep by remember { mutableStateOf("") }
    var rua by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var pontoReferencia by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Iluminação
    var numeroPoste by remember { mutableStateOf("") }

    // Saneamento
    var areaSaneamento by remember { mutableStateOf("") }
    var isSaneamentoDropdownExpanded by remember { mutableStateOf(false) }

    // Órgãos públicos
    var nomeOrgao by remember { mutableStateOf("") }

    // Transporte coletivo
    var numeroTransporte by remember { mutableStateOf("") }
    var linhaTransporte by remember { mutableStateOf("") }
    var horarioTransporte by remember { mutableStateOf("") }

    // Coordenadas GPS
    var locationLatitude by remember { mutableStateOf(0.0) }
    var locationLongitude by remember { mutableStateOf(0.0) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = (selectedImages + uris).distinct().take(6)
    }

    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchCurrentLocation(locationClient, viewModel)
        } else {
            useMyLocation = false
            viewModel.setLocationError("Permissão de localização negada.")
        }
    }

    val categoryUiState by viewModel.categoryUiState.collectAsState()
    val insertUiState by viewModel.insertUiState.collectAsState()
    val preSelectedCategory by viewModel.preSelectedCategory.collectAsState()
    val locationUiState by viewModel.locationUiState.collectAsState()
    val cepUiState by cepViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategories(preSelectedId = categoryIdFromRoute)
    }

    LaunchedEffect(preSelectedCategory) {
        if (preSelectedCategory != null) {
            selectedCategory = preSelectedCategory
        }
    }

    LaunchedEffect(cep) {
        val cleanCep = cep.filter { it.isDigit() }
        if (cleanCep.length == 8) {
            cepViewModel.fetchAddressByCep(cleanCep)
        } else {
            cepViewModel.resetCepState()
        }
    }

    LaunchedEffect(cepUiState) {
        when (val state = cepUiState) {
            is CepUiState.Success -> {
                rua = state.address.street
                bairro = state.address.neighborhood
            }
            is CepUiState.Idle -> {
                rua = ""
                bairro = ""
            }
            else -> {}
        }
    }

    LaunchedEffect(useMyLocation) {
        if (useMyLocation) {
            val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (hasFine || hasCoarse) {
                fetchCurrentLocation(locationClient, viewModel)
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        } else {
            viewModel.resetLocationState()
        }
    }

    LaunchedEffect(locationUiState) {
        when (val state = locationUiState) {
            is LocationUiState.Success -> {
                locationLatitude = state.latitude
                locationLongitude = state.longitude
                rua = state.address.rua
                bairro = state.address.bairro
                if (state.address.cep.isNotBlank()) cep = state.address.cep
            }
            is LocationUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                useMyLocation = false
            }
            else -> {}
        }
    }

    LaunchedEffect(insertUiState) {
        when (val state = insertUiState) {
            is InsertUiState.Success -> {
                navController.navigate("$protocolRoute?created=true") {
                    popUpTo(homeRoute) { inclusive = false }
                }
            }
            is InsertUiState.Error -> {
                snackbarHostState.showSnackbar("Erro: ${state.message}")
            }
            else -> {}
        }
    }

    val transporte = isTransporte(selectedCategory)

    Scaffold(
        topBar = {
            AppHeader(
                type = AppHeaderType.INTERN_SCREEN,
                title = "Nova Reclamação",
                onBackClick = { navController.popBackStack() }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = data.visuals.message,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        bottomBar = {
            val isFormValid by remember(
                selectedCategory, description, useMyLocation,
                cep, rua, bairro, numero,
                numeroPoste, areaSaneamento, nomeOrgao,
                numeroTransporte, linhaTransporte, horarioTransporte,
                locationUiState
            ) {
                val categoryExtrasValid =
                    (!isIluminacao(selectedCategory) || numeroPoste.isNotBlank()) &&
                    (!isSaneamento(selectedCategory) || areaSaneamento.isNotBlank()) &&
                    (!isOrgaos(selectedCategory) || nomeOrgao.isNotBlank())

                mutableStateOf(
                    selectedCategory != null &&
                    description.length >= 10 &&
                    when {
                        isTransporte(selectedCategory) ->
                            numeroTransporte.isNotBlank() && linhaTransporte.isNotBlank() && horarioTransporte.isNotBlank()
                        useMyLocation ->
                            locationUiState is LocationUiState.Success &&
                            (locationLatitude != 0.0 || locationLongitude != 0.0) &&
                            categoryExtrasValid
                        else ->
                            cep.isNotBlank() && rua.isNotBlank() && bairro.isNotBlank() && numero.isNotBlank() &&
                            categoryExtrasValid
                    }
                )
            }

            Button(
                text = "Enviar Reclamação",
                onClick = {
                    viewModel.saveProtocol(
                        selectedCategory = selectedCategory,
                        description = description,
                        useMyLocation = useMyLocation,
                        cep = cep,
                        rua = rua,
                        bairro = bairro,
                        numero = numero,
                        pontoReferencia = pontoReferencia,
                        selectedPhotoUris = selectedImages,
                        numeroPoste = numeroPoste,
                        areaSaneamento = areaSaneamento,
                        nomeOrgao = nomeOrgao,
                        numeroTransporte = numeroTransporte,
                        linhaTransporte = linhaTransporte,
                        horarioTransporte = horarioTransporte,
                        latitude = locationLatitude,
                        longitude = locationLongitude
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = insertUiState != InsertUiState.Loading && isFormValid,
                variant = if (isFormValid) ButtonVariant.Primary else ButtonVariant.Disabled
            )
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // ── Seção 1: Categoria e descrição ──────────────────────────────
            SectionTitle(text = "1. Sobre o problema")
            Text(
                text = "Categoria",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (val state = categoryUiState) {
                is CategoryUiState.Loading -> {
                    OutlinedTextField(
                        value = "Carregando categorias...",
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is CategoryUiState.Success -> {
                    Dropdown(
                        expanded = isCategoryDropdownExpanded,
                        onExpandedChange = { isCategoryDropdownExpanded = it },
                        options = state.categories.map { it.title },
                        selectedOption = selectedCategory?.title,
                        onOptionSelected = { selectedTitle ->
                            selectedCategory = state.categories.find { it.title == selectedTitle }
                            isCategoryDropdownExpanded = false
                            numeroPoste = ""; areaSaneamento = ""; nomeOrgao = ""
                            numeroTransporte = ""; linhaTransporte = ""; horarioTransporte = ""
                            if (isTransporte(selectedCategory)) {
                                useMyLocation = false
                                viewModel.resetLocationState()
                            }
                        }
                    )
                }
                is CategoryUiState.Error -> {
                    Text(text = "Erro ao carregar categorias: ${state.message}", color = Color.Red)
                }
            }

            // ── Campos específicos por categoria ────────────────────────────

            if (isIluminacao(selectedCategory)) {
                Spacer(modifier = Modifier.height(16.dp))
                Input(
                    label = "Número do poste",
                    value = numeroPoste,
                    onValueChange = { numeroPoste = it },
                    type = InputType.Text,
                    placeholder = "Ex: 12345 (consta na plaquinha do poste)"
                )
            }

            if (isOrgaos(selectedCategory)) {
                Spacer(modifier = Modifier.height(16.dp))
                Input(
                    label = "Nome do órgão",
                    value = nomeOrgao,
                    onValueChange = { nomeOrgao = it },
                    type = InputType.Text,
                    placeholder = "Ex: UBS Centro, Escola Municipal João Paulo II"
                )
            }

            if (isSaneamento(selectedCategory)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Área do saneamento",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Dropdown(
                    expanded = isSaneamentoDropdownExpanded,
                    onExpandedChange = { isSaneamentoDropdownExpanded = it },
                    options = SANEAMENTO_AREAS,
                    selectedOption = areaSaneamento.ifBlank { null },
                    onOptionSelected = {
                        areaSaneamento = it
                        isSaneamentoDropdownExpanded = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextArea(
                label = "Descrição",
                value = description,
                onValueChange = { description = it },
                placeholderText = "Descreva com detalhes o que está acontecendo."
            )

            // ── Seção 2: Fotos ───────────────────────────────────────────────
            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = "2. Adicione fotos (Opcional)")
            Spacer(modifier = Modifier.height(8.dp))
            ImagePicker(
                onClick = { photoPickerLauncher.launch("image/*") },
                selectedImages = selectedImages,
                onRemove = { uri -> selectedImages = selectedImages - uri }
            )

            // ── Seção 3: Localização ou dados do transporte ──────────────────
            Spacer(modifier = Modifier.height(24.dp))

            if (transporte) {
                SectionTitle(text = "3. Dados do transporte")
                Spacer(modifier = Modifier.height(16.dp))
                Input(
                    label = "Número do veículo",
                    value = numeroTransporte,
                    onValueChange = { numeroTransporte = it },
                    type = InputType.Text,
                    placeholder = "Ex: 2045"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Input(
                    label = "Linha",
                    value = linhaTransporte,
                    onValueChange = { linhaTransporte = it },
                    type = InputType.Text,
                    placeholder = "Ex: 042 – Terminal Benedito Bentes / Farol"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Input(
                    label = "Horário aproximado",
                    value = horarioTransporte,
                    onValueChange = { horarioTransporte = it },
                    type = InputType.Text,
                    placeholder = "Ex: 07:30"
                )
            } else {
                SectionTitle(text = "3. Localização do problema")

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = useMyLocation,
                                    onClick = { useMyLocation = true }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = useMyLocation, onClick = { useMyLocation = true })
                            Text("Usar minha localização atual", color = MaterialTheme.colorScheme.onSurface)
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = !useMyLocation,
                                    onClick = { useMyLocation = false }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = !useMyLocation, onClick = { useMyLocation = false })
                            Text("Preencher manualmente", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                // Feedback de status do GPS
                when (val locState = locationUiState) {
                    is LocationUiState.Loading -> {
                        Row(
                            modifier = Modifier.padding(start = 4.dp, top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Buscando sua localização...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    is LocationUiState.Success -> {
                        Row(
                            modifier = Modifier.padding(start = 4.dp, top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Localização obtida com sucesso",
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryGreen
                            )
                        }
                    }
                    else -> Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                Input(
                    label = "CEP",
                    value = cep,
                    onValueChange = { cep = it },
                    type = InputType.Number,
                    mask = ::cepMask,
                    placeholder = "00000-000"
                )

                when (val state = cepUiState) {
                    is CepUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                    is CepUiState.Error -> {
                        Text(
                            text = state.message,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                    }
                    else -> Spacer(modifier = Modifier.height(16.dp))
                }

                Input(
                    label = "Rua",
                    value = rua,
                    onValueChange = { rua = it },
                    type = InputType.Text,
                    placeholder = "Digite o nome da rua"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Input(
                        label = "Bairro",
                        value = bairro,
                        onValueChange = { bairro = it },
                        modifier = Modifier.weight(1f),
                        type = InputType.Text,
                        placeholder = "Digite o nome do bairro"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Input(
                        label = "Nº",
                        value = numero,
                        onValueChange = { numero = it },
                        modifier = Modifier.width(100.dp),
                        type = InputType.Number,
                        placeholder = "Nº / S/N"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Input(
                    label = "Ponto de referência (Opcional)",
                    value = pontoReferencia,
                    onValueChange = { pontoReferencia = it },
                    type = InputType.Text,
                    placeholder = "Digite o ponto de referência"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchCurrentLocation(
    client: FusedLocationProviderClient,
    viewModel: NewProtocolViewModel
) {
    client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                viewModel.geocodeCoordinates(location.latitude, location.longitude)
            } else {
                client.lastLocation
                    .addOnSuccessListener { last ->
                        if (last != null) viewModel.geocodeCoordinates(last.latitude, last.longitude)
                        else viewModel.setLocationError("Localização não disponível. Ative o GPS e tente novamente.")
                    }
                    .addOnFailureListener { viewModel.setLocationError("Erro ao obter localização.") }
            }
        }
        .addOnFailureListener { viewModel.setLocationError("Erro ao acessar o GPS.") }
}

@Composable
private fun SectionTitle(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(22.dp)
                .background(PrimaryGreen, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
