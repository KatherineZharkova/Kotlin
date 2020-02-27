package ru.cocovella.mortalenemies.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T, S : BaseViewState<T>> : ViewModel() {
    open val baseLiveData = MutableLiveData<S>()
    open fun liveData(): LiveData<S> = baseLiveData
}
