package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.api.dto.CepResponse
import br.edu.ifal.fiscalizaapp.data.repository.CepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class CepUiState {
    object Idle : CepUiState()
    object Loading : CepUiState()
    data class Success(val address: CepResponse) : CepUiState()
    data class Error(val message: String) : CepUiState()
}

class CepViewModel(private val repository: CepRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<CepUiState>(CepUiState.Idle)
    val uiState: StateFlow<CepUiState> = _uiState.asStateFlow()

    fun fetchAddressByCep(cep: String) {
        viewModelScope.launch {
            _uiState.value = CepUiState.Loading
            try {
                val response = repository.getAddressByCep(cep)
                if (response.error != true) {
                    _uiState.value = CepUiState.Success(response)
                } else {
                    _uiState.value = CepUiState.Error("CEP não encontrado.")
                }
            } catch (e: Exception) {
                _uiState.value = CepUiState.Error(e.message ?: "Erro ao buscar CEP")
            }
        }
    }

    fun resetCepState() {
        _uiState.value = CepUiState.Idle
    }
}