package br.edu.ifal.fiscalizaapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.remote.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.ModelService
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository

class ProtocolViewModelFactoryV2(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProtocolViewModelV2::class.java)) {
            val service = RetrofitHelper.getInstance().create(ModelService::class.java)
            val repository = ProtocolRepository(service)
            @Suppress("UNCHECKED_CAST")
            return ProtocolViewModelV2(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class for this factory")
    }
}