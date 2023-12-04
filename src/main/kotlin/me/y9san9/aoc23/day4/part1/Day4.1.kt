package me.y9san9.aoc23.day4.part1

import java.io.File

data class Card(
    val winningNumbers: List<Int>,
    val numbers: List<Int>
)

private fun main() {
    val cards = lines()
        .map { line -> line.substringAfter(": ") }
        .map { line ->
            line.split(" | ").map { string -> string.split(" ").mapNotNull(String::toIntOrNull) }
        }.map { (winningNumbers, numbers) -> Card(winningNumbers, numbers) }

    val points = cards.sumOf { (winningNumbers, numbers) ->
        val power = numbers.count { number -> number in winningNumbers } - 1
        power.takeIf { it >= 0 } ?: return@sumOf 0
        2 smallPow power
    }

    println(points)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day4/part1/Input.txt"
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

