package me.y9san9.geometry.region.simple

import me.y9san9.geometry.coordinate.Coordinate
import me.y9san9.geometry.region.Region

public typealias SimpleRegion = Region<Unit>

public fun Iterable<Coordinate>.toRegion(): SimpleRegion = object : SimpleRegion {
    override val coordinates = this@toRegion.toSet()
    override fun get(coordinate: Coordinate) {
        require(coordinate in coordinates)
    }
    override fun toString(): String = coordinates.toString()
}
