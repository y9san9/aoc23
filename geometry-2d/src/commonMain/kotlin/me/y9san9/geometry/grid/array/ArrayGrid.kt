package me.y9san9.geometry.grid.array

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.grid.Grid

public class ArrayGrid<T>(
    private val delegate: Array<Array<T>>,
    override val topLeft: Coordinate = Coordinate(0, 0)
) : Grid<T> {

    init {
        require(delegate.all { row -> row[0] == row.size })
    }

    override val height: Int get() = delegate.size
    override val width: Int get() = delegate[0].size

    override fun get(coordinate: Coordinate): T {
        val relative = coordinate - topLeft
        return delegate[relative.y][relative.x]
    }
}
