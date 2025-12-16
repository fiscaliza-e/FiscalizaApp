package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.user.UserAPI
import br.edu.ifal.fiscalizaapp.data.db.dao.UserDao
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity

class UserRepository(private val userAPI: UserAPI, private val userDao: UserDao) {

    suspend fun getUsers(): List<UserEntity> {
        return try {
            val apiUsers = userAPI.getUsers()
            userDao.insertAll(apiUsers)
            apiUsers
        } catch (e: Exception) {
            listOfNotNull(userDao.getUser())
        }
    }

    suspend fun getUserById(userId: Int) = userAPI.getUserById(userId)

    suspend fun insert(user: UserEntity): Result<Unit> {
        return try {
            val existingEmail = userDao.getByEmail(user.email)
            if (existingEmail != null) {
                return Result.failure(Exception("Este e-mail já está cadastrado."))
            }

            val existingCpf = userDao.getByCpf(user.cpf)
            if (existingCpf != null) {
                return Result.failure(Exception("Este CPF já está cadastrado."))
            }

            userDao.insert(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}