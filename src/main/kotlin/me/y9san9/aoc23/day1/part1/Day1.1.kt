package me.y9san9.aoc23.day1.part1

import java.io.File

fun main() {
    val lines = lines()
    val sum = lines.sumOf { line ->
        val first = line.firstNotNullOf(Char::digitToIntOrNull)
        val second = line.lastNotNullOf(Char::digitToIntOrNull)
        first * 10 + second
    }
    println(sum)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day1/part1/Input.txt"
)

private inline fun <T> String.lastNotNullOf(block: (Char) -> T?): T {
    return reversed().firstNotNullOf(block)
}
