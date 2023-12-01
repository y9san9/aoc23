package me.y9san9.aoc23.day1.part1

import me.y9san9.aoc23.day1.part2.digits
import java.io.File

fun main() {
    lines()
        .map(::digits)
        .sumOf(::extractInt)
        .let(::println)
}

fun digits(string: String): List<Int> =
    string.mapNotNull { char -> char.digitToIntOrNull() }

fun extractInt(digits: List<Int>): Int =
    digits.first() * 10 + digits.last()

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day1/part1/Input.txt"
)
