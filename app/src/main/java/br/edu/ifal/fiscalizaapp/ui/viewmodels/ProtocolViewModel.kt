package br.edu.ifal.fiscalizaapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.model.Protocol
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.composables.session.SessionManager
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProtocolViewModel(
    private val repository: ProtocolRepository,
    context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Protocol>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Protocol>>> = _uiState

    private val sessionManager = SessionManager(context)

    fun fetchUserProtocols() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val userId = sessionManager.getUserApiId()
                if (userId != -1) {
                    val protocols = repository.getProtocolsByUserId(userId)
                    _uiState.value = UiState.Success(protocols)
                } else {
                    // Se não houver usuário logado, retorna uma lista vazia.
                    _uiState.value = UiState.Success(emptyList())
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }
}
