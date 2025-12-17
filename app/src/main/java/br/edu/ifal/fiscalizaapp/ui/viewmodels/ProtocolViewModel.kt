package br.edu.ifal.fiscalizaapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.composables.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

sealed class RefreshState {
    object Idle : RefreshState()
    object Loading : RefreshState()
    data class Error(val message: String) : RefreshState()
}

class ProtocolViewModel(
    private val repository: ProtocolRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val userIdFlow = MutableStateFlow(sessionManager.getUserApiId())

    val protocols: Flow<List<ProtocolEntity>> = userIdFlow.flatMapLatest { userId ->
        if (userId != -1) {
            repository.getProtocols(userId)
        } else {
            emptyFlow()
        }
    }

    private val _refreshState = MutableStateFlow<RefreshState>(RefreshState.Idle)
    val refreshState: StateFlow<RefreshState> = _refreshState.asStateFlow()

    init {
        try {
            refreshUserProtocols()
        } catch (e: Exception) {
            _refreshState.value = RefreshState.Error("Erro na inicialização: ${e.message}")
        }
    }

    fun refreshUserProtocols() {
        viewModelScope.launch {
            _refreshState.value = RefreshState.Loading

            val currentUserId = sessionManager.getUserApiId()
            userIdFlow.value = currentUserId

            if (currentUserId != -1) {
                try {
                    repository.refreshProtocols(currentUserId)
                    _refreshState.value = RefreshState.Idle
                } catch (e: Exception) {
                    _refreshState.value = RefreshState.Error(e.message ?: "Erro ao atualizar os dados.")
                }
            } else {
                _refreshState.value = RefreshState.Idle
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }
}