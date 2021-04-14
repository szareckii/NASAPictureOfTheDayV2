package com.szareckii.nasapictureoftheday.ui.picture.recyclerView

import com.szareckii.nasapictureoftheday.BuildConfig

data class Change<out T>(
    val oldData: T,
    val newData: T
)

fun <T> createCombinedPayload(payloads: List<Change<T>>): Change<T> {
    if (BuildConfig.DEBUG && !payloads.isNotEmpty()) {
        error("Assertion failed")
    }
    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Change(firstChange.oldData, lastChange.newData)
}
