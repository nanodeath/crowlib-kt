package com.github.nanodeath

interface PriorityQueue<T : Comparable<T>> {
    fun isEmpty(): Boolean
    fun peek(): T?
    fun dequeue(): T
    fun enqueue(element: T)

    /**
     * If the element was present in the queue, delete it and return true.
     * Otherwise, if no element was present or removed, return false.
     */
    fun removeIfPresent(element: T): Boolean
}

expect fun <T : Comparable<T>> constructPriorityQueue(): PriorityQueue<T>
