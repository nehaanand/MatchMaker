package neha.matchmaker.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Users::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}