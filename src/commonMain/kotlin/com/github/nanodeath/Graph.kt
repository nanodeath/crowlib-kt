package com.github.nanodeath

public interface Graph<T : Node> {
    public fun neighborsOf(node: T): Collection<T>
}