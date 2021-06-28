package com.github.nanodeath

/**
 * A priority queue is a collection of [Comparable] elements. Any item you pull out of the queue is guaranteed to be the
 * "smallest" element.
 *
 * [enqueue] and [dequeue] are generally O(log(n)). [isEmpty] and [peek] are generally O(1). [removeIfPresent] is O(n).
 */
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
