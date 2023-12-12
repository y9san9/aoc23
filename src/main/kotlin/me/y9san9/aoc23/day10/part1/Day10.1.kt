package me.y9san9.aoc23.day10.part1

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.grid.indexOf
import me.y9san9.geometry.grid.map
import me.y9san9.geometry.grid.neighbors
import me.y9san9.geometry.grid.toGrid
import me.y9san9.geometry.region.filterIsInstance
import me.y9san9.geometry.region.first
import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

private sealed interface Point {
    sealed interface Pipe : Point {
        data object Vertical : Pipe
        data object Horizontal : Pipe
        data object TopLeft : Pipe
        data object TopRight : Pipe
        data object BottomLeft : Pipe
        data object BottomRight : Pipe
    }
    data object Ground : Point
    data object Start : Point
}

private val Point.Pipe.directions: List<Coordinate> get() = when (this) {
    Point.Pipe.Vertical -> listOf(
        Coordinate(0, 1),
        Coordinate(0, -1)
    )
    Point.Pipe.Horizontal -> listOf(
        Coordinate(1, 0),
        Coordinate(-1, 0)
    )
    Point.Pipe.TopLeft -> listOf(
        Coordinate(0, -1),
        Coordinate(-1, 0)
    )
    Point.Pipe.TopRight -> listOf(
        Coordinate(0, -1),
        Coordinate(1, 0)
    )
    Point.Pipe.BottomLeft -> listOf(
        Coordinate(0, 1),
        Coordinate(-1, 0)
    )
    Point.Pipe.BottomRight -> listOf(
        Coordinate(0, 1),
        Coordinate(1, 0)
    )
}

// '|' -> {} is a vertical pipe connecting north and south.
// '-' -> {} is a horizontal pipe connecting east and west.
// 'L' -> {} is a 90-degree bend connecting north and east.
// 'J' -> {} is a 90-degree bend connecting north and west.
// '7' -> {} is a 90-degree bend connecting south and west.
// 'F' -> {} is a 90-degree bend connecting south and east.
// '.' -> {} is ground; there is no pipe in this tile.
// 'S' -> {}

fun main() {
    val grid = lines().toGrid().map { _, char ->
        when (char) {
            '|' -> Point.Pipe.Vertical
            '-' -> Point.Pipe.Horizontal
            'L' -> Point.Pipe.TopRight
            'J' -> Point.Pipe.TopLeft
            '7' -> Point.Pipe.BottomLeft
            'F' -> Point.Pipe.BottomRight
            '.' -> Point.Ground
            'S' -> Point.Start
            else -> stub()
        }
    }

    val startCoordinate = grid.indexOf(Point.Start)

    val (firstRouteCoordinate, firstRoutePipe) = grid
        .neighbors(startCoordinate)
        .filterIsInstance<Point.Pipe>()
        .first { coordinate, pipe ->
            pipe.directions.any { direction ->
                coordinate + direction == startCoordinate
            }
        }

    tailrec fun follow(
        coordinate: Coordinate,
        pipe: Point.Pipe,
        previousDirection: Coordinate = Coordinate(0, 0),
        acc: Int = 1
    ): Int {
        val direction = pipe.directions.first { direction -> direction != -previousDirection }
        val nextCoordinate = coordinate + direction
        val nextPoint = grid[nextCoordinate]
        if (nextPoint is Point.Start) return acc + 1
        return follow(nextCoordinate, nextPoint as Point.Pipe, direction, acc = acc + 1)
    }

    val pathLength = follow(firstRouteCoordinate, firstRoutePipe)
    println(pathLength / 2)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day10/part1/Input.txt"
)

private inline fun <T> List<T>.indexOfFirstOrNull(
    block: (T) -> Boolean
): Int? = indexOfFirst(block).takeIf { it != -1 }

private inline fun <T, R> Iterable<T>.lastNotNullOf(block: (T) -> R?): R {
    return reversed().firstNotNullOf(block)
}

private inline fun <T, R> List<T>.lastNotNullOf(block: (T) -> R?): R {
    return asReversed().firstNotNullOf(block)
}

private inline fun <T> Sequence<T>.takeWhileInclusive(
    crossinline block: (T) -> Boolean
): Sequence<T> = sequence {
    for (element in this@takeWhileInclusive) {
        yield(element)
        if (!block(element)) break
    }
}

private fun stub(): Nothing = error("stub!")

private fun zeroIntTriple() = Triple(0, 0, 0)

private infix fun Int.smallPow(other: Int): Int {
    return this.toFloat().pow(other).roundToInt()
}

private fun smallSqrt(int: Int): Int {
    return sqrt(int.toFloat()).roundToInt()
}

private fun solveQuadratic(a: Int, b: Int, c: Int): Pair<Int, Int> {
    val first = (-b + smallSqrt(b * b + 4 * a * c)) / 2
    val second = (-b + smallSqrt(b * b - 4 * a * c)) / 2
    return first to second
}

private fun smallSqrt(long: Long): Long {
    return sqrt(long.toFloat()).roundToLong()
}

private fun solveQuadratic(a: Long, b: Long, c: Long): Pair<Long, Long> {
    val first = (-b - smallSqrt(b * b - 4 * a * c)) / 2
    val second = (-b + smallSqrt(b * b - 4 * a * c)) / 2
    return first to second
}

private fun gcd(a: Int, b: Int): Int {
    tailrec fun loop(
        a: Int,
        b: Int
    ): Int {
        if (b <= 0) return a
        return loop(
            a = b,
            b = a % b
        )
    }
    return loop(a, b)
}

private fun lcm(a: Int, b: Int): Int {
    return a * (b / gcd(a, b))
}


private fun gcd(a: Long, b: Long): Long {
    tailrec fun loop(
        a: Long,
        b: Long
    ): Long {
        if (b <= 0) return a
        return loop(
            a = b,
            b = a % b
        )
    }
    return loop(a, b)
}

private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}
