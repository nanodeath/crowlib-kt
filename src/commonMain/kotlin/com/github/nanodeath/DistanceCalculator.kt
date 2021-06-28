package com.github.nanodeath

interface DistanceCalculator<T : Node> {
    fun approximateDistance(start: T, end: T): Float
    fun exactDistance(node: T, neighbor: T): Float
}