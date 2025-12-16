package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.repository.CategoryRepository
import br.edu.ifal.fiscalizaapp.data.repository.LocalProtocolRepository
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String
)

sealed class CategoryUiState {
    object Loading : CategoryUiState()
    data class Success(val categories: List<CategoryUiModel>) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}


open class NewProtocolViewModel(
    private val categoryRepository: CategoryRepository,
    private val localProtocolRepository: LocalProtocolRepository
) : ViewModel() {

    private val _categoryUiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    open val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState

    private val _insertUiState = MutableStateFlow<InsertUiState>(InsertUiState.Idle)
    val insertUiState: StateFlow<InsertUiState> = _insertUiState

    open fun fetchCategories() {
        viewModelScope.launch {
            _categoryUiState.value = CategoryUiState.Loading
            try {
                val networkCategories = categoryRepository.getCategories()

                val uiCategories = networkCategories.map { networkCategory ->
                    CategoryUiModel(
                        id = networkCategory.id,
                        title = networkCategory.title,
                        description = networkCategory.description
                    )
                }

                _categoryUiState.value = CategoryUiState.Success(uiCategories)
            } catch (e: Exception) {
                _categoryUiState.value = CategoryUiState.Error(e.message ?: "Erro desconhecido.")
            }
        }
    }

    fun saveProtocol(
        description: String,
        selectedCategory: CategoryUiModel?,
        useMyLocation: Boolean,
        cep: String,
        bairro: String,
        rua: String,
        numero: String,
        pontoReferencia: String,
    ) {
        viewModelScope.launch {
            _insertUiState.value = InsertUiState.Loading

            val trimmedDescription = description.trim()

            if (selectedCategory == null || description.isBlank()) {
                _insertUiState.value = InsertUiState.Error("Categoria e descrição são campos obrigatórios.")
                return@launch
            }

            if (trimmedDescription.length < 10) {
                _insertUiState.value = InsertUiState.Error("A descrição deve ter no mínimo 10 caracteres.")
                return@launch
            }

            if (!useMyLocation && (cep.isBlank() || rua.isBlank() || bairro.isBlank() || numero.isBlank())) {
                _insertUiState.value =
                    InsertUiState.Error("Preencha todos os campos de endereço ao preencher manualmente.")
                return@launch
            }

            try {
                val status = "Pendente"
                val userId = 3 // uso temporário
                val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                val newProtocol = ProtocolEntity(
                    category = selectedCategory.title,
                    description = description,
                    cep = if (useMyLocation) "" else cep,
                    bairro = if (useMyLocation) "" else bairro,
                    rua = if (useMyLocation) "" else rua,
                    numero = if (useMyLocation) "" else numero,
                    pontoReferencia = if (useMyLocation) "" else pontoReferencia,
                    useMyLocation = useMyLocation,
                    status = status,
                    userId = userId,
                    date = currentDate
                )

                localProtocolRepository.saveProtocol(newProtocol)
                _insertUiState.value = InsertUiState.Success
            } catch (e: Exception) {
                _insertUiState.value = InsertUiState.Error(e.message ?: "Erro ao salvar o protocolo.")
            }
        }
    }


}