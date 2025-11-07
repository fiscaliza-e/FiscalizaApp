package br.edu.ifal.fiscalizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.remote.CepRetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.CepService
import br.edu.ifal.fiscalizaapp.data.remote.ModelService
import br.edu.ifal.fiscalizaapp.data.repository.CepRepository
import br.edu.ifal.fiscalizaapp.data.repository.FaqRepository
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository

class ViewModelFactory : ViewModelProvider.Factory {

    private val modelService: ModelService by lazy {
        RetrofitHelper.getInstance().create(ModelService::class.java)
    }
    private val cepService: CepService by lazy {
        CepRetrofitHelper.getCepService()
    }
    private val protocolRepository: ProtocolRepository by lazy {
        ProtocolRepository(modelService)
    }
    private val faqRepository: FaqRepository by lazy {
        FaqRepository(modelService)
    }

    private val cepRepository: CepRepository by lazy {
        CepRepository(cepService)
    }

    private val userRepository: UserRepository by lazy {
        UserRepository(modelService)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProtocolViewModel::class.java) -> {
                ProtocolViewModel(protocolRepository) as T
            }
            modelClass.isAssignableFrom(FaqViewModel::class.java) -> {
                FaqViewModel(faqRepository) as T
            }
            modelClass.isAssignableFrom(CepViewModel::class.java) -> {
                CepViewModel(cepRepository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}