package com.github.nanodeath

import kotlin.math.min

internal data class NodeWithFloats<T>(val originalNode: T, val floatTuple: FloatTuple) : Comparable<NodeWithFloats<T>> {
    constructor(originalNode: T) : this(originalNode, FloatTuple.ZERO)

    override fun compareTo(other: NodeWithFloats<T>): Int = floatTuple.compareTo(other.floatTuple)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as NodeWithFloats<*>

        if (originalNode != other.originalNode) return false

        return true
    }

    override fun hashCode(): Int = originalNode.hashCode()
}

internal data class FloatTuple(val v1: Float, val v2: Float): Comparable<FloatTuple> {
    override fun compareTo(other: FloatTuple): Int {
        v1.compareTo(other.v1).let { if (it != 0) return it }
        return v2.compareTo(other.v2)
    }
    companion object {
        val ZERO = FloatTuple(0F, 0F)
    }
}

class LPAStarAlgorithm<T>(
    private val graph: Graph<T>,
    private var start: T,
    private val goal: T
) {
    private val queue = constructPriorityQueue<NodeWithFloats<T>>()
    private val gMap = hashMapOf<T, Float>()
    private val rhsMap = hashMapOf<T, Float>()

    //region Helper extensions
    private inline var T.g: Float
        get() = gMap[this] ?: Float.POSITIVE_INFINITY
        set(value) {
            gMap[this] = value
        }
    private inline var T.rhs: Float
        get() = rhsMap[this] ?: Float.POSITIVE_INFINITY
        set(value) {
            rhsMap[this] = value
        }

    private val T.successors: Collection<T> get() = graph.successorsOf(this)
    private val T.predecessors: Collection<T> get() = graph.predecessorsOf(this)
    //endregion

    init {
        start.rhs = 0F
        queue.enqueue(NodeWithFloats(start, calculateKey(start)))
    }

    private fun calculateKey(node: T): FloatTuple =
        FloatTuple(min(node.g, node.rhs) + graph.approximateDistance(node, goal), min(node.g, node.rhs))

    fun computeShortestPath() {
        while (true) {
            val topKey = queue.peek() ?: break
            if (topKey.floatTuple < calculateKey(goal) || goal.rhs != goal.g) {
                val node = queue.dequeue().originalNode
                if (node.g > node.rhs) {
                    node.g = node.rhs
                } else {
                    node.g = Float.POSITIVE_INFINITY
                    updateNode(node)
                }
                node.successors.forEach { updateNode(it) }
            } else {
                break
            }
        }
    }

    fun findPath(): Path<T>? {
        val nodes = arrayListOf<T>()
        if (goal.g == Float.POSITIVE_INFINITY) {
            return null
        }
        nodes.add(goal)
        var node: T = goal
        var distance = 0F
        while (node != start) {
            val predecessor = node.predecessors.minByOrNull { it.g + graph.exactDistance(it, node) }
            distance += graph.exactDistance(predecessor!!, node)
            nodes.add(predecessor)
            node = predecessor
        }
        nodes.reverse()
        return Path(nodes, distance)
    }

    // Call whenever the cost of the edge(s) pointing into [node] are updated.
    fun updateNode(node: T) {
        if (node != start) {
            node.rhs = node.predecessors.minOfOrNull { predecessor ->
                predecessor.g + graph.exactDistance(predecessor, node)
            } ?: Float.POSITIVE_INFINITY
            queue.removeIfPresent(NodeWithFloats(node))
            if (node.g != node.rhs) {
                queue.enqueue(NodeWithFloats(node, calculateKey(node)))
            }
        }
    }

    // It's unclear whether this is correct or even possible -- it's not in the wiki.
    // But it does pass tests.
    fun updateStart(node: T) {
        val oldStart = start
        start = node
        updateNode(oldStart)
    }
}