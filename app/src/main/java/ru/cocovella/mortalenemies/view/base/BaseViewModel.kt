package ru.cocovella.mortalenemies.view.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
open class BaseViewModel<S> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Default + Job() }

    private val viewStateChannel = BroadcastChannel<S>(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    fun viewStateChannel(): ReceiveChannel<S> = viewStateChannel.openSubscription()
    fun errorChannel(): ReceiveChannel<Throwable> = errorChannel

    protected fun setData(data: S) = launch { viewStateChannel.send(data) }
    protected fun setError(e: Throwable) = launch { errorChannel.send(e) }

    override fun onCleared() {
        viewStateChannel.close()
        errorChannel.close()
        coroutineContext.cancel()
        super.onCleared()
    }

}
