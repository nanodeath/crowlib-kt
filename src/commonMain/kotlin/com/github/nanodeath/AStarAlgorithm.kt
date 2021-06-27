package com.github.nanodeath

private data class NodeWithScore<T>(val originalNode: T, val score: Float) : Comparable<NodeWithScore<T>> {
    override fun compareTo(other: NodeWithScore<T>): Int = score.compareTo(other.score)
}

class AStarAlgorithm<T : Node>(private val graph: Graph<T>) {
    private inline fun Map<T, Float>.g(node: T) = this.getOrElse(node) { Float.POSITIVE_INFINITY }
    private inline fun Map<T, Float>.g(node: NodeWithScore<T>) = g(node.originalNode)

    fun findPath(start: T, end: T, opts: AlgorithmOpts<T>): Path<T>? {
        val distanceCalculator = opts.distanceCalculator
        fun h(node: T) = distanceCalculator.approximateDistance(node, end)

        val openSet = constructPriorityQueue<NodeWithScore<T>>().also { it.enqueue(NodeWithScore(start, 0F)) }
        val closedSet = hashSetOf<T>()
        val gMap = hashMapOf<T, Float>().apply {
            this[start] = 0F
        }

        val fMap = hashMapOf<T, Float>().apply {
            this[start] = h(start)
        }
        val lengthMap = hashMapOf<T, Int>().apply {
            this[start] = 1
        }
        val ancestorMap = hashMapOf<T, T>()

        while (!openSet.isEmpty()) {
            val next = openSet.dequeue()

            if (next.originalNode == end) {
                return reconstructPath(next.originalNode, ancestorMap, gMap.g(next), lengthMap[next.originalNode]!!)
            }

            if (!closedSet.add(next.originalNode)) continue

            for (neighbor in graph.neighborsOf(next.originalNode)) {
                if (neighbor in closedSet) continue
                val tentativeG = gMap.g(next) + distanceCalculator.exactDistance(next.originalNode, neighbor)
                if (tentativeG < gMap.g(neighbor)) {
                    ancestorMap[neighbor] = next.originalNode
                    gMap[neighbor] = tentativeG
                    val fScore = tentativeG + h(neighbor)
                    fMap[neighbor] = fScore
                    openSet.enqueue(NodeWithScore(neighbor, fScore))

                    lengthMap[neighbor] = lengthMap[next.originalNode]!! + 1
                }
            }
        }

        return null
    }

    private fun reconstructPath(finalNode: T, ancestorMap: Map<T, T>, distance: Float, length: Int): Path<T> {
        val nodes = ArrayList<T>(length)
        var node: T? = finalNode
        repeat(length) {
            nodes.add(node!!)
            node = ancestorMap[node]
        }
        nodes.reverse()
        return Path(nodes, distance)
    }
}