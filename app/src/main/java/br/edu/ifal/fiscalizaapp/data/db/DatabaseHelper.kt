package br.edu.ifal.fiscalizaapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.ifal.fiscalizaapp.data.db.dao.CategoryDao
import br.edu.ifal.fiscalizaapp.data.db.dao.ProtocolDao
import br.edu.ifal.fiscalizaapp.data.db.dao.UserDao
import br.edu.ifal.fiscalizaapp.data.db.data.initialUser
import br.edu.ifal.fiscalizaapp.data.db.entities.CategoryEntity
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    version = 6,
    entities = [CategoryEntity::class, UserEntity::class, ProtocolEntity::class],
    exportSchema = false
)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao

    abstract fun protocolDao(): ProtocolDao


    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHelper::class.java,
                    "fiscalizae.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                populateDatabase()
            }
        }

        suspend fun populateDatabase() {
            val userDao = getInstance(context).userDao()
            userDao.insert(initialUser)
        }
    }
}