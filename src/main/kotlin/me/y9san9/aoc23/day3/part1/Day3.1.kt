package me.y9san9.aoc23.day3.part1

import me.y9san9.aoc23.day3.part2.EngineNumber
import java.io.File

data class EngineNumber(
    val int: Int,
    val surroundings: List<Char>
)

fun main() {
    val lines = lines()

    val sum = lines.indices.flatMap { y ->
        val line = lines[y]
        line.indices.mapNotNull { x ->
            val char = line[x]
            if (!char.isDigit()) return@mapNotNull null
            if (x != 0 && line[x - 1].isDigit()) return@mapNotNull null
            val number = line.substring(x).takeWhile(Char::isDigit)
            val surroundings = (y - 1..y + 1).flatMap { newY ->
                (x - 1..x + number.length).mapNotNull { newX ->
                    if (newY in lines.indices && newX in line.indices) {
                        val newChar = lines[newY][newX]
                        if (newChar.isDigit()) null else newChar
                    } else null
                }
            }
            EngineNumber(number.toInt(), surroundings)
        }
    }.filter { number ->
        number.surroundings.any { char -> char != '.' }
    }.sumOf { number -> number.int }

    println(sum)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day3/part1/Input.txt"
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

private fun String.day2Parser(
    split: List<String> = listOf(":", ",")
): List<List<String>> = split(
    delimiters = split.toTypedArray()
).map { string -> string.split(" ") }

private fun stub(): Nothing = error("stub!")

private val Triple<Int, Int, Int>.Zero get() = Triple(0, 0, 0)

private fun zeroIntTriple() = Triple(0, 0, 0)
