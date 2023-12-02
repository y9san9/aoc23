package me.y9san9.aoc23.day2.part1

import java.io.File

fun main() {
    val lines = lines()
    val redLimit = 12
    val greenLimit = 13
    val blueLimit = 14

    val sum = lines.mapIndexed { i, line ->
        (i + 1) to line.substringAfter(":").trim()
    }.filter { (_, line) ->
        line.split(";").all { game ->
            val cubes = game.split(",")
            cubes.all { cube ->
                when {
                    cube.endsWith("red") -> cube.removeSuffix(" red").trim().toInt() <= redLimit
                    cube.endsWith("green") -> cube.removeSuffix(" green").trim().toInt() <= greenLimit
                    cube.endsWith("blue") -> cube.removeSuffix(" blue").trim().toInt() <= blueLimit
                    else -> error("Stub!")
                }
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
