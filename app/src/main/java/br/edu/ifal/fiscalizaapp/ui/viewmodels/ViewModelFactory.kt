package br.edu.ifal.fiscalizaapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.remote.CepRetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.remote.CepService
import br.edu.ifal.fiscalizaapp.data.remote.ModelService
import br.edu.ifal.fiscalizaapp.data.repository.CategoryRepository
import br.edu.ifal.fiscalizaapp.data.repository.CepRepository
import br.edu.ifal.fiscalizaapp.data.repository.FaqRepository
import br.edu.ifal.fiscalizaapp.data.repository.LocalProtocolRepository
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.db.DatabaseHelper

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

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

    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(modelService)
    }

    private val localProtocolRepository: LocalProtocolRepository by lazy {
        LocalProtocolRepository(DatabaseHelper.getInstance(context).protocolDao())
    }

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
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(NewProtocolViewModel::class.java) -> {
                NewProtocolViewModel(categoryRepository, localProtocolRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}