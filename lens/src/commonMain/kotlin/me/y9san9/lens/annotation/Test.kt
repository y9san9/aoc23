package me.y9san9.lens.annotation


import me.y9san9.lens.annotation.User.Companion.name
import me.y9san9.lens.get
import me.y9san9.lens.lens

@lens class User(
    val name: String
) {
    companion object {
        val name = lens(
            get = User::name,
            inject = { User(it) }
        )
    }
}

fun main() {
    val user = User("string")

    user[name].copy("New Name")
}
