package me.y9san9.geometry.region.map

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.region.Region

public class MapRegion<out T>(
    private val underlying: Map<Coordinate, T>
): Region<T> {
    override val coordinates: Set<Coordinate> = underlying.keys
    override fun get(coordinate: Coordinate): T = underlying.getValue(coordinate)
    override fun toString(): String = underlying.toString()
}

public fun <T> Map<Coordinate, T>.toRegion(): MapRegion<T> = MapRegion(underlying = this)
