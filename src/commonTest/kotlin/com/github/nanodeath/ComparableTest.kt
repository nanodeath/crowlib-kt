package com.github.nanodeath

import kotlin.test.Test
import kotlin.test.assertEquals

class ComparableTest {
    @Test
    fun sanity() {
        5.asComparable()
    }

    @Test
    fun test2() {
        5 as Comparable<Int>
    }

    @Test
    fun postDecrement() {
        var a = 1
        assertEquals(1, a--)
        assertEquals(0, a)
    }

    @Test
    fun postDecrementArrayAccess() {
        val myList = listOf("foo")
        val length = 1
    }
}