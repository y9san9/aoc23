package me.y9san9.geometry.grid

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.coordinate.neighbors
import me.y9san9.geometry.grid.list.ListGrid
import me.y9san9.geometry.region.*

public interface Grid<out T> {
    public val height: Int
    public val width: Int
    public val topLeft: Coordinate

    public operator fun get(coordinate: Coordinate): T
}

public val Grid<*>.coordinates: Set<Coordinate> get() = List(width) { x ->
    List(height) { y ->
        Coordinate(topLeft.x + x, topLeft.y + y)
    }
}.flatten().toSet()

public val Grid<*>.bottomRight: Coordinate get() = topLeft + Coordinate(width, height) - Coordinate(1, 1)

public val Grid<*>.minX: Int get() = topLeft.x
public val Grid<*>.minY: Int get() = topLeft.y

public val Grid<*>.maxX: Int get() = bottomRight.x
public val Grid<*>.maxY: Int get() = bottomRight.y

public operator fun <T> Grid<T>.contains(coordinate: Coordinate): Boolean {
    return coordinate.x in 0..<width && coordinate.y in 0..<height
}

public operator fun <T> Grid<T>.get(x: Int, y: Int): T = get(Coordinate(x, y))

public fun <T> Grid<T>.columns(): List<List<T>> {
    return List(width) { x ->
        List(height) { y ->
            get(topLeft.x + x, topLeft.y + y)
        }
    }
}

public fun <T> Grid<T>.rows(): List<List<T>> {
    return List(height) { y ->
        List(width) { x ->
            get(topLeft.x + x, topLeft.y + y)
        }
    }
}

public inline fun <T, R> Grid<T>.map(
    transform: (Coordinate, T) -> R
): Grid<R> {
    val list = rows().mapIndexed { y, row ->
        row.mapIndexed { x, element ->
            transform(Coordinate(topLeft.x + x, topLeft.y + y), element)
        }
    }
    return ListGrid(list)
}

public inline fun <T> Grid<T>.filter(
    predicate: (Coordinate, T) -> Boolean
): Region<T> = toRegion().filter(predicate)

public fun <T> Grid<T>.toRegion(): Region<T> = object : Region<T> {
    val grid = this@toRegion

    override val coordinates = grid.coordinates

    override fun get(coordinate: Coordinate): T {
        return grid[coordinate]
    }
}

public fun <T> Iterable<Iterable<T>>.toGrid(): Grid<T> = ListGrid(map(Iterable<T>::toList))

@JvmName("toStringGrid")
public fun Iterable<String>.toGrid(): Grid<Char> = ListGrid(map(String::toList))

public fun <T> Grid<T>.indexOfOrNull(element: T): Coordinate? = toRegion()
    .firstOrNull { _, current -> current == element }?.first

public fun <T> Grid<T>.indexOf(element: T): Coordinate =
    indexOfOrNull(element) ?: error("Cannot get $element from Grid")

public fun <T> Grid<T>.neighbors(coordinate: Coordinate): Region<T> = coordinate
    .neighbors()
    .filter { neighbor, _ -> neighbor in this }
    .map { neighbor, _ -> get(neighbor) }
