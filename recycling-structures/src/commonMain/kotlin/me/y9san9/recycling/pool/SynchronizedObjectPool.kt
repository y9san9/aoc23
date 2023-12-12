package me.y9san9.recycling.pool

import me.y9san9.recycling.annotation.UnsafeKType
import kotlin.reflect.KType

class SynchronizedObjectPool(size: Int = 2048) : ObjectPool {
    private val underlying = ThreadUnsafeObjectPool(size)

    @UnsafeKType
    override fun borrowOrNull(type: KType): Any? =
        synchronized(type) { underlying.borrowOrNull(type) }

    @UnsafeKType
    override fun recycle(value: Any?, type: KType) {
        synchronized(type) { underlying.recycle(value, type) }
    }

    override fun toString(): String = underlying.toString()
}
