package br.edu.ifal.fiscalizaapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifal.fiscalizaapp.data.api.cep.CepRetrofitHelper
import br.edu.ifal.fiscalizaapp.data.api.cep.CepAPI
import br.edu.ifal.fiscalizaapp.data.api.RetrofitHelper
import br.edu.ifal.fiscalizaapp.data.api.category.CategoryAPI
import br.edu.ifal.fiscalizaapp.data.api.faq.FaqAPI
import br.edu.ifal.fiscalizaapp.data.api.protocol.ProtocolAPI
import br.edu.ifal.fiscalizaapp.data.api.user.UserAPI
import br.edu.ifal.fiscalizaapp.data.repository.CategoryRepository
import br.edu.ifal.fiscalizaapp.data.repository.CepRepository
import br.edu.ifal.fiscalizaapp.data.repository.FaqRepository
import br.edu.ifal.fiscalizaapp.data.repository.LocalProtocolRepository
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.data.db.DatabaseHelper
import br.edu.ifal.fiscalizaapp.data.db.dao.UserDao

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val cepAPI: CepAPI by lazy {
        CepRetrofitHelper.getCepAPI()
    }

    private val protocolAPI: ProtocolAPI by lazy {
        RetrofitHelper.getInstance().create(ProtocolAPI::class.java)
    }

    private val faqAPI: FaqAPI by lazy {
        RetrofitHelper.getInstance().create(FaqAPI::class.java)
    }

    private val userAPI: UserAPI by lazy {
        RetrofitHelper.getInstance().create(UserAPI::class.java)
    }

    private val userDao: UserDao by lazy {
        DatabaseHelper.getInstance(context).userDao()
    }


    private val categoryAPI: CategoryAPI by lazy {
        RetrofitHelper.getInstance().create(CategoryAPI::class.java)
    }

    private val protocolRepository: ProtocolRepository by lazy {
        ProtocolRepository(protocolAPI)
    }
    private val faqRepository: FaqRepository by lazy {
        FaqRepository(faqAPI)
    }
    private val cepRepository: CepRepository by lazy {
        CepRepository(cepAPI)
    }
    private val userRepository: UserRepository by lazy {
        UserRepository(userAPI, userDao)
    }
    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(categoryAPI)
    }

    // TODO: O que é isso aqui abaixo? Pra que serve?
    private val localProtocolRepository: LocalProtocolRepository by lazy {
        LocalProtocolRepository(DatabaseHelper.getInstance(context).protocolDao())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(ProtocolViewModel::class.java) -> {
                ProtocolViewModel(protocolRepository) as T
            }
            modelClass.isAssignableFrom(FaqViewModel::class.java) -> {
                FaqViewModel(faqRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(NewProtocolViewModel::class.java) -> {
                NewProtocolViewModel(categoryRepository, localProtocolRepository) as T
            }

            modelClass.isAssignableFrom(CepViewModel::class.java) -> {
                CepViewModel(cepRepository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(ProtocolViewModelV2::class.java) -> {
                 ProtocolViewModelV2(protocolRepository, context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}