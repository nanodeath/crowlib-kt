package com.github.nanodeath

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt

class CostGrid(val width: Int, val height: Int) : Graph<CostGrid.CostNode> {
    private val data: Array<Array<CostNode?>> = Array(height) { y -> Array(width) { x -> CostNode(x, y, 1F) } }

    fun getAt(x: Int, y: Int): CostNode? {
        val row = data.getOrNull(y)
        if (row != null && x in row.indices) {
            return row[x]
        }
        throw IndexOutOfBoundsException()
    }

    fun requireAt(x: Int, y: Int): CostNode = getAt(x, y)!!

    fun setAt(x: Int, y: Int, node: CostNode?) {
        data[y][x] = node
    }

    fun setCost(x: Int, y: Int, costToEnter: Float) {
        setAt(x, y, CostNode(x, y, costToEnter))
    }

    override fun successorsOf(node: CostNode): Collection<CostNode> = listOfNotNull(
        // left
        data.getOrNull(node.y)?.getOrNull(node.x - 1),
        // right
        data.getOrNull(node.y)?.getOrNull(node.x + 1),
        // up
        data.getOrNull(node.y - 1)?.getOrNull(node.x),
        // down
        data.getOrNull(node.y + 1)?.getOrNull(node.x),
    )

    override fun predecessorsOf(node: CostNode): Collection<CostNode> {
        // In CostGrid every node connection is bidirectional
        return successorsOf(node)
    }

    override fun approximateDistance(start: CostNode, end: CostNode): Float {
        val x = end.x - start.x
        val y = end.y - start.y
        return sqrt(x * x * 1.0F + y * y)
    }
    override fun exactDistance(node: CostNode, neighbor: CostNode): Float = neighbor.costToEnter

    data class CostNode(val x: Int, val y: Int, val costToEnter: Float) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as CostNode

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        override fun toString(): String = "CostNode($x,$y -> $$costToEnter)"
    }
}