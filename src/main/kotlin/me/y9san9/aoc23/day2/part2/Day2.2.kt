package me.y9san9.aoc23.day2.part2

import java.io.File
import kotlin.math.max

fun main() {
    val lines = lines()

    val sum = lines
        .map { line ->
            line.substringAfter(":").trim()
                .split("; ", ", ")
                .map { cubes -> cubes.split(" ") }
                .map { (int, type) -> int.toInt() to type }
        }.sumOf { game ->
            val (r, g, b) = game.fold(zeroIntTriple()) { (red, green, blue), (int, type) ->
                Triple(
                    if (type == "red") max(red, int) else red,
                    if (type == "green") max(green, int) else green,
                    if (type == "blue") max(blue, int) else blue
                )
            }

            r * g * b
        }

    println(sum)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day2/part2/Input.txt"
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

private fun zeroIntTriple() = Triple(0, 0, 0)
