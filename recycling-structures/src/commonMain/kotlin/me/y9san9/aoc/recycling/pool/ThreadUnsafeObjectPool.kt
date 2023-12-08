package me.y9san9.aoc.recycling.pool

import me.y9san9.aoc.recycling.annotation.UnsafeKType
import kotlin.reflect.KType

class ThreadUnsafeObjectPool(
    private val size: Int = 2048
) : ObjectPool {
    private val available = mutableMapOf<KType, MutableList<Any?>>()

    @UnsafeKType
    override fun borrowOrNull(type: KType): Any? {
        return available[type]?.removeFirst()
    }

    @UnsafeKType
    override fun recycle(value: Any?, type: KType) {
        val instances = available[type]
        if (instances == null) {
            available[type] = mutableListOf(value)
        } else {
            if (instances.size == size) instances.removeLast()
            instances.add(value)
        }
    }

    override fun toString(): String = available.toString()
}
