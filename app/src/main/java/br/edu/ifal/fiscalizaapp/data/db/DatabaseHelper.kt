package br.edu.ifal.fiscalizaapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.ifal.fiscalizaapp.data.db.dao.CategoryDao
import br.edu.ifal.fiscalizaapp.data.db.dao.FaqDao
import br.edu.ifal.fiscalizaapp.data.db.dao.ProtocolDao
import br.edu.ifal.fiscalizaapp.data.db.dao.UserDao
import br.edu.ifal.fiscalizaapp.data.db.data.initialUser
import br.edu.ifal.fiscalizaapp.data.db.entities.CategoryEntity
import br.edu.ifal.fiscalizaapp.data.db.entities.FaqEntity
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    version = 8,
    entities = [CategoryEntity::class, UserEntity::class, ProtocolEntity::class, FaqEntity::class],
    exportSchema = false
)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    abstract fun protocolDao(): ProtocolDao
    abstract fun faqDao(): FaqDao


    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `faq` (`id` INTEGER NOT NULL, `question` TEXT NOT NULL, `answer` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHelper::class.java,
                    "fiscalizae.db"
                )
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_6_7, MIGRATION_7_8)
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