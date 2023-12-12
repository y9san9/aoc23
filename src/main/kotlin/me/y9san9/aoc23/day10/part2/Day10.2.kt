@file:JvmName("DAY10")

package me.y9san9.aoc23.day10.part2

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.grid.*
import me.y9san9.geometry.region.map
import me.y9san9.geometry.region.simple.SimpleRegion
import me.y9san9.geometry.region.simple.toRegion
import me.y9san9.geometry.region.toGrid
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
    val startEquivalent = Point.Pipe.TopLeft

    tailrec fun follow(
        coordinate: Coordinate,
        pipe: Point.Pipe,
        previousDirection: Coordinate = Coordinate(0, 0),
        acc: List<Coordinate> = listOf(coordinate)
    ): SimpleRegion {
        val direction = pipe.directions.first { direction -> direction != -previousDirection }
        val nextCoordinate = coordinate + direction
        val nextPoint = grid[nextCoordinate]
        if (nextPoint is Point.Start) return (acc + nextCoordinate).toRegion()
        return follow(nextCoordinate, nextPoint as Point.Pipe, direction, acc = acc + nextCoordinate)
    }
    val circuit = follow(
        coordinate = startCoordinate,
        pipe = startEquivalent
    ).map { coordinate, _ ->
        val point = grid[coordinate]
        if (point is Point.Start) startEquivalent else point
    }.toGrid { Point.Ground }
    tailrec fun rayCasting(
        y: Int = circuit.minY,
        acc: Int = 0
    ): Int {
        if (y > circuit.maxY) return acc
        tailrec fun traceRow(
            x: Int = circuit.minX,
            prevIsInside: Boolean = false,
            prevContinuousLine: List<Point.Pipe> = emptyList(),
            acc: Int = 0
        ): Int {
            if (x > circuit.maxX) return acc
            val point = circuit[x, y]
            if (point is Point.Pipe) {
                val continuousLine = prevContinuousLine + point
                val isTerminated = point.directions.none { (x) -> x == 1 }
                val flip = isTerminated &&
                        continuousLine.first().directions.sumOf(Coordinate::y) +
                        continuousLine.last().directions.sumOf(Coordinate::y) == 0
                return traceRow(
                    x = x + 1,
                    prevContinuousLine = if (!isTerminated) continuousLine else emptyList(),
                    prevIsInside = if (flip) !prevIsInside else prevIsInside,
                    acc = acc
                )
            }
            return traceRow(
                x = x + 1,
                prevIsInside = prevIsInside,
                prevContinuousLine = emptyList(),
                acc = if (prevIsInside) acc + 1 else acc
            )
        }
        return rayCasting(
            y = y + 1,
            acc = acc + traceRow()
        )
    }

    println(rayCasting())
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day10/part2/Input.txt"
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
