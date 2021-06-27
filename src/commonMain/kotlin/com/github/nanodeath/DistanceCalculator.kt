package com.github.nanodeath

public interface DistanceCalculator<T : Node> {
    public fun approximateDistance(start: T, end: T): Float
    public fun exactDistance(start: T, end: T): Float
}