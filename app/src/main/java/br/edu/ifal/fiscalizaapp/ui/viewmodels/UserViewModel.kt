package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.navigation.screens.register.RegisterFormState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<UserEntity>) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

sealed class InsertUiState {
    object Idle : InsertUiState()
    object Loading : InsertUiState()
    object Success : InsertUiState()
    data class Error(val message: String) : InsertUiState()
}

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _insertState = MutableStateFlow<InsertUiState>(InsertUiState.Idle)
    val insertState: StateFlow<InsertUiState> = _insertState.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                val users = repository.getUsers()
                _uiState.value = UserUiState.Success(users)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun insert(form: RegisterFormState) {
        _insertState.value = InsertUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val newUser = UserEntity(
                id = 0,
                apiId = null,
                name = form.fullName.trim(),
                cpf = form.cpf.filter { it.isDigit() },
                email = form.email.trim(),
                password = form.password,
                address = "${form.street}, ${form.number}, ${form.neighborhood} - ${form.city}/${form.state}"
            )

            val result = repository.insert(newUser)

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _insertState.value = InsertUiState.Success
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Erro desconhecido"
                    _insertState.value = InsertUiState.Error(error)
                }
            }
        }
    }

    fun resetInsertState() {
        _insertState.value = InsertUiState.Idle
    }
}