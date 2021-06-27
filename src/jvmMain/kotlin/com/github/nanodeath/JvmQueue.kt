package com.github.nanodeath

internal class JvmQueue<T> : Queue<T> {
    private val q = ArrayDeque<T>()
    override fun isEmpty() = q.isEmpty()

    override fun dequeue(): T = q.removeFirst()

    override fun enqueue(element: T) {
        q.addLast(element)
    }
}

actual fun <T> constructQueue(): Queue<T> = JvmQueue()