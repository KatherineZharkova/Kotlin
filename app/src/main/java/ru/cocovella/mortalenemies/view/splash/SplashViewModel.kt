package ru.cocovella.mortalenemies.view.splash

import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import ru.cocovella.mortalenemies.view.base.BaseViewModel

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser() {
        repository.getCurrentUser().observeForever {
            baseLiveData.value = it?.let { SplashViewState(authOK = true) } ?: let { SplashViewState(error = NoAuthException()) }
        }
    }
}
