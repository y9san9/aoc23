package me.y9san9.geometry.grid.list

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.grid.Grid

public class ListGrid<T>(
    private val underlying: List<List<T>>,
    override val topLeft: Coordinate = Coordinate(0, 0)
) : Grid<T> {
    init {
        require(underlying.isEmpty() || underlying.all { row -> underlying[0].size == row.size })
    }

    override val height: Int = underlying.size
    override val width: Int = underlying.getOrNull(0)?.size ?: 0

    override fun get(coordinate: Coordinate): T {
        val relative = coordinate - topLeft
        return underlying[relative.y][relative.x]
    }

    override fun toString(): String = underlying.toString()
}
