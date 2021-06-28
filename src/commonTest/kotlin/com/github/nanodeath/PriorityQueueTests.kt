package com.github.nanodeath

import kotlin.random.Random
import kotlin.test.*

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

        val output = queue.drainToList()
        assertEquals(expected, output)
    }

    @Test
    fun addManyFloat() {
        val input = List(100) { Random.nextFloat() }
        val expected = input.sorted()

        val queue = constructPriorityQueue<Float>()
        input.forEach { queue.enqueue(it) }

        val output = queue.drainToList()
        assertEquals(expected, output)
    }

    @Test
    fun dequeueMany() {
        repeat(100) {
            val input = List(30) { Random.nextInt() }

            val queue = constructPriorityQueue<Int>()
            input.forEach { queue.enqueue(it) }
            repeat(input.size / 2) { queue.dequeue() }
            val expected = input.sorted().drop(input.size / 2)

            val output = queue.drainToList()
            assertEquals(expected, output)
        }
    }

    @Test
    fun removeIfPresent() {
        val input = List(50) { it }
        val toRemove = List(20) { Random.nextInt(0, input.size) }.distinct().shuffled()

        val expected = input - toRemove

        val queue = constructPriorityQueue<Int>()
        input.forEach { queue.enqueue(it) }
        toRemove.forEach {
            assertTrue(
                queue.removeIfPresent(it)
            )
        }

        val output = queue.drainToList()
        assertEquals(expected, output)
    }

    @Test
    fun removeIfPresentWithPojo() {
        data class Wrapper(val int: Int) : Comparable<Wrapper> {
            override fun compareTo(other: Wrapper): Int = int.compareTo(other.int)
        }

        val input = List(20) { Wrapper(it) }
        val toRemove = List(10) { Wrapper(Random.nextInt(0, input.size * 2)) }.distinct().shuffled()

        val expected = input - toRemove

        val queue = constructPriorityQueue<Wrapper>()
        input.forEach { queue.enqueue(it) }
        toRemove.forEach { itemToRemove ->
            val removed = queue.removeIfPresent(itemToRemove)
            if (itemToRemove in input) {
                assertTrue(removed)
            } else {
                assertFalse(removed)
            }
        }

        val output = queue.drainToList()
        assertEquals(expected, output)
    }

    private fun <T : Comparable<T>> PriorityQueue<T>.drainToList(): List<T> {
        val output = mutableListOf<T>()
        while (!isEmpty()) {
            output.add(dequeue())
        }
        return output
    }
}