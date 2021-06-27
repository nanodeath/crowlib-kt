package com.github.nanodeath

interface PriorityQueue<T : Comparable<T>> {
    fun isEmpty(): Boolean
    fun dequeue(): T
    fun enqueue(element: T)
}

expect fun <T : Comparable<T>> constructPriorityQueue(): PriorityQueue<T>
