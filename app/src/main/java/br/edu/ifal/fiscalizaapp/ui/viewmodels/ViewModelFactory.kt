package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.remote.CepRetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.ModelService
import br.edu.ifal.fiscalizaapp.data.repository.CepRepository
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProtocolViewModel::class.java)) {
            val service = RetrofitHelper.getInstance().create(ModelService::class.java)
            val repository = ProtocolRepository(service)
            @Suppress("UNCHECKED_CAST")
            return ProtocolViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(CepViewModel::class.java)) {
            val cepService = CepRetrofitHelper.getCepService()
            val cepRepository = CepRepository(cepService)
            @Suppress("UNCHECKED_CAST")
            return CepViewModel(cepRepository) as T
        }

        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            val service = RetrofitHelper.getInstance().create(ModelService::class.java)
            val repository = UserRepository(service)
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}