package me.y9san9.geometry.grid.simple

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.grid.Grid
import me.y9san9.geometry.grid.contains


public typealias SimpleGrid = Grid<Unit>

public fun SimpleGrid(
    width: Int,
    height: Int
): SimpleGrid = object : SimpleGrid {
    override val width = width
    override val height = height
    override fun get(coordinate: Coordinate) {
        require(coordinate in this)
    }
}
