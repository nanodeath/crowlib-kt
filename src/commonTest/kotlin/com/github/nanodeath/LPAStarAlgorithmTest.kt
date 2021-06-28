package com.github.nanodeath

import kotlin.test.Test
import kotlin.test.assertEquals

class LPAStarAlgorithmTest {
    @Test
    fun straightShot() {
        val grid = Grid(3, 2)
        val algorithm = LPAStarAlgorithm(grid, grid.requireAt(0, 1), grid.requireAt(2, 1), AlgorithmOpts(SimpleNode.distanceCalculator()))
        algorithm.computeShortestPath()
        val path = algorithm.findPath()
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
        val algorithm = LPAStarAlgorithm(grid, grid.requireAt(0, 1), grid.requireAt(2, 1), AlgorithmOpts(SimpleNode.distanceCalculator()))
        algorithm.computeShortestPath()
        val path = algorithm.findPath()
        assertEquals(Path(listOf(
            grid.requireAt(0, 1),
            grid.requireAt(0, 0),
            grid.requireAt(1, 0),
            grid.requireAt(2, 0),
            grid.requireAt(2, 1),
        ), 4F), path)
    }

    @Test
    fun updatedObstacle() {
        val grid = CostGrid(3, 2)
        val algorithm = LPAStarAlgorithm(grid, grid.requireAt(0, 1), grid.requireAt(2, 1), AlgorithmOpts(CostNode.distanceCalculator()))
        algorithm.computeShortestPath()
        val path = algorithm.findPath()
        assertEquals(Path(listOf(
            grid.requireAt(0, 1),
            grid.requireAt(1, 1),
            grid.requireAt(2, 1),
        ), 2F), path)
        grid.setAt(1, 1, CostNode(1, 1, 10F))
        algorithm.updateNode(grid.requireAt(1, 1))
        algorithm.computeShortestPath()

        val newPath = algorithm.findPath()
        assertEquals(Path(listOf(
            grid.requireAt(0, 1),
            grid.requireAt(0, 0),
            grid.requireAt(1, 0),
            grid.requireAt(2, 0),
            grid.requireAt(2, 1),
        ), 4F), newPath)
    }

    @Test
    fun updatedStart() {
        val grid = CostGrid(3, 3)
        val algorithm = LPAStarAlgorithm(grid, grid.requireAt(0, 0), grid.requireAt(2, 2), AlgorithmOpts(CostNode.distanceCalculator()))
        algorithm.computeShortestPath()
        val path = algorithm.findPath()
        assertEquals(Path(listOf(
            grid.requireAt(0, 0),
            grid.requireAt(0, 1),
            grid.requireAt(0, 2),
            grid.requireAt(1, 2),
            grid.requireAt(2, 2),
        ), 4F), path)

        algorithm.updateStart(grid.requireAt(2, 0))
        algorithm.computeShortestPath()
        val newPath = algorithm.findPath()
        assertEquals(Path(listOf(
            grid.requireAt(2, 0),
            grid.requireAt(2, 1),
            grid.requireAt(2, 2),
        ), 2F), newPath)
    }
}