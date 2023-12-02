package me.y9san9.aoc23.day2.part1

import java.io.File

private const val redLimit = 12
private const val greenLimit = 13
private const val blueLimit = 14

fun main() {
    val lines = lines()

    val sum = lines
        .map { line ->
            line.substringAfter(":").trim()
                .split(": ", ", ")
                .map { cubes -> cubes.split(" ") }
                .map { (int, type) -> int.toInt() to type }
        }
        .mapIndexed { i, games -> (i + 1) to games }
        .filter { (_, games) ->
            games.all { (int, type) ->
                when (type) {
                    "red" -> int <= redLimit
                    "green" -> int <= greenLimit
                    "blue" -> int <= blueLimit
                    else -> stub()
                }
            }
        }.sumOf { (i) -> i }

    println(sum)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day2/part1/Input.txt"
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
