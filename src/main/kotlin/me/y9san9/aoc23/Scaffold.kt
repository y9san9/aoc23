package me.y9san9.aoc23

import java.io.File
import java.lang.Integer.min
import kotlin.math.max

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
