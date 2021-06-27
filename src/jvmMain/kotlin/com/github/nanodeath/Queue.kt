package com.github.nanodeath

import java.util.*

actual fun <T : Comparable<T>> constructQueue(): Queue<T> = object : Queue<T> {
    private val queue = PriorityQueue<T>()
    override fun isEmpty() = queue.isEmpty()
    override fun dequeue(): T = queue.remove()
    override fun enqueue(element: T) {
        queue.add(element)
    }
}
