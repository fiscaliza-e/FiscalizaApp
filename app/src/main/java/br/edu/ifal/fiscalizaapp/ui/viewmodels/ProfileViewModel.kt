package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.db.dao.UserDao
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.data.session.SessionManager
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfilePictureState {
    data object Loading : ProfilePictureState()
    data class Success(val pictureUrl: String?) : ProfilePictureState()
    data class Error(val message: String) : ProfilePictureState()
}

class ProfileViewModel(
    private val userRepository: UserRepository,
    @Suppress("UNUSED_PARAMETER") userDao: UserDao,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<UserEntity>>(UiState.Loading)
    val uiState: StateFlow<UiState<UserEntity>> = _uiState.asStateFlow()

    private val _updateState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val updateState: StateFlow<UpdateProfileState> = _updateState.asStateFlow()

    private val _pictureState = MutableStateFlow<ProfilePictureState>(ProfilePictureState.Loading)
    val pictureState: StateFlow<ProfilePictureState> = _pictureState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val localUser = userRepository.getLoggedUser(sessionManager.getUserApiId())
                if (localUser != null) {
                    val userWithPhoto = if (localUser.profileImage.isNullOrBlank() && localUser.apiId != null) {
                        try {
                            val apiUser = userRepository.getUserById(localUser.apiId)
                            val merged = localUser.copy(profileImage = apiUser.pictureUrl)
                            userRepository.updateUser(merged)
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

    fun refreshUser() {
        viewModelScope.launch {
            try {
                val localUser = userRepository.getLoggedUser(sessionManager.getUserApiId())
                if (localUser != null) {
                    _uiState.value = UiState.Success(localUser)
                }
            } catch (_: Exception) {}
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
                    userRepository.deleteAccount(userId)
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
                val currentUser = userRepository.getLoggedUser(sessionManager.getUserApiId())
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        name = name.trim(),
                        address = address.trim()
                    )
                    val result = userRepository.updateUser(updatedUser)
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

    fun saveProfileImagePath(path: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getLoggedUser(sessionManager.getUserApiId())
                if (user != null) {
                    userRepository.updateUser(user.copy(profileImage = path))
                    refreshUser()
                }
            } catch (_: Exception) {}
        }
    }

    fun clearProfileImage() {
        viewModelScope.launch {
            try {
                val user = userRepository.getLoggedUser(sessionManager.getUserApiId())
                if (user != null) {
                    userRepository.updateUser(user.copy(profileImage = null))
                    refreshUser()
                }
            } catch (_: Exception) {}
        }
    }

    fun updateProfilePicture(apiId: Int) {
        viewModelScope.launch {
            try {
                val currentUser = userRepository.getLoggedUser(sessionManager.getUserApiId())
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(apiId = apiId, profileImage = null)
                    userRepository.updateUser(updatedUser)
                    loadCurrentUser()
                }
            } catch (_: Exception) {}
        }
    }

    fun fetchProfilePicture() {
        viewModelScope.launch {
            _pictureState.value = ProfilePictureState.Loading
            val sessionId = sessionManager.getUserApiId()
            try {
                val localUser = userRepository.getLoggedUser(sessionId)
                if (localUser?.apiId != null) {
                    val apiUser = userRepository.getUserById(localUser.apiId)
                    val pictureUrl = apiUser.pictureUrl?.takeIf { it.isNotBlank() }
                    _pictureState.value = ProfilePictureState.Success(pictureUrl)
                } else {
                    val pictureUrl = localUser?.profileImage?.takeIf { !it.isNullOrBlank() }
                    _pictureState.value = ProfilePictureState.Success(pictureUrl)
                }
            } catch (e: Exception) {
                try {
                    val localUser = userRepository.getLoggedUser(sessionId)
                    val pictureUrl = localUser?.profileImage?.takeIf { !it.isNullOrBlank() }
                    _pictureState.value = ProfilePictureState.Success(pictureUrl)
                } catch (ex: Exception) {
                    _pictureState.value =
                        ProfilePictureState.Error(ex.message ?: "Erro ao buscar foto de perfil")
                }
            }
        }
    }
}
