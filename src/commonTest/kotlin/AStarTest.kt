import com.github.nanodeath.*
import kotlin.test.*

class AStarTest  {
    @Test
    fun `straight shot`() {
        val grid = Grid(3, 2)
        val algorithm = AStarAlgorithm(grid)
        val path = algorithm.findPath(grid.requireAt(0, 1), grid.requireAt(2, 1), AlgorithmOpts(SimpleNode.distanceCalculator()))
        assertEquals(Path(listOf(
            grid.requireAt(0, 1),
            grid.requireAt(1, 1),
            grid.requireAt(2, 1),
        ), 2F), path)
    }

    @Test
    fun `around obstacle`() {
        val grid = Grid(3, 2)
        grid.setAt(1, 1, null)
        val algorithm = AStarAlgorithm(grid)
        val path = algorithm.findPath(grid.requireAt(0, 1), grid.requireAt(2, 1), AlgorithmOpts(SimpleNode.distanceCalculator()))
        assertEquals(Path(listOf(
            grid.requireAt(0, 1),
            grid.requireAt(0, 0),
            grid.requireAt(1, 0),
            grid.requireAt(2, 0),
            grid.requireAt(2, 1),
        ), 4F), path)
    }
}