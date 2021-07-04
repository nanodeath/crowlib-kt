package com.github.nanodeath.ui

class Observable<T>(initialValue: T) {
    var value: T = initialValue
        set(value) {
            if (value != field) {
                field = value
                notify()
            }
        }

    private val subscribers: MutableList<(T) -> Unit> = mutableListOf()

    fun subscribe(listener: (T) -> Unit) {
        subscribers.add(listener)
    }

    fun unsubscribe(listener: (T) -> Unit): Boolean = subscribers.remove { it === listener }

    fun notify() {
        value?.let { value ->
            subscribers.forEach { it.invoke(value) }
        }
    }
}