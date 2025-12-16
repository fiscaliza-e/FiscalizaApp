package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.fiscalizaapp.model.FaqItem
import br.edu.ifal.fiscalizaapp.data.repository.FaqRepository
import br.edu.ifal.fiscalizaapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FaqViewModel (private val repository: FaqRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<FaqItem>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<FaqItem>>> = _uiState.asStateFlow()

    init {
        fetchFaqItems()
    }

    private fun fetchFaqItems() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val faqList = repository.getFaq()
                _uiState.value = UiState.Success(faqList)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
