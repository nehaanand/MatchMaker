package neha.matchmaker.injection.component

import dagger.Component
import neha.matchmaker.injection.module.NetworkModule
import neha.matchmaker.viewmodel.shadiusers.ShadiUsersViewModel
import neha.matchmaker.viewmodel.shadiusers.SplashScreenViewModel
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param postListViewModel PostListViewModel in which to inject the dependencies
     */


    fun inject(charactersListViewModel: ShadiUsersViewModel)
    fun inject(splashScreenViewModel: SplashScreenViewModel)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}