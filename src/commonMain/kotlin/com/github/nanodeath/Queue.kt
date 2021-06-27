package com.github.nanodeath

interface Queue<T : Comparable<T>> {
    fun isEmpty(): Boolean
    fun dequeue(): T
    fun enqueue(element: T)
}

expect fun <T : Comparable<T>> constructQueue(): Queue<T>