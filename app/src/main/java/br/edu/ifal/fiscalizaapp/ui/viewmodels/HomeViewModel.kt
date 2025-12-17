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

    private val _updateState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val updateState: StateFlow<UpdateProfileState> = _updateState.asStateFlow()

    private val sessionManager = SessionManager(context)

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val localUser = repository.getLoggedUser()

                if (localUser != null) {
                    val userWithPhoto = if (localUser.profileImage.isNullOrBlank() && localUser.apiId != null) {
                        try {
                            val apiUser = repository.getUserById(localUser.apiId)
                            val merged = localUser.copy(profileImage = apiUser.pictureUrl)
                            repository.updateUser(merged)
                            merged
                        } catch (e: Exception) {
                            localUser
                        }
                    } else {
                        localUser
                    }

                    _uiState.value = UiState.Success(userWithPhoto)
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

    fun updateProfile(name: String, address: String) {
        viewModelScope.launch {
            _updateState.value = UpdateProfileState.Loading

            try {
                val currentUser = repository.getLoggedUser()
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        name = name.trim(),
                        address = address.trim()
                    )

                    val result = repository.updateUser(updatedUser)

                    if (result.isSuccess) {
                        _updateState.value = UpdateProfileState.Success
                        loadCurrentUser()
                    } else {
                        _updateState.value = UpdateProfileState.Error(
                            result.exceptionOrNull()?.message ?: "Erro ao atualizar perfil"
                        )
                    }
                } else {
                    _updateState.value = UpdateProfileState.Error("Usuário não encontrado")
                }
            } catch (e: Exception) {
                _updateState.value = UpdateProfileState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UpdateProfileState.Idle
    }

    fun refreshUser() {
        loadCurrentUser()
    }

    fun updateProfilePicture(apiId: Int) {
        viewModelScope.launch {
            try {
                val currentUser = repository.getLoggedUser()

                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        apiId = apiId
                    )

                    repository.updateUser(updatedUser)
                    loadCurrentUser()
                }
            } catch (e: Exception) {

            }
        }
    }
}

sealed class UpdateProfileState {
    data object Idle : UpdateProfileState()
    data object Loading : UpdateProfileState()
    data object Success : UpdateProfileState()
    data class Error(val message: String) : UpdateProfileState()
}