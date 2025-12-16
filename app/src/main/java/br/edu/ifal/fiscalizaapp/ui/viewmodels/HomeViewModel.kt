package br.edu.ifal.fiscalizaapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.composables.session.SessionManager
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
    private val repository: UserRepository,
    context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<UserEntity>>(UiState.Loading)
    val uiState: StateFlow<UiState<UserEntity>> = _uiState.asStateFlow()

    private val sessionManager = SessionManager(context)

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val user = repository.getLoggedUser()

                if (user != null) {
                    _uiState.value = UiState.Success(user)
                } else {
                    _uiState.value = UiState.Error("Nenhum usuário encontrado.")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val userId = sessionManager.getUserApiId()

            if (userId != -1) {
                try {
                    repository.deleteAccount(userId)

                    sessionManager.clearSession()

                } catch (e: Exception) {
                    sessionManager.clearSession()
                    e.printStackTrace()
                }
            }
        }
    }
}