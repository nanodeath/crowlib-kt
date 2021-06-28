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

            override fun exactDistance(node: SimpleNode, neighbor: SimpleNode) = approximateDistance(node, neighbor)
        }
    }
}

data class CostNode(val x: Int, val y: Int, val costToEnter: Float) : Node {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CostNode

        return x == other.x && y == other.y

    }

    override fun hashCode(): Int = 31 * x + y
    override fun toString() = "CostNode([$x,$y], cost:$costToEnter)"

    companion object {
        fun distanceCalculator() = object : DistanceCalculator<CostNode> {
            override fun approximateDistance(start: CostNode, end: CostNode): Float {
                val x = abs(end.x - start.x).toFloat()
                val y = abs(end.y - start.y).toFloat()
                return sqrt(x * x + y * y)
            }

            override fun exactDistance(node: CostNode, neighbor: CostNode) = approximateDistance(node, neighbor) * neighbor.costToEnter
        }
    }
}