package com.github.nanodeath

internal class JsQueue<T> : Queue<T> {
    private val data = js("[]")
    override fun isEmpty(): Boolean {
        return data.length == 0
    }

    override fun dequeue(): T {
        val el = data.shift()
        return if (el == undefined) {
            throw NoSuchElementException()
        } else el as T
    }

    override fun enqueue(element: T) {
        data.push(element)
    }
}

actual fun <T> constructQueue(): Queue<T> = JsQueue()