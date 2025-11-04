package br.edu.ifal.fiscalizaapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.ifal.fiscalizaapp.model.CategoryEntity
import br.edu.ifal.fiscalizaapp.model.UserEntity

@Database(
    version = 1,
    entities = [CategoryEntity::class, UserEntity::class]
)

abstract class DatabaseHelper : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    
    companion object {
         fun getInstance(context: Context): DatabaseHelper {
             return Room.databaseBuilder(
                 context,
                 DatabaseHelper::class.java,
                 "fiscalizae.db"
             )
             .fallbackToDestructiveMigration(dropAllTables = true)
             .build()
         }
    }
}

