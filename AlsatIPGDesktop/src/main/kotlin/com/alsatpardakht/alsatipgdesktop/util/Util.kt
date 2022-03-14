package com.alsatpardakht.alsatipgdesktop.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun String.getQueryKey(): String = "^\\w*=".toRegex().findAll(this).first().value.replace("=", "")

fun String.getQueryValue(): String = "=\\w*$".toRegex().findAll(this).first().value.replace("=", "")

fun String.queryStringToParameters(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    "\\w*=\\w*".toRegex().findAll(this).map {
        map.put(it.value.getQueryKey(), it.value.getQueryValue())
    }.toList()
    return map
}

@ExperimentalCoroutinesApi
fun <T> LiveData<T>.asFlow() = callbackFlow {
    val observer = observeForever { data ->
        trySend(data)
    }
    awaitClose {
        removeObserver(observer)
    }
}