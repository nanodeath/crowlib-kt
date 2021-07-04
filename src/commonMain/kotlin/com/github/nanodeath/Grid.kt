package com.github.nanodeath

import kotlin.js.JsExport
import kotlin.math.abs
import kotlin.math.sqrt

@JsExport
class Grid(width: Int, height: Int) : Graph<Grid.SimpleNode> {
    private val data: Array<Array<SimpleNode?>> = Array(height) { y -> Array(width) { x -> SimpleNode(x, y) } }

    fun getAt(x: Int, y: Int): SimpleNode? {
        val row = data.getOrNull(y)
        if (row != null && x in row.indices) {
            return row[x]
        }
        throw IndexOutOfBoundsException()
    }

    fun requireAt(x: Int, y: Int): SimpleNode = getAt(x, y)!!

    fun setAt(x: Int, y: Int, node: SimpleNode?) {
        data[y][x] = node
    }

    data class SimpleNode(internal val x: Int, internal val y: Int)

    override fun successorsOf(node: SimpleNode): Collection<SimpleNode> = listOfNotNull(
        // left
        data.getOrNull(node.y)?.getOrNull(node.x - 1),
        // right
        data.getOrNull(node.y)?.getOrNull(node.x + 1),
        // up
        data.getOrNull(node.y - 1)?.getOrNull(node.x),
        // down
        data.getOrNull(node.y + 1)?.getOrNull(node.x),
    )

    override fun predecessorsOf(node: SimpleNode): Collection<SimpleNode> {
        // In Grid every node connection is bidirectional
        return successorsOf(node)
    }

    override fun approximateDistance(start: SimpleNode, end: SimpleNode): Float {
        val x = end.x - start.x
        val y = end.y - start.y
        return sqrt(x * x * 1.0F + y * y)
    }

    override fun exactDistance(node: SimpleNode, neighbor: SimpleNode): Float = 1F
}