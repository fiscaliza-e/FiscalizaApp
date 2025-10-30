import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.ifal.fiscalizaapp.db.CategoryDao
import br.edu.ifal.fiscalizaapp.model.CategoryEntity

@Database(
    version = 1,
    entities = [CategoryEntity::class]
)

abstract class DatabaseHelper : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    companion object {
         fun getInstance(context: Context): DatabaseHelper {
             return Room.databaseBuilder(
                 context,
                 DatabaseHelper::class.java,
                 "fiscalizae.db"
             ).build()
         }
    }
}

