package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.model.Protocol
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val protocols: List<Protocol>) : UiState()
    data class Error(val message: String) : UiState()
}

class ProtocolViewModel(private val repository: ProtocolRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchProtocolsByUserId(3)
    }

    private fun fetchProtocols() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val protocols = repository.getProtocols()
                _uiState.value = UiState.Success(protocols)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun fetchProtocolsByUserId(userId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val protocols = repository.getProtocolsByUserId(userId)
                _uiState.value = UiState.Success(protocols)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

}