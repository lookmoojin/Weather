package com.piglet.weather.base

import androidx.lifecycle.ViewModel
import com.piglet.weather.extension.LaunchSafe
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

open class ScopedViewModel : ViewModel(), CoroutineScope, LaunchSafe {

    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + job + safeCoroutineExceptionHandler())
    private val errorLiveEvent = LiveEvent<Throwable>()

    override fun onCleared() {
        uiScope.coroutineContext.cancelChildren()
        super.onCleared()
    }

    override val coroutineContext: CoroutineContext
        get() = uiScope.coroutineContext

    override fun onCancellation(e: CancellationException) {
        // DO NOTHING
    }

    override fun onFailure(t: Throwable) {
        errorLiveEvent.postValue(t)
    }

    private fun safeCoroutineExceptionHandler() = CoroutineExceptionHandler { _, throwable ->
        Thread.currentThread()
            .uncaughtExceptionHandler
            ?.uncaughtException(Thread.currentThread(), throwable)
    }
}
