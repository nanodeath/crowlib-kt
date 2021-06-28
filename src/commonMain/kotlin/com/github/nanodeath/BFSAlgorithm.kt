package com.github.nanodeath

class BFSAlgorithm<T : Node>(private val graph: Graph<T>) {
    fun findPath(start: T, end: T, opts: AlgorithmOpts<T>): Path<T>? {
        val openSet = constructQueue<T>().also { it.enqueue(start) }
        val ancestorMap = hashMapOf<T, T?>().also { it[start] = null }
        val lengthMap = hashMapOf<T, Int>().apply {
            this[start] = 1
        }
        val visited = hashSetOf<T>()
        while (openSet.isNotEmpty()) {
            val next = openSet.dequeue()
            if (!visited.add(next)) continue
            if (next == end) {
                return reconstructPath(next, ancestorMap, lengthMap[next]!!, opts)
            }
            val nextLength = lengthMap[next]!!
            for (neighbor in graph.successorsOf(next)) {
                if (!ancestorMap.containsKey(neighbor)) {
                    openSet.enqueue(neighbor)
                    ancestorMap[neighbor] = next
                    lengthMap[neighbor] = nextLength + 1
                }
            }
        }
        return null
    }

    private fun reconstructPath(finalNode: T, ancestorMap: Map<T, T?>, length: Int, opts: AlgorithmOpts<T>): Path<T> {
        val nodes = ArrayList<T>(length)
        var node: T? = finalNode
        var distance = 0F
        for (i in 1..length) {
            nodes.add(node!!)
            val ancestor = ancestorMap[node]
            if (ancestor != null) {
                // ancestor should only be null for our start node
                distance += opts.distanceCalculator.exactDistance(ancestor, node)
            }
            node = ancestor
        }
        nodes.reverse()
        return Path(nodes, distance)
    }
}