package com.github.nanodeath

class Grid(val width: Int, val height: Int) : Graph<SimpleNode> {
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

    override fun neighborsOf(node: SimpleNode): Collection<SimpleNode> =
        listOfNotNull(
            // left
            data.getOrNull(node.y)?.getOrNull(node.x - 1),
            // right
            data.getOrNull(node.y)?.getOrNull(node.x + 1),
            // up
            data.getOrNull(node.y - 1)?.getOrNull(node.x),
            // down
            data.getOrNull(node.y + 1)?.getOrNull(node.x),
        )

    companion object {

    }
}