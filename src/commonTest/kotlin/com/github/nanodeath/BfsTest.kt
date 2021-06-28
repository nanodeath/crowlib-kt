package com.github.nanodeath

import kotlin.test.Test
import kotlin.test.assertEquals

class BfsTest  {
    @Test
    fun straightShot() {
        val grid = Grid(3, 2)
        val algorithm = BFSAlgorithm(grid)
        val path = algorithm.findPath(grid.requireAt(0, 1), grid.requireAt(2, 1))
        assertEquals(Path(listOf(
            grid.requireAt(0, 1),
            grid.requireAt(1, 1),
            grid.requireAt(2, 1),
        ), 2F), path)
    }

    @Test
    fun aroundObstacle() {
        val grid = Grid(3, 2)
        grid.setAt(1, 1, null)
        val algorithm = BFSAlgorithm(grid)
        val path = algorithm.findPath(grid.requireAt(0, 1), grid.requireAt(2, 1))
        assertEquals(Path(listOf(
            grid.requireAt(0, 1),
            grid.requireAt(0, 0),
            grid.requireAt(1, 0),
            grid.requireAt(2, 0),
            grid.requireAt(2, 1),
        ), 4F), path)
    }
}