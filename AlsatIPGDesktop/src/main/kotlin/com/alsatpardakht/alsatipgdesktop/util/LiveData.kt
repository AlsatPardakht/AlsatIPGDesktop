package com.alsatpardakht.alsatipgdesktop.util

open class LiveData<T> {

    private val observers: MutableList<Observer<T>> = mutableListOf()
    private var lastState: T? = null

    fun observeForever(observer: Observer<T>) {
        if (!observers.contains(observer)) {
            observers.add(observer)
            lastState?.let {
                observer.onChanged(it)
            }
        }
    }

    fun observeForever(onChanged: (T) -> Unit): Observer<T> {
        val observer = object : Observer<T> {
            override fun onChanged(data: T) {
                onChanged(data)
            }
        }
        this.observeForever(observer)
        return observer
    }

    fun removeObserver(observer: Observer<T>) = observers.remove(observer)

    fun removeAllObservers() = observers.clear()

    protected fun notifyAll(data: T) {
        lastState = data
        for (observer in observers) {
            observer.onChanged(data)
        }
    }
}