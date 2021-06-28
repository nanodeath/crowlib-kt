package com.github.nanodeath

internal value class JsArray<T> private constructor(private val array: dynamic) {
    constructor() : this(js("[]"))

    operator fun get(idx: Int): T = array[idx].unsafeCast<T>()
    operator fun set(idx: Int, value: T) {
        array[idx] = value
    }

    val length: Int get() = array.length.unsafeCast<Int>()
    fun push(value: T) {
        array.push(value)
    }
}