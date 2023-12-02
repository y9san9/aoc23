package me.y9san9.aoc23.day2.part2

import java.io.File

fun main() {
    val lines = lines()

    val sum = lines.map { line -> line.substringAfter(":").trim() }.sumOf { line ->
        val games = line.split(";")

        val red = games.maxOfOrNull { game ->
            game.split(",").maxOfOrNull { cube ->
                if (cube.endsWith("red")) {
                    cube.removeSuffix("red").trim().toInt()
                } else {
                    0
                }
            } ?: 0
        } ?: 0

        val green = games.maxOfOrNull { game ->
            game.split(",").maxOfOrNull { cube ->
                if (cube.endsWith("green")) {
                    cube.removeSuffix("green").trim().toInt()
                } else {
                    0
                }
            } ?: 0
        } ?: 0

        val blue = games.maxOfOrNull { game ->
            game.split(",").maxOfOrNull { cube ->
                if (cube.endsWith("blue")) {
                    cube.removeSuffix("blue").trim().toInt()
                } else {
                    0
                }
            } ?: 0
        } ?: 0

        (red * green * blue).also { println(it) }
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
