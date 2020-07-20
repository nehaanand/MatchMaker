package net.gahfy.rickandmorty.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import neha.matchmaker.model.database.Users
import neha.matchmaker.model.database.UsersDao

@Database(entities = [Users::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}