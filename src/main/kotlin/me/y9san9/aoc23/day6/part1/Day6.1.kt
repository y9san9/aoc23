package me.y9san9.aoc23.day6.part1

import java.io.File
import kotlin.math.roundToInt
import kotlin.math.sqrt

// distance = (time - speedingTime) * speedingTime
// distance > record

// x = speedingTime
// y = record
// (time - x) * x > y
// - x^2 + time * x - y > 0

private fun main() {
    val (times, records) = lines().take(2).map { line ->
        line.drop(n = 11).split(" ").mapNotNull(String::toIntOrNull)
    }

    val prod = times.zip(records) { time, record ->
        val (min, max) = solveQuadratic(-1, time, -record)
        max - min + 1
    }.fold(initial = 1, Int::times)

    println(prod)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day6/part1/Input.txt"
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

private fun stub(): Nothing = error("stub!")

private fun zeroIntTriple() = Triple(0, 0, 0)

private infix fun Int.smallPow(other: Int): Int {
    val base = this

    tailrec fun body(times: Int = other, acc: Int = 1): Int = when {
        times == 0 -> acc
        else -> body(times = times - 1, acc = acc * base)
    }

    return body()
}

private fun smallSqrt(int: Int): Int {
    return sqrt(int.toFloat()).roundToInt()
}

private fun solveQuadratic(a: Int, b: Int, c: Int): Pair<Int, Int> {
    val first = (-b + smallSqrt(b * b + 4 * a * c)) / 2
    val second = (-b + smallSqrt(b * b - 4 * a * c)) / 2
    return first to second
}
