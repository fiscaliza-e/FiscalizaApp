package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.data.repository.hashSha256
import br.edu.ifal.fiscalizaapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Por favor, preencha todos os campos.")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val user = repository.getUserByEmail(email.trim())

                if (user != null && user.password == hashSha256(password)) {
                    val idToSave = user.apiId ?: user.id
                    sessionManager.saveUserApiId(idToSave)
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error("E-mail ou senha inválidos.")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Ocorreu um erro ao tentar fazer login: ${e.message}")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
