package me.y9san9.aoc.recycling

@JvmInline
value class Ownership private constructor(private val isOwned: Boolean) {
    fun <T> get(value: T): T = if (isOwned) {
        value
    } else {
        error("Object was disposed to another `Pinned`. You cannot use it anymore")
    }

    companion object {
        val Owned = Ownership(true)
        val Disposed = Ownership(false)
    }
}

class RecyclingList<T>(
    private val underlying: MutableList<T>,
    private val mode: RecyclingMode
) {
    private var ownership = Ownership.Owned
    private val isSecure = mode.ordinal == RecyclingMode.Secure.ordinal

    private val value: List<T> get() = ownership.get(underlying)

    val size: Int get() = value.size
    operator fun get(index: Int) = value[index]
    fun set(index: Int, value: T) = recycle { underlying[index] = value }
    inline fun calculate(index: Int, block: (T) -> T) = set(index, block(get(index)))
    operator fun iterator(): Iterator<T> = value.iterator()
    operator fun plus(element: T) = recycle { underlying.add(element) }
    fun drop(n: Int): RecyclingList<T> = recycle {
        repeat(n) { underlying.removeFirst() }
    }

    fun copy(): RecyclingList<T> = RecyclingList(underlying.toMutableList(), mode)

    private inline fun recycle(block: () -> Unit): RecyclingList<T> {
        return if (isSecure) {
            ownership = Ownership.Disposed
            block()
            RecyclingList(underlying, mode)
        } else {
            block()
            this
        }
    }

    override fun toString(): String = value.toString()
}

fun <T> emptyRecyclingList(): RecyclingList<T> = RecyclingList(0) { error("") }

inline fun <T> RecyclingList(
    size: Int,
    mode: RecyclingMode = RecyclingMode.Secure,
    initializer: (Int) -> T
): RecyclingList<T> = RecyclingList(MutableList(size, initializer), mode)
