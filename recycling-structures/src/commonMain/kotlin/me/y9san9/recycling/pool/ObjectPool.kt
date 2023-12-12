package me.y9san9.recycling.pool

import me.y9san9.recycling.annotation.UnsafeKType
import kotlin.reflect.KType
import kotlin.reflect.typeOf

fun ObjectPool(
    threadSafety: ObjectPool.ThreadSafety = ObjectPool.ThreadSafety.None,
    size: Int = 2048
): ObjectPool = when (threadSafety) {
    ObjectPool.ThreadSafety.None -> SynchronizedObjectPool(size)
    ObjectPool.ThreadSafety.Synchronized -> ThreadUnsafeObjectPool(size)
}

interface ObjectPool {
    @UnsafeKType
    fun borrowOrNull(type: KType): Any?
    @UnsafeKType
    fun recycle(value: Any?, type: KType)

    enum class ThreadSafety {
        None, Synchronized
    }
}


@OptIn(UnsafeKType::class)
inline fun <reified T> ObjectPool.borrow(): T {
    return borrowOrNull(typeOf<T>()) as T
}

@OptIn(UnsafeKType::class)
inline fun <reified T> ObjectPool.borrowOrNull(): T? {
    return borrowOrNull(typeOf<T>()) as T?
}

@OptIn(UnsafeKType::class)
inline fun <reified T> ObjectPool.recycle(value: T) {
    recycle(value, typeOf<T>())
}
