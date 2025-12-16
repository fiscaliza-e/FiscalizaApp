package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.data.db.entities.CategoryEntity
import br.edu.ifal.fiscalizaapp.data.repository.CategoryRepository
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<CategoryEntity>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val categories = repository.getCategories()

                _uiState.value = UiState.Success(categories)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar categorias.")
            }
        }
    }
}