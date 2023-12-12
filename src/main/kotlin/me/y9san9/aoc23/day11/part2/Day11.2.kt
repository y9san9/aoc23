package me.y9san9.aoc23.day11.part2

import me.y9san9.geometry.grid.*
import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

private sealed interface Point {
    val distance: Int
    data object Galaxy : Point {
        override val distance = 1
    }
    data class Void(override val distance: Int) : Point
}

fun main() {
    val grid = lines().toGrid().map { _, char ->
        when (char) {
            '#' -> Point.Galaxy
            '.' -> Point.Void(distance = 1)
            else -> stub()
        }
    }

    val expand = { column: List<Point> ->
        if (column.filterIsInstance<Point.Galaxy>().isEmpty()) {
            column.map { Point.Void(distance = 1_000_000) }
        } else {
            column
        }
    }

    val expandedGrid = grid.columns()
        .map(expand).toGrid().columns()
        .map(expand).toGrid()

    val galaxies = expandedGrid.filterIsInstance<Point.Galaxy>().coordinates

    val sum = galaxies.sumOf { first ->
        galaxies.sumOf { second ->
            val width = (first.x..<second.x).sumOf { x ->
                expandedGrid[x, first.y].distance
            }
            val height = (first.y..<second.y).sumOf { y ->
                expandedGrid[second.x, y].distance
            }
            (height.coerceAtLeast(0) + width.coerceAtLeast(0)).toLong()
        }
    }

    println(sum)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day11/part2/Input.txt"
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
