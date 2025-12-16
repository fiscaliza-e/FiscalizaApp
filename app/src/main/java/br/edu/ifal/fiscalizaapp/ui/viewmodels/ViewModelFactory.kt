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
import br.edu.ifal.fiscalizaapp.data.repository.ProtocolRepository
import br.edu.ifal.fiscalizaapp.data.repository.UserRepository
import br.edu.ifal.fiscalizaapp.data.db.DatabaseHelper
import br.edu.ifal.fiscalizaapp.data.db.dao.ProtocolDao
import br.edu.ifal.fiscalizaapp.data.db.dao.UserDao


class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val database by lazy {
        DatabaseHelper.getInstance(context)
    }

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

    private val protocolDao: ProtocolDao by lazy {
        DatabaseHelper.getInstance(context).protocolDao()
    }

    private val categoryAPI: CategoryAPI by lazy {
        RetrofitHelper.getInstance().create(CategoryAPI::class.java)
    }

    private val protocolRepository: ProtocolRepository by lazy {
        ProtocolRepository(protocolAPI, protocolDao)
    }

    private val faqRepository: FaqRepository by lazy {
        FaqRepository(faqAPI, database.faqDao())
    }

    private val cepRepository: CepRepository by lazy {
        CepRepository(cepAPI)
    }

    private val userRepository: UserRepository by lazy {
        UserRepository(userAPI, database.userDao())
    }

    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(categoryAPI, database.categoryDao())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(ProtocolViewModel::class.java) -> {
                ProtocolViewModel(protocolRepository, context) as T
            }
            modelClass.isAssignableFrom(FaqViewModel::class.java) -> {
                FaqViewModel(faqRepository, context) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, context) as T
            }
            modelClass.isAssignableFrom(NewProtocolViewModel::class.java) -> {
                NewProtocolViewModel(categoryRepository, protocolRepository) as T
            }
            modelClass.isAssignableFrom(CepViewModel::class.java) -> {
                CepViewModel(cepRepository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                CategoryViewModel(categoryRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}