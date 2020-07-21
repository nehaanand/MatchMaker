package neha.matchmaker.injection.component

import dagger.Component
import neha.matchmaker.injection.module.NetworkModule
import neha.matchmaker.viewmodels.UsersViewModel
import neha.matchmaker.viewmodels.SplashScreenViewModel
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified ViewModel.
     */


    fun inject(charactersListViewModel: UsersViewModel)

    fun inject(splashScreenViewModel: SplashScreenViewModel)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}