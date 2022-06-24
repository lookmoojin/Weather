package com.piglet.foundation.extension

fun String.ifNotNullOrEmpty(block: ((String) -> Unit)): String {
    if (isNotEmpty()) block(this)
    return this
}

fun String?.ifIsNullOrEmpty(defaultValue: () -> String): String {
    return if (isNullOrEmpty()) {
        defaultValue()
    } else {
        this
    }
}

fun String?.doOnNullOrEmpty(block: ((String?) -> Unit)) {
    if (isNullOrEmpty()) block(this)
}
