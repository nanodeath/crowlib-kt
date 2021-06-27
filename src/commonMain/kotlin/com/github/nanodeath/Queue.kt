package com.github.nanodeath

interface Queue<T> {
    fun isEmpty(): Boolean
    fun dequeue(): T
    fun enqueue(element: T)
}
inline fun <T> Queue<T>.isNotEmpty() = !isEmpty()

expect fun <T> constructQueue(): Queue<T>

expect fun <T> Comparable<T>.asComparable(): Comparable<T>