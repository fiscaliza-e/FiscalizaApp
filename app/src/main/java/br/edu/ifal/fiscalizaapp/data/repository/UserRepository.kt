package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.dto.NetworkUser
import br.edu.ifal.fiscalizaapp.data.api.user.UserAPI
import br.edu.ifal.fiscalizaapp.data.db.dao.UserDao
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity

class UserRepository(
    private val userAPI: UserAPI,
    private val userDao: UserDao
) {

    suspend fun getUsers(): List<UserEntity> {
        val apiUsers = userAPI.getUsers()
        return apiUsers.map { it.toEntity() }
    }

    suspend fun getUserById(userId: Int): NetworkUser = userAPI.getUserById(userId)

    suspend fun getLoggedUser(): UserEntity? {
        return userDao.getUser()
    }

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

    suspend fun deleteAccount(userId: Int) {
        userDao.deleteUser(userId.toLong())
    }

    suspend fun updateUser(user: UserEntity): Result<Unit> {
        return try {
            userDao.updateUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun NetworkUser.toEntity(): UserEntity {
    return UserEntity(
        apiId = id,
        name = name,
        cpf = cpf.replace(Regex("[^0-9]"), ""),
        address = "",
        profileImage = pictureUrl,
        email = email,
        password = password
    )
}
