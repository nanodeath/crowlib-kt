package com.github.nanodeath

interface Graph<T> {
    // The terms "successors" and "predecessors" come from the LPA* wiki: https://en.wikipedia.org/wiki/Lifelong_Planning_A*
    // Basically, successors are any nodes that are directly reachable from a given node, i.e outbound connections.
    // Conversely, predecessors are any nodes that can directly reach the given node, i.e. inbound connections.
    fun successorsOf(node: T): List<T>
    fun predecessorsOf(node: T): List<T>
    fun approximateDistance(start: T, end: T): Float
    fun exactDistance(node: T, neighbor: T): Float
}