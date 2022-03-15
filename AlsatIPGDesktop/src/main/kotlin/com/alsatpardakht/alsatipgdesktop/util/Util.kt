package com.alsatpardakht.alsatipgdesktop.util

import com.alsatpardakht.alsatipgcore.core.util.decodeQueryParameter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.net.URI

fun String.getQueryKey(): String = "^\\w*=".toRegex().findAll(this).first().value.replace("=", "")

fun String.getQueryValue(): String = "=\\w*$".toRegex().findAll(this).first().value.replace("=", "")

fun String.queryStringToParameters(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    "\\w*=\\w*".toRegex().findAll(this).forEach {
        map[it.value.getQueryKey()] = it.value.getQueryValue()
    }
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

fun getDecodedQueryValueByKey(data: URI, key: String): String {
    for (param in data.query.queryStringToParameters()) {
        if (param.key == key) return param.value.decodeQueryParameter()
    }
    return ""
}