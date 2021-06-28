package com.github.nanodeath

// based on minheap in https://www.educative.io/blog/data-structure-heaps-guide
class JsPriorityQueue<T : Comparable<T>> : PriorityQueue<T> {
    private val heap = JsArray<T>()
    private var elements = 0

    override fun isEmpty(): Boolean = elements == 0

    override fun peek(): T? = if (elements > 0) heap[0] else null

    override fun dequeue(): T {
        check(elements >= 0)
        return when (elements) {
            0 -> throw NoSuchElementException()
            1 -> heap[--elements]
            else -> {
                val min = heap[0]
                heap[0] = heap[elements - 1]
                elements--
                minHeapify(0)
                min
            }
        }
    }

    override fun removeIfPresent(element: T): Boolean {
        var idxToRemove = -1
        for (i in 0 until elements) {
            if (heap[i] == element) {
                idxToRemove = i
                break
            }
        }
        if (idxToRemove >= 0) {
            heap[idxToRemove] = heap[elements - 1]
            elements--
            minHeapify(idxToRemove)
            return true
        }
        return false
    }

    override fun enqueue(element: T) {
        if (elements >= heap.length) {
            elements++
            heap.push(element)
            percolateUp(heap.length - 1)
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
        // left child is smaller than current node
        if (elements > left && compareByIndex(smallest, left) > 0) {
            smallest = left
        }
        // right child is smaller than current node or left child
        if (elements > right && compareByIndex(smallest, right) > 0) {
            smallest = right
        }
        if (smallest != index) {
            val tmp = heap[smallest]
            heap[smallest] = heap[index]
            heap[index] = tmp
            minHeapify(smallest)
        }
    }

    private inline fun compareByIndex(idx1: Int, idx2: Int): Int = heap[idx1].compareTo(heap[idx2])
}

actual fun <T : Comparable<T>> constructPriorityQueue(): PriorityQueue<T> = JsPriorityQueue()
