package com.github.nanodeath

class JsPriorityQueue<T : Comparable<T>> : PriorityQueue<T> {
    private val heap = js("[]")
    private var elements = 0

    override fun isEmpty(): Boolean {
        return elements == 0
    }

    override fun dequeue(): T {
        check(elements >= 0)
        return when (elements) {
            0 -> throw NoSuchElementException()
            1 -> {
                heap[--elements] as T
            }
            else -> {
                val min = heap[0]
                heap[0] = this.heap[elements - 1]
                elements--
                minHeapify(0)
                min as T
            }
        }
    }

    override fun enqueue(element: T) {
        if (elements >= heap.length as Int) {
            elements++
            heap.push(element)
            percolateUp((heap.length - 1) as Int)
        } else {
            heap[elements] = element
            elements++
            percolateUp(elements - 1)
        }
    }

    private tailrec fun percolateUp(index: Int) {
        val parent = (index - 1) / 2
        if (index <= 0) return
        if (heap[parent] > heap[index]) {
            val tmp = heap[parent]
            heap[parent] = heap[index]
            heap[index] = tmp
            percolateUp(parent)
        }
    }

    private tailrec fun minHeapify(index: Int) {
        val left = index * 2 + 1
        val right = index * 2 + 2
        var smallest = index
        if (elements > left && compareByIndex(smallest, left) > 0) { //heap[smallest] > heap[left]) {
            smallest = left
        }
        if (elements > right && compareByIndex(smallest, right) > 0) {//heap[smallest] > heap[right]) {
            smallest = right
        }
        if (smallest != index) {
            val tmp = heap[smallest]
            heap[smallest] = heap[index]
            heap[index] = tmp
            minHeapify(smallest)
        }
    }

    private inline fun compareByIndex(idx1: Int, idx2: Int): Int {
        return (heap[idx1] as Comparable<T>).compareTo(heap[idx2])
    }
}

actual fun <T : Comparable<T>> constructPriorityQueue(): PriorityQueue<T> = JsPriorityQueue()

actual fun <T> Comparable<T>.asComparable(): Comparable<T> = this