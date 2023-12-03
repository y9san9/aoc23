package me.y9san9.aoc23.day3.part2

import java.io.File

private data class EngineNumber(
    val int: Int,
    val region: RectRegion
)

private data class Gear(
    val coordinates: Coordinates
)

private fun main() {
    val lines = lines()
    val region = lines.region()

    val numbers = region.coordinates.mapNotNull { (x, y) ->
        val line = lines[y]
        val char = line[x]

        if (!char.isDigit()) return@mapNotNull null
        val isFirstDigit = x == 0 || !line[x - 1].isDigit()
        if (!isFirstDigit) return@mapNotNull null

        val int = line.substring(x).takeWhile(Char::isDigit)
        val numberRegion = RectRegion(
            width = int.length, height = 1,
            topLeft = Coordinates(x, y)
        )
        EngineNumber(int.toInt(), numberRegion)
    }

    val gears = region.coordinates.mapNotNull { (x, y) ->
        if (lines[y][x] != '*') return@mapNotNull null
        Gear(coordinates = Coordinates(x, y))
    }

    val sum = gears.sumOf { gear ->
        val gearNumbers = numbers
            .filter { (_, region) -> gear.coordinates in region.neighbors() }
            .takeIf { list -> list.size == 2 } ?: return@sumOf 0L
        gearNumbers.fold(initial = 1) { acc, (int) -> acc * int }
    }

    println(sum)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day3/part2/Input.txt"
)

// Coordinates Helpers

private data class Coordinates(
    val x: Int,
    val y: Int
) {
    operator fun plus(other: Coordinates): Coordinates {
        return Coordinates(
            x = x + other.x,
            y = y + other.y
        )
    }

    operator fun minus(other: Coordinates): Coordinates {
        return Coordinates(
            x = x - other.x,
            y = y - other.y
        )
    }

    fun move(x: Int = 0, y: Int = 0) = Coordinates(
        x = this.x + x,
        y = this.y + y
    )
}

private data class RectRegion(
    val width: Int,
    val height: Int,
    val topLeft: Coordinates
) {
    init {
        require(width > 0)
        require(height > 0)
    }

    val leftX: Int get() = topLeft.x
    val rightX: Int get() = topLeft.x + width - 1
    val topY: Int get() = topLeft.y
    val bottomY: Int get() = topLeft.y + height - 1

    val topRight: Coordinates get() = Coordinates(rightX, topY)
    val bottomLeft: Coordinates get() = Coordinates(leftX, bottomY)

    val leftLine: RectRegion get() = RectRegion(
        width = 1,
        height = height,
        topLeft = topLeft
    )
    val topLine: RectRegion get() = RectRegion(
        width = width,
        height = 1,
        topLeft = topLeft
    )
    val rightLine: RectRegion get() = RectRegion(
        width = 1,
        height = height,
        topLeft = topRight
    )
    val bottomLine: RectRegion get() = RectRegion(
        width = width,
        height = 1,
        topLeft = bottomLeft
    )

    val raw: Region get() {
        val coordinates = (leftX..rightX).flatMap { x ->
            (topY..bottomY).map { y ->
                Coordinates(x, y)
            }
        }
        return Region(coordinates.toSet())
    }

    operator fun contains(coordinates: Coordinates): Boolean {
        return coordinates.x in (coordinates.x..<coordinates.x + width) &&
                coordinates.y in (coordinates.y..coordinates.y + height)
    }

    fun expand(x: Int = 1, y: Int = 1): RectRegion {
        return RectRegion(
            width = width + x * 2,
            height = height + y * 2,
            topLeft = topLeft.move(
                x = -x,
                y = -y
            )
        )
    }

    fun border(): Region {
        return leftLine.raw + topLine.raw + rightLine.raw + bottomLine.raw
    }

    fun neighbors(): Region {
        return expand().border()
    }
}

private data class Region(val coordinates: Set<Coordinates>) {

    operator fun contains(coordinates: Coordinates): Boolean {
        return coordinates in this.coordinates
    }

    operator fun plus(other: Region): Region {
        return Region(coordinates = coordinates + other.coordinates)
    }

    operator fun minus(other: Region): Region {
        return Region(coordinates = coordinates - other.coordinates)
    }
}

@JvmName("regionString")
private fun List<String>.region(): Region = map { string -> string.toList() }.region()

private fun List<List<*>>.region(): Region {
    val columns = this
    val coordinates = columns.indices.flatMap { y ->
        val rows = columns[y]
        rows.indices.map { x ->
            Coordinates(x, y)
        }
    }
    return Region(coordinates.toSet())
}
