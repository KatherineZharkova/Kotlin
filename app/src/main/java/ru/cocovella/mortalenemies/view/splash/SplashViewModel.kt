package ru.cocovella.mortalenemies.view.splash

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import ru.cocovella.mortalenemies.view.base.BaseViewModel

@ExperimentalCoroutinesApi
class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean?>() {
    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let { setData(true) } ?: setError(NoAuthException())
        }
    }
}
