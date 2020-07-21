package neha.matchmaker.base

import android.arch.lifecycle.ViewModel
import neha.matchmaker.injection.component.DaggerViewModelInjector
import neha.matchmaker.injection.component.ViewModelInjector
import neha.matchmaker.injection.module.NetworkModule
import neha.matchmaker.viewmodels.UsersViewModel
import neha.matchmaker.viewmodels.SplashScreenViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is UsersViewModel -> injector.inject(this)
            is SplashScreenViewModel -> injector.inject(this)

        }
    }
}