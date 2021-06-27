package com.github.nanodeath

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.expect

class PriorityQueueTests {
    @Test
    fun empty() {
        assertTrue(constructPriorityQueue<Int>().isEmpty())
    }

    @Test
    fun addRemove() {
        val queue = constructPriorityQueue<Int>()
        queue.enqueue(5)
        assertEquals(5, queue.dequeue())
        assertTrue(queue.isEmpty())
    }

    @Test
    fun addMany() {
        val input = List(100) { Random.nextInt() }
        val expected = input.sorted()

        val queue = constructPriorityQueue<Int>()
        input.forEach { queue.enqueue(it) }

        val output = mutableListOf<Int>()
        while (!queue.isEmpty()) {
            output.add(queue.dequeue())
        }
        assertEquals(expected, output)
    }

    @Test
    fun addManyFloat() {
        val input = List(100) { Random.nextFloat() }
        val expected = input.sorted()

        val queue = constructPriorityQueue<Float>()
        input.forEach { queue.enqueue(it) }

        val output = mutableListOf<Float>()
        while (!queue.isEmpty()) {
            output.add(queue.dequeue())
        }
        assertEquals(expected, output)
    }
}