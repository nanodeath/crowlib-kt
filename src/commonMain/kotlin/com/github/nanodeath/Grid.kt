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
}

class CostGrid(val width: Int, val height: Int) : Graph<CostNode> {
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
}