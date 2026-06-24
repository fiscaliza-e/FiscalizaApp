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
    entities = [UserEntity::class, CategoryEntity::class, ProtocolEntity::class, FaqEntity::class],
    version = 12,
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

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE protocols ADD COLUMN photoUris TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE protocols ADD COLUMN numeroPoste TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE protocols ADD COLUMN areaSaneamento TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE protocols ADD COLUMN nomeOrgao TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE protocols ADD COLUMN numeroTransporte TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE protocols ADD COLUMN linhaTransporte TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE protocols ADD COLUMN horarioTransporte TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE protocols ADD COLUMN latitude REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE protocols ADD COLUMN longitude REAL NOT NULL DEFAULT 0.0")
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE protocols ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
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
                    .addMigrations(MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12)
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