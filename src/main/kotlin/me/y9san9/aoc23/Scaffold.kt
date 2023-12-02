package me.y9san9.aoc23

import java.io.File

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

private fun String.day2Parser(
    split: List<String> = listOf(":", ",")
): List<List<String>> = split(
    delimiters = split.toTypedArray()
).map { string -> string.split(" ") }

private fun stub(): Nothing = error("stub!")

private val Triple<Int, Int, Int>.Zero get() = Triple(0, 0, 0)

private fun zeroIntTriple() = Triple(0, 0, 0)
