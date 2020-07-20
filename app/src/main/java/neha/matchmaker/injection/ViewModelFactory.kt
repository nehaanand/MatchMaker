package neha.matchmaker.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import neha.matchmaker.viewmodel.shadiusers.ShadiUsersViewModel
import neha.matchmaker.viewmodel.shadiusers.SplashScreenViewModel
import net.gahfy.rickandmorty.model.database.AppDatabase

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "users").fallbackToDestructiveMigration()
                .build()

        if (modelClass.isAssignableFrom(ShadiUsersViewModel::class.java)) {

           @Suppress("UNCHECKED_CAST")
            return ShadiUsersViewModel(db.usersDao()) as T
        }

        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return SplashScreenViewModel(db.usersDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}