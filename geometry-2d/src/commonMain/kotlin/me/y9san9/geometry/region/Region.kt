package me.y9san9.geometry.region

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.grid.Grid
import me.y9san9.geometry.grid.list.ListGrid
import me.y9san9.geometry.region.map.toRegion

public interface Region<out T> {
    public val coordinates: Set<Coordinate>
    public operator fun get(coordinate: Coordinate): T
}

public val Region<*>.size: Int get() = coordinates.size

public operator fun <T> Region<T>.get(x: Int, y: Int): T = get(Coordinate(x, y))

public operator fun Region<*>.contains(coordinate: Coordinate): Boolean = coordinate in coordinates

public fun <T> Region<T>.toList(): List<Pair<Coordinate, T>> =
    coordinates.map { coordinate -> coordinate to get(coordinate) }

public fun <T> Iterable<Pair<Coordinate, T>>.toRegion(): Region<T> = toMap().toRegion()

public inline fun <T> Region<T>.filter(
    predicate: (Coordinate, T) -> Boolean
): Region<T> = toList().filter { (coordinate, element) ->
    predicate(coordinate, element)
}.toRegion()

public inline fun <T, R> Region<T>.map(
    transform: (Coordinate, T) -> R
): Region<R> = toList()
    .map { (coordinate, element) -> coordinate to transform(coordinate, element) }
    .toRegion()

@Suppress("UNCHECKED_CAST")
public inline fun <reified T> Region<*>.filterIsInstance(): Region<T> =
    filter { _, element -> element is T } as Region<T>

public inline fun <T> Region<T>.firstOrNull(
    predicate: (Coordinate, T) -> Boolean
): Pair<Coordinate, T>? = toList().firstOrNull { (coordinate, element) -> predicate(coordinate, element) }

public inline fun <T> Region<T>.first(
    predicate: (Coordinate, T) -> Boolean
): Pair<Coordinate, T> = toList().first { (coordinate, element) -> predicate(coordinate, element) }

public inline fun <T> Region<T>.toGrid(
    initializer: (Coordinate) -> T
): Grid<T> {
    val topLeft = Coordinate(
        x = coordinates.minOf(Coordinate::x),
        y = coordinates.minOf(Coordinate::y)
    )
    val bottomRight = Coordinate(
        x = coordinates.maxOf(Coordinate::x),
        y = coordinates.maxOf(Coordinate::y)
    )
    val (width, height) = bottomRight - topLeft + Coordinate(1, 1)

    val list = List(height) { y ->
        List(width) { x ->
            val coordinate = Coordinate(x, y) + topLeft
            if (coordinate in this) this[coordinate] else initializer(coordinate)
        }
    }

    return ListGrid(list, topLeft)
}
