package com.github.nanodeath

internal value class JsArray<T> private constructor(private val array: dynamic) : Queue<T> {
    constructor() : this(js("[]"))

    operator fun get(idx: Int): T = array[idx].unsafeCast<T>()
    operator fun set(idx: Int, value: T) {
        array[idx] = value
    }

    val length: Int get() = array.length.unsafeCast<Int>()
    fun push(value: T) {
        array.push(value)
    }

    override fun dequeue(): T {
        val el = array.shift()
        return if (el == undefined) {
            throw NoSuchElementException()
        } else el.unsafeCast<T>()
    }

    override fun enqueue(element: T) {
        push(element)
    }

    override fun isEmpty(): Boolean = array.length == 0
}