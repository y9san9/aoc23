package me.y9san9.aoc23.day1.part2

import java.io.File

fun main() {
    val lines = lines()
    val sum = lines.sumOf { line ->
        val first = line.indices.firstNotNullOf { index ->
            line.extractDigitOrNull(index)
        }
        val last = line.indices.lastNotNullOf { index ->
            line.extractDigitOrNull(index)
        }
        first * 10 + last
    }
    println(sum)
}

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

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day1/part2/Input.txt"
)

private inline fun <T> List<T>.indexOfFirstOrNull(
    block: (T) -> Boolean
): Int? = indexOfFirst(block).takeIf { it != -1 }

private inline fun <T, R> Iterable<T>.lastNotNullOf(block: (T) -> R?): R {
    return reversed().firstNotNullOf(block)
}
