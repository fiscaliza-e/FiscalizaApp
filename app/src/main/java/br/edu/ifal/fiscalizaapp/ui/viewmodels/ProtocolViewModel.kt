package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.composables.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

sealed class RefreshState {
    object Idle : RefreshState()
    object Loading : RefreshState()
    data class Error(val message: String) : RefreshState()
}

enum class SortOrder { NEWEST_FIRST, OLDEST_FIRST }

enum class ProtocolFilter { ALL, PENDING, VIEWED, PROCESSED, ARCHIVED }

class ProtocolViewModel(
    private val repository: ProtocolRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val userIdFlow = MutableStateFlow(sessionManager.getUserApiId())

    private val protocols: Flow<List<ProtocolEntity>> = userIdFlow.flatMapLatest { userId ->
        if (userId != -1) {
            repository.getProtocols(userId)
        } else {
            emptyFlow()
        }
    }

    private val _filterStatus = MutableStateFlow(ProtocolFilter.ALL)
    val filterStatus: StateFlow<ProtocolFilter> = _filterStatus.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST_FIRST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val filteredProtocols: Flow<List<ProtocolEntity>> = combine(
        protocols, _filterStatus, _sortOrder
    ) { list, filter, order ->
        val filtered = if (filter == ProtocolFilter.ALL) list
        else list.filter { matchesFilter(it.status, filter) }
        when (order) {
            SortOrder.NEWEST_FIRST -> filtered.sortedByDescending { it.date }
            SortOrder.OLDEST_FIRST -> filtered.sortedBy { it.date }
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

    fun setFilterStatus(filter: ProtocolFilter) {
        _filterStatus.value = filter
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
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

private fun matchesFilter(status: String, filter: ProtocolFilter): Boolean {
    val s = status.uppercase()
    return when (filter) {
        ProtocolFilter.ALL -> true
        ProtocolFilter.PENDING -> s in setOf("PENDENTE", "PENDING")
        ProtocolFilter.VIEWED -> s in setOf("VISUALIZADO", "VISUALIZADA", "VIEWED")
        ProtocolFilter.PROCESSED -> s in setOf("PROCESSADO", "PROCESSADA", "PROCESSED")
        ProtocolFilter.ARCHIVED -> s in setOf("ARQUIVADO", "ARQUIVADA", "ARCHIVED")
    }
}
