package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.model.Protocol
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProtocolViewModel(private val repository: ProtocolRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Protocol>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Protocol>>> = _uiState

    init {
        fetchProtocolsByUserId(3)
    }

    private fun fetchProtocols() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val protocols = repository.getProtocols()
                _uiState.value = UiState.Success(data = protocols)
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
                _uiState.value = UiState.Success(data = protocols)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

}