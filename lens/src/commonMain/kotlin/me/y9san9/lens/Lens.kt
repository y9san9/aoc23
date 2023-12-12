package me.y9san9.lens

public interface Lens<T, R> {
    public fun get(from: T): R
    public fun copy(from: T, value: R): T
}

public interface LensValue<T, R> {
    public fun get(): R
    public fun copy(value: R): T
}

public operator fun <T, R1, R2> Lens<T, R1>.plus(
    other: Lens<R1, R2>
): Lens<T, R2> = object : Lens<T, R2> {
    val base = this@plus

    override fun get(from: T): R2 {
        val intermediate = base.get(from)
        return other.get(intermediate)
    }

    override fun copy(from: T, value: R2): T {
        val intermediate = base.get(from)
        val modified = other.copy(intermediate, value)
        return base.copy(from, modified)
    }
}

public operator fun <T, R1, R2> Lens<T, R1>.get(other: Lens<R1, R2>): Lens<T, R2> = this + other
public operator fun <T, R1, R2> LensValue<T, R1>.get(
    other: Lens<R1, R2>
): LensValue<T, R2> = object : LensValue<T, R2> {
    val base = this@get

    override fun get(): R2 = other.get(base.get())

    override fun copy(value: R2): T {
        val intermediate = other.copy(base.get(), value)
        return base.copy(intermediate)
    }
}
public operator fun <T, R> T.get(lens: Lens<T, R>): LensValue<T, R> = object : LensValue<T, R> {
    val from = this@get
    override fun get(): R = lens.get(from)
    override fun copy(value: R): T = lens.copy(from, value)
}

public inline fun <T, R> lens(
    crossinline get: T.() -> R,
    crossinline inject: T.(R) -> T
): Lens<T, R> = object : Lens<T, R> {
    override fun get(from: T): R {
        return from.get()
    }
    override fun copy(from: T, value: R): T {
        return from.inject(value)
    }
}

private fun <T> T.context() = this

private data class User(
    val username: String
)

private data class Meeting(
    val creator: User
)

private data class MapMeeting(
    val meeting: Meeting
)

private fun main() {
    val mapMeeting = MapMeeting(
        meeting = Meeting(
            creator = User(
                username = "Test"
            )
        )
    )

    val newMeeting = mapMeeting.copy(
        meeting = mapMeeting.meeting.copy(
            creator = mapMeeting.meeting.creator.copy(
                username = "New Test"
            )
        )
    )

    val meeting = lens(MapMeeting::meeting) { copy(meeting = it) }
    val creator = lens(Meeting::creator) { copy(creator = it) }
    val username = lens(User::username) { copy(username = it) }

    val newMeeting2 = mapMeeting[meeting][creator][username].copy("New Test")
}
