package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.remote.ModelService
import br.edu.ifal.fiscalizaapp.data.remote.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.repository.FaqRepository
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val service = RetrofitHelper.getInstance().create(ModelService::class.java)

        return when {
            modelClass.isAssignableFrom(ProtocolViewModel::class.java) -> {
                val repository = ProtocolRepository(service)
                ProtocolViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FaqViewModel::class.java) -> {
                val repository = FaqRepository(service)
                FaqViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}