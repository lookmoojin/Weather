package com.piglet.foundation.extension

import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CheckResult
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.disable() {
    this.isEnabled = false
}

fun View.onClick(doSomething: () -> Unit) {
    addRipple()
    setOnClickListener {
        isClickable = false
        postDelayed({ isClickable = true }, 1500)
        doSomething()
    }
}

private fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        foreground = ContextCompat.getDrawable(context, resourceId)
    } else {
        setBackgroundResource(resourceId)
    }
}

fun View.clearKeyboardFocus() {
    clearFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Ref. from this lib -> https://github.com/ReactiveCircus/FlowBinding
 * */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun View.clickAsFlow(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        trySend(Unit)
    }
    setOnClickListener(listener)
    awaitClose { setOnClickListener(null) }
}.conflate()

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}
