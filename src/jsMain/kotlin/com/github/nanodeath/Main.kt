package com.github.nanodeath

import com.github.nanodeath.ui.Observable
import kotlinx.browser.document
import kotlinx.dom.appendElement
import org.w3c.dom.*
import kotlin.random.Random

fun main() {
    console.log("Hello!")
    val input = (1..10).map { Thing("thing$it", it) }.shuffled()
    val myQueue = JsPriorityQueue<Thing>()
    input.forEach { myQueue.enqueue(it) }
    while (!myQueue.isEmpty()) {
        println("Got: ${myQueue.dequeue()}")
    }
    val grid = document.getElementById("grid")!!
    grid.setAttribute("style", "width: 1024px")
    val width = 64
    val height = 64
    val widthPercentage = (100.0F / width).toString()
    repeat(height) { y ->
        val row = document.createElement("tr")
        repeat(width) { x ->
            row.appendElement("td") {
                setAttribute("style", "width: $widthPercentage%;")
                appendElement("div") {}
            }
        }
        grid.appendChild(row)
    }

    val start = randomPoint(width, height)
    val goal = generateSequence { randomPoint(width, height) }.first { it != start }
    val forbidden = setOf(start, goal)
    console.asDynamic().time("obstacles")
    val obstacles = generateSequence { randomPoint(width, height) }.filter { it !in forbidden }.distinct()
        .take((0.4F * (width * height)).toInt()).toList()
    console.asDynamic().timeEnd("obstacles")
    println("Generated ${obstacles.size} obstacles")

    println("Plotting path between $start and $goal")
    val pathGraph = Grid(width, height)
    obstacles.forEach { pathGraph.setAt(it.x, it.y, null) }

    val g = DomGrid(grid as HTMLTableElement)
    console.asDynamic().time("coloring board")
    obstacles.forEach { g.color(it.x, it.y, "black") }
    start.let { g.color(it.x, it.y, "green") }
    goal.let { g.color(it.x, it.y, "red") }
    console.asDynamic().timeEnd("coloring board")
    console.asDynamic().time("running algorithm")
    val path = AStarAlgorithm(pathGraph).findPath(Grid.SimpleNode(start.x, start.y), Grid.SimpleNode(goal.x, goal.y))
    console.asDynamic().timeEnd("running algorithm")
    if (path != null) {
        for (node in path.nodes.drop(1).dropLast(1)) {
            g.color(node.x, node.y, "lightgray")
        }
    } else {
        console.log("No path!")
    }

    val algorithm = (document.getElementById("algorithmSelect") as HTMLSelectElement).observable()
    algorithm.subscribe { newAlgorithm ->
        displayAlgorithm(newAlgorithm)
    }
    algorithm.notify()

    val widthForm = (document.getElementById("width") as HTMLInputElement).intObservable()
    val heightForm = (document.getElementById("height") as HTMLInputElement).intObservable()
    widthForm.subscribe { heightForm.value = it; println("Updating heightform") }

    (document.getElementById("generate") as HTMLButtonElement).addEventListener("click", { ev ->
        ev.preventDefault()
        (ev.target as HTMLElement?)?.classList?.add("is-loading")
        println("Generating!")
    })
}

fun displayAlgorithm(newAlgorithm: String) {
    document.querySelectorAll("article.algo").asSequence()
        .filterIsInstance<HTMLElement>()
        .forEach { node ->
            node.style.display = if (node.classList.contains(newAlgorithm)) "block" else "none"
        }
}

fun NodeList.asSequence(): Sequence<Node?> {
    return sequence {
        for (i in 0 until length) {
            yield(item(i) ?: error("No node at $i"))
        }
    }
}

fun HTMLInputElement.intObservable(): Observable<Int> {
    val initialValue = value.toInt()
    val observable = Observable(initialValue)
    addEventListener("input", { observable.value = value.toInt() })
    observable.subscribe {
        println("observable.subscribe")
        value = it.toString()
    }
    return observable
}

fun HTMLSelectElement.observable(): Observable<String> {
    val initialValue = value
    val observable = Observable(initialValue)
    addEventListener("input", { observable.value = value })
    observable.subscribe {
        value = it
    }
    return observable
}

private fun randomPoint(xMax: Int, yMax: Int) = Point(Random.nextInt(xMax), Random.nextInt(yMax))

data class Point(val x: Int, val y: Int)

class DomGrid(private val node: HTMLTableElement) {
    fun color(x: Int, y: Int, c: String) {
//        val row = node.querySelectorAll("* > tr")[y] as HTMLTableRowElement
//        val cell = row.querySelectorAll("* > td")[x] as HTMLTableCellElement
        val row = node.children[y] as? HTMLTableRowElement ?: error("Not a row element? ${node.childNodes[y]}")
        val cell = row.children[x] as? HTMLTableCellElement ?: error("Not a cell element? ${row.childNodes[x]}")
        val div = cell.children[0] as HTMLElement
        div.style.background = c
//        console.log("Updating cell %o to $c", cell)
    }
}

data class Thing(val name: String, val priority: Int) : Comparable<Thing> {
    override fun compareTo(other: Thing): Int {
        return priority.compareTo(other.priority)
    }
}