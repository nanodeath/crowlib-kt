package com.github.nanodeath

public interface DistanceCalculator<T : Node> {
    public fun approximateDistance(start: T, end: T): Float
    public fun exactDistance(node: T, neighbor: T): Float
}