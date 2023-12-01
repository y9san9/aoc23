package me.y9san9.aoc23.day1.part2

import java.io.File

fun main() = lines()
    .map(::digits)
    .sumOf(::extractInt)
    .let(::println)

fun digits(string: String) = string.indices
    .mapNotNull { index -> string.extractDigitOrNull(index) }

private val digits = listOf(
    "one", "two", "three",
    "four", "five", "six",
    "seven", "eight", "nine"
)

private fun String.extractDigitOrNull(index: Int): Int? {
    val simple = this[index].digitToIntOrNull()
    if (simple != null) return simple

    return digits.indexOfFirstOrNull { digit ->
        this.startsWith(digit, index)
    }?.plus(other = 1)
}

fun extractInt(digits: List<Int>): Int =
    digits.first() * 10 + digits.last()

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day1/part2/Input.txt"
)

private inline fun <T> List<T>.indexOfFirstOrNull(
    block: (T) -> Boolean
): Int? = indexOfFirst(block).takeIf { it != -1 }
