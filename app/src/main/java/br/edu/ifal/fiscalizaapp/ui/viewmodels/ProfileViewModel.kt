package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.db.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfilePictureState {
    object Loading : ProfilePictureState()
    data class Success(val pictureUrl: String?) : ProfilePictureState()
    data class Error(val message: String) : ProfilePictureState()
}

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val userDao: UserDao
) : ViewModel() {

    private val _pictureState = MutableStateFlow<ProfilePictureState>(ProfilePictureState.Loading)
    val pictureState: StateFlow<ProfilePictureState> = _pictureState.asStateFlow()

    fun fetchProfilePicture() {
        viewModelScope.launch {
            _pictureState.value = ProfilePictureState.Loading
            try {
                // Busca o usuário local do banco de dados
                val localUser = userDao.getUser()
                
                if (localUser?.apiId != null) {
                    // Se tiver apiId, busca o usuário da API para pegar a pictureUrl
                    val apiUser = userRepository.getUserById(localUser.apiId)
                    // Usa a URL da API (ou null se estiver vazia)
                    val pictureUrl = apiUser.pictureUrl?.takeIf { it.isNotBlank() }
                    _pictureState.value = ProfilePictureState.Success(pictureUrl)
                } else {
                    // Se não tiver apiId, usa a foto local (se existir)
                    val pictureUrl = localUser?.profileImage?.takeIf { it.isNotBlank() }
                    _pictureState.value = ProfilePictureState.Success(pictureUrl)
                }
            } catch (e: Exception) {
                // Em caso de erro, tenta usar a foto local
                try {
                    val localUser = userDao.getUser()
                    val pictureUrl = localUser?.profileImage?.takeIf { it.isNotBlank() }
                    _pictureState.value = ProfilePictureState.Success(pictureUrl)
                } catch (ex: Exception) {
                    _pictureState.value = ProfilePictureState.Error(ex.message ?: "Erro ao buscar foto de perfil")
                }
            }
        }
    }
}

