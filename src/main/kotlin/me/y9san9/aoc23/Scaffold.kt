package me.y9san9.aoc23

import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

// NOTE: Each Day* file contains FULL solution
// this file is only used to copy-paste util functions
// from day to day

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/dayTODO/partTODO/Input.txt"
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
