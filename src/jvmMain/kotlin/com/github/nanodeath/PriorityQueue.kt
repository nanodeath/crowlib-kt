package com.github.nanodeath

import java.util.*

actual fun <T : Comparable<T>> constructPriorityQueue(): PriorityQueue<T> = object :
    PriorityQueue<T> {
    private val queue = PriorityQueue<T>()
    override fun isEmpty() = queue.isEmpty()
    override fun dequeue(): T = queue.remove()
    override fun enqueue(element: T) {
        queue.add(element)
    }
}

actual fun <T> Comparable<T>.asComparable(): Comparable<T> = this