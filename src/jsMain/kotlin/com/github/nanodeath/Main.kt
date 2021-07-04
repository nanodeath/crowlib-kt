package com.github.nanodeath

import com.github.nanodeath.ui.*
import com.github.nanodeath.ui.all
import com.github.nanodeath.ui.byId
import kotlinx.browser.document
import kotlinx.dom.appendElement
import kotlinx.dom.createElement
import org.w3c.dom.*
import kotlin.random.Random

class GridView(private val dom: HTMLTableElement) {
    var obstacles: List<Grid.SimpleNode> = emptyList()
        private set
    fun clear() {
        dom.removeChildNodes()
    }

    fun setDimensions(width: String) {
        dom.style.width = width
    }

    fun color(x: Int, y: Int, c: String) {
        val row = dom.children[y] as? HTMLTableRowElement ?: error("Not a row element? ${dom.childNodes[y]}")
        val cell = row.children[x] as? HTMLTableCellElement ?: error("Not a cell element? ${row.childNodes[x]}")
        val div = cell.children[0] as HTMLElement
        div.style.backgroundColor = c
    }

    fun populateTable(width: Int, height: Int) {
        val widthPercentage = (100.0F / width).toString()
        val cell = document.createElement("td") {
//            unsafeCast<HTMLElement>().style.width = "$widthPercentage%;"
            setAttribute("style", "width: $widthPercentage%;")
            appendElement("div") {}
        }
        repeat(height) { y ->
            dom.appendElement("tr") {
                repeat(width) { x ->
                    appendChild(cell.cloneNode(deep = true))
                }
            }
        }
    }

    fun generateObstacles(width: Int, height: Int) {
        obstacles = generateSequence { randomPoint(width, height) }.distinct()
            .take((0.4F * (width * height)).toInt()).toList()
        obstacles.forEach { color(it.x, it.y, "black") }
    }

    fun clearPath(allowedColors: Set<String>) {
        dom.querySelectorAll("* tr td div").asSequence().forEach {
            val htmlElement = it.unsafeCast<HTMLElement>()
            if (htmlElement.style.backgroundColor !in allowedColors) {
                htmlElement.style.backgroundColor = "white"
            }
        }
    }
}

fun main() {
    console.log("Hello!")
    val input = (1..10).map { Thing("thing$it", it) }.shuffled()
    val myQueue = JsPriorityQueue<Thing>()
    input.forEach { myQueue.enqueue(it) }
    while (!myQueue.isEmpty()) {
        println("Got: ${myQueue.dequeue()}")
    }
    val grid = GridView(document.byId<HTMLTableElement>("grid")).apply {
        setDimensions("1024px")
    }

    val algorithm = document.byId<HTMLSelectElement>("algorithmSelect").observable()
    algorithm.subscribe { newAlgorithm ->
        displayAlgorithm(newAlgorithm)
    }
    algorithm.notify()

    val displayWidthValue = document.byId<HTMLInputElement>("displayWidth").intObservable()
    val displayHeightValue = document.byId<HTMLInputElement>("displayHeight").intObservable()
    displayWidthValue.subscribe { displayHeightValue.value = it }
    displayWidthValue.subscribe {
        grid.setDimensions("${it}px")
    }
    val dimensionsX = document.byId<HTMLInputElement>("dimensionsX").intObservable()
    val dimensionsY = document.byId<HTMLInputElement>("dimensionsY").intObservable()

    var start: Grid.SimpleNode = Grid.SimpleNode(0, 0)
    var goal: Grid.SimpleNode = Grid.SimpleNode(0, 0)
    document.byId("generateGraph").addEventListener("click", {
        it.preventDefault()
        grid.clear()
        val width = dimensionsX.value
        val height = dimensionsY.value
        time("populateTable")
        grid.populateTable(width, height)
        grid.generateObstacles(width, height)
        timeEnd("populateTable")

        start = randomPoint(width, height)
        goal = generateSequence { randomPoint(width, height) }.first { it != start }

        start.let { grid.color(it.x, it.y, "lightgreen") }
        goal.let { grid.color(it.x, it.y, "red") }
    })

    document.byId("findPath").addEventListener("click", { ev ->
        ev.preventDefault()
        (ev.target as HTMLElement?)?.classList?.add("is-loading")
        println("Generating!")

        time("running algorithm")
        grid.clearPath(setOf("black", "white", "lightgreen", "red"))
        val pathGraph = Grid(dimensionsX.value, dimensionsY.value)
        grid.obstacles.forEach { pathGraph.setAt(it.x, it.y, null) }
        val path = when (algorithm.value) {
            "a_star" -> {
                AStarAlgorithm(pathGraph).findPath(Grid.SimpleNode(start.x, start.y), Grid.SimpleNode(goal.x, goal.y))
            }
            "fra_star" -> FRAStarAlgorithm(pathGraph).findPath(Grid.SimpleNode(start.x, start.y), Grid.SimpleNode(goal.x, goal.y))
            else -> error("Invalid algorithm value: ${algorithm.value}")
        }
        timeEnd("running algorithm")
        if (path != null) {
            for (node in path.nodes.drop(1).dropLast(1)) {
                grid.color(node.x, node.y, "lightgray")
            }
        } else {
            console.log("No path!")
            (ev.target as HTMLElement?)?.classList?.add("is-danger")
            (ev.target as HTMLElement?)?.textContent = "No path!"
        }
        (ev.target as HTMLElement?)?.classList?.remove("is-loading")
    })
}

fun displayAlgorithm(newAlgorithm: String) {
    document.all("article.algo")
        .forEach { node ->
            node.style.display = if (node.classList.contains(newAlgorithm)) "block" else "none"
        }
}

private fun randomPoint(xMax: Int, yMax: Int, r: Random = Random) = Grid.SimpleNode(r.nextInt(xMax), r.nextInt(yMax))

data class Thing(val name: String, val priority: Int) : Comparable<Thing> {
    override fun compareTo(other: Thing): Int {
        return priority.compareTo(other.priority)
    }
}