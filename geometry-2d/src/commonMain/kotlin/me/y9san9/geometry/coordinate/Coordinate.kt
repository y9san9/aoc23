package me.y9san9.geometry.coordinate

import me.y9san9.geometry.region.simple.SimpleRegion
import me.y9san9.geometry.region.simple.toRegion

public data class Coordinate(val x: Int, val y: Int) {
    public operator fun plus(coordinate: Coordinate): Coordinate = Coordinate(
        x = x + coordinate.x,
        y = y + coordinate.y
    )
    public operator fun minus(coordinate: Coordinate): Coordinate = Coordinate(
        x = x - coordinate.x,
        y = y - coordinate.y
    )
    public operator fun unaryMinus(): Coordinate = Coordinate(-x, -y)
}

public fun Coordinate.neighbors(): SimpleRegion {
    return buildSet {
        repeat(3) { deltaX ->
            repeat(3) { deltaY ->
                if (deltaX != 0 || deltaY != 0) add(Coordinate(x = x + deltaX - 1, y = y + deltaY - 1))
            }
        }
    }.toRegion()
}
