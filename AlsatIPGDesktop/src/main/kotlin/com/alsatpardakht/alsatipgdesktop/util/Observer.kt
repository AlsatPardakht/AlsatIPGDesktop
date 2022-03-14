package com.alsatpardakht.alsatipgdesktop.util

interface Observer<T> {
    fun onChanged(data: T)
}