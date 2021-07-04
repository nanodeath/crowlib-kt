package com.github.nanodeath

import kotlin.js.JsExport
import kotlin.math.min

/**
 * Fringe Search: https://en.wikipedia.org/wiki/Fringe_search
 * https://web.archive.org/web/20090219220415/http://www.cs.ualberta.ca/~games/pathfind/publications/cig2005.pdf
 */
@JsExport
class FRAStarAlgorithm<T>(private val graph: Graph<T>) {
    fun findPath(start: T, end: T): Path<T>? {
        val gMap = hashMapOf<T, Float>().apply {
            this[start] = 0F
        }
        fun T.g() = gMap.getOrElse(this) { Float.POSITIVE_INFINITY }
        fun T.h() = graph.approximateDistance(this, end)
        fun T.f() = g() + h()

        var currentIteration = linkedSetOf<T>().also { it.add(start) } // "now"
        var nextIteration = linkedSetOf<T>() // "later"
        val cache = mutableMapOf<T, CacheKey>()
        cache[start] = CacheKey(0F, null)
        var fLimit = start.h()
        var found = false

        while(!found && (currentIteration.isNotEmpty() || nextIteration.isNotEmpty())) {
            if (currentIteration.isEmpty() && nextIteration.isNotEmpty()) {
                currentIteration = nextIteration
                nextIteration = linkedSetOf()
            }
            var fMin = Float.POSITIVE_INFINITY
            val iterator = currentIteration.iterator()
            while (iterator.hasNext()) {
                val n = iterator.next()
                val (g, _) = cache[n]!!
                val f = n.f()
                if (f > fLimit) {
                    fMin = min(f, fMin)
                    continue
                }
                if (n == end) {
                    found = true
                    break
                }
                for (s in graph.successorsOf(n).asReversed()) {
                    val g_s = g + graph.exactDistance(n, s)
                    if (cache[s] != null) {
                        val (g_prime, _) = cache[s]!!
                        if (g_s >= g_prime) {
                            continue
                        }
                    }
                    check(s !in currentIteration)
                    nextIteration.add(s)
                    cache[s] = CacheKey(g_s, n)
                }
                iterator.remove()
            }
            fLimit = fMin
        }

        if (found) {
            return reconstructPath(
                end,
                cache.mapValues { entry -> entry.value.parent }
            )
        }
        return null
    }

    private fun reconstructPath(finalNode: T, ancestorMap: Map<T, T?>): Path<T> {
        val nodes = ArrayList<T>()
        var node: T = finalNode
        var distance = 0F
        while (true) {
            nodes.add(node)
            distance += ancestorMap[node]?.let { graph.exactDistance(node, it) } ?: 0F
            node = ancestorMap[node] ?: break
        }
        nodes.reverse()
        return Path(nodes, distance)
    }

    private inner class CacheKey(val g: Float, val parent: T?) {
        operator fun component1() = g
        operator fun component2() = parent
    }
}