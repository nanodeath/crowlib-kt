package com.github.nanodeath

import kotlin.math.abs
import kotlin.math.sqrt

public interface Node {
}

data class SimpleNode(val x: Int, val y: Int) : Node {
    companion object {
        fun distanceCalculator() = object : DistanceCalculator<SimpleNode> {
            override fun approximateDistance(start: SimpleNode, end: SimpleNode): Float {
                val x = abs(end.x - start.x).toFloat()
                val y = abs(end.y - start.y).toFloat()
                return sqrt(x * x + y * y)
            }

            override fun exactDistance(start: SimpleNode, end: SimpleNode) = approximateDistance(start, end)
        }
    }
}

