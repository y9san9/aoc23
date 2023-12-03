package me.y9san9.aoc23.day3.part2

import java.io.File
import kotlin.math.max
import kotlin.math.min

private fun main() {
    val lines = lines()

    fun findNumber(
        x: Int, y: Int,
        startX: Int
    ): Int? {
        val line = lines[y].takeIf { it[x].isDigit() } ?: return null
        if (x != startX && line[x - 1].isDigit()) return null
        tailrec fun findStart(x: Int): Int = when {
            x == 0 || !line[x - 1].isDigit() -> x
            else -> findStart(x = x - 1)
        }
        tailrec fun findEnd(x: Int): Int = when {
            x == line.lastIndex || !line[x + 1].isDigit() -> x
            else -> findEnd(x = x + 1)
        }
        return line.substring(findStart(x)..findEnd(x)).toInt()
    }

    val sum = lines.region().coordinates.filter { (x, y) -> lines[y][x] == '*' }
        .map { coordinates ->
            coordinates.neighbors().coordinates
                .mapNotNull { (x, y) -> findNumber(x, y, (coordinates.x - 1).coerceAtLeast(0)) }
        }.sumOf { list -> if (list.size == 2) list.fold(initial = 1, Int::times) else 0 }

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

    fun neighbors(): Region = RectRegion.fromCoordinate(coordinates = this).neighbors()
}

private data class Rect(
    val width: Int,
    val height: Int
) {
    init {
        require(width > 0)
        require(height > 0)
    }

    fun placeAt(coordinates: Coordinates): RectRegion {
        return RectRegion(width, height, coordinates)
    }
    fun placeAt(x: Int, y: Int): RectRegion {
        return placeAt(Coordinates(x, y))
    }
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
    val bottomRight: Coordinates get() = Coordinates(rightX, bottomY)

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

    val rect: Rect get() = Rect(width, height)

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

    infix fun intersectOrNull(other: RectRegion): RectRegion? {
        val topLeft = Coordinates(
            x = max(topLeft.x, other.topLeft.x),
            y = max(topLeft.y, other.topLeft.y)
        )
        val bottomRight = Coordinates(
            x = min(bottomRight.x, other.bottomRight.x),
            y = min(bottomRight.y, other.bottomRight.y)
        )
        val width = bottomRight.x - topLeft.x
        val height = bottomRight.y - topLeft.y
        if (width <= 0 || height <= 0) return null
        return RectRegion(width, height, topLeft)
    }

    companion object {
        fun fromCoordinate(coordinates: Coordinates): RectRegion =
            RectRegion(width = 1, height = 1, topLeft = coordinates)
        fun fromCoordinate(x: Int, y: Int): RectRegion =
            fromCoordinate(Coordinates(x, y))
    }
}

private data class Region(val coordinates: Set<Coordinates>) {
    val size: Int get() = coordinates.size
    fun isEmpty() = coordinates.isEmpty()
    fun isNotEmpty() = coordinates.isNotEmpty()

    inline fun filter(predicate: (Coordinates) -> Boolean): Region =
        Region(coordinates = coordinates.filter(predicate).toSet())

    operator fun contains(coordinates: Coordinates): Boolean {
        return coordinates in this.coordinates
    }

    operator fun plus(other: Region): Region {
        return Region(coordinates = coordinates + other.coordinates)
    }

    operator fun minus(other: Region): Region {
        return Region(coordinates = coordinates - other.coordinates)
    }

    infix fun intersect(other: Region): Region {
        return Region(coordinates = coordinates intersect other.coordinates)
    }

    fun toRectRegionOrNull(): RectRegion? {
        val sorted = coordinates.sortedWith(
            Comparator<Coordinates> { (x, _), (otherX, _) -> x.compareTo(otherX) }
                .thenComparator { (_, y), (_, otherY) -> y.compareTo(otherY) }
        )
        val topLeft = sorted.first()
        val bottomRight = sorted.last()
        val width = bottomRight.x - topLeft.x
        val height = bottomRight.y - topLeft.y
        if (width <= 0 || height <= 0) return null
        val region = RectRegion(width, height, topLeft)
        if (region.raw != this) return null
        return region
    }

    fun toRectRegion(): RectRegion = toRectRegionOrNull() ?: error("Cannot create Rect from $coordinates")

    companion object {
        val Empty: Region = Region(emptySet())
    }
}

@JvmName("regionString")
private fun List<String>.region(): Region = map { string -> string.toList() }.region()
@JvmName("rectRegionString")
private fun List<String>.rectRegion(): RectRegion = map { string -> string.toList() }.rectRegion()

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

private fun List<List<*>>.rectRegion(): RectRegion = region().toRectRegion()
