package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.remote.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.ModelService
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProtocolViewModel::class.java)) {
            val service = RetrofitHelper.getInstance().create(ModelService::class.java)
            val repository = ProtocolRepository(service)
            @Suppress("UNCHECKED_CAST")
            return ProtocolViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}