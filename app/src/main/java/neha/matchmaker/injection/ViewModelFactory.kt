package neha.matchmaker.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import neha.matchmaker.model.database.AppDatabase
import neha.matchmaker.viewmodels.UsersViewModel
import neha.matchmaker.viewmodels.SplashScreenViewModel

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "users").fallbackToDestructiveMigration()
                .build()
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(db.usersDao()) as T
        }
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SplashScreenViewModel(db.usersDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}