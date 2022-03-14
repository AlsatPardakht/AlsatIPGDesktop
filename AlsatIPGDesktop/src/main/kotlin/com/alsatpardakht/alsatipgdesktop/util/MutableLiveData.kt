package com.alsatpardakht.alsatipgdesktop.util

class MutableLiveData<T> : LiveData<T>() {
    fun postValue(data: T) {
        super.notifyAll(data)
    }
}