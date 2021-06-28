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

class LPAStarAlgorithm<T : Node>(
    private val graph: Graph<T>,
    private val start: T,
    private val goal: T,
    private val opts: AlgorithmOpts<T>
) {
    private val queue = constructPriorityQueue<NodeWithFloats<T>>()
    private val gMap = hashMapOf<T, Float>()
    private val rhsMap = hashMapOf<T, Float>()
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

    init {
        start.rhs = 0F
        queue.enqueue(NodeWithFloats(start, calculateKey(start)))
    }

    private fun calculateKey(node: T): FloatTuple =
        FloatTuple(min(node.g, node.rhs) + opts.distanceCalculator.approximateDistance(node, goal), min(node.g, node.rhs))

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
            val predecessor = graph.neighborsOf(node).minByOrNull { it.g + opts.distanceCalculator.exactDistance(it, node) }
            distance += opts.distanceCalculator.exactDistance(predecessor!!, node)
            nodes.add(predecessor)
            node = predecessor
        }
        nodes.reverse()
        return Path(nodes, distance)
    }

    // Call whenever the cost of the edge(s) pointing into [node] are updated.
    fun updateNode(node: T) {
        if (node != start) {
            node.rhs = Float.POSITIVE_INFINITY
            for (predecessor in node.predecessors) {
                node.rhs = min(node.rhs, predecessor.g + opts.distanceCalculator.exactDistance(predecessor, node))
            }
            queue.removeIfPresent(NodeWithFloats(node))
            if (node.g != node.rhs) {
                queue.enqueue(NodeWithFloats(node, calculateKey(node)))
            }
        }
    }

    private val T.successors: Collection<T> get() {
        // In theory, this should only be nodes connected by an "outbound" edge of a node. But isn't that all neighbors?
        // I suppose you could get into a trap that you can't get out of, which means an edge is one-directional.
        // But even that can be captured in the Graph itself -- even if Node_B is a neighbor of Node_A, doesn't mandate
        // that Node_A is a neighbor of Node_B -- graphs aren't necessarily commutative in their edges.
        return graph.neighborsOf(this)
    }

    private val T.predecessors: Collection<T> get() {
        // Uh, this should really not be the same as successors, but it's fine for now FIXME
        return graph.neighborsOf(this)
    }
}