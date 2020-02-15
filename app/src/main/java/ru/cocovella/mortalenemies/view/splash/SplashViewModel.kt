package ru.cocovella.mortalenemies.view.splash

import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import ru.cocovella.mortalenemies.view.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser() {
        Repository.getCurrentUser().observeForever {
            baseLiveData.value = it?.let { SplashViewState(authOK = true) } ?: SplashViewState(error = NoAuthException())
        }
    }
}
