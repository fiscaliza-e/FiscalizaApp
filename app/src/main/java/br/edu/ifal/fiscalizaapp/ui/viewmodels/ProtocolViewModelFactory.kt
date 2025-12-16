package br.edu.ifal.fiscalizaapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.data.api.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.api.protocol.ProtocolAPI

class ProtocolViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProtocolViewModel::class.java)) {
            val retrofit = RetrofitHelper.getInstance()
            val service = retrofit.create(ProtocolAPI::class.java)
            val repository = ProtocolRepository(service)
            @Suppress("UNCHECKED_CAST")
            return ProtocolViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
