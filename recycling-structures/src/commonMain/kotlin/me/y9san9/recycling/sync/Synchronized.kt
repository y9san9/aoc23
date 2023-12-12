package me.y9san9.recycling.sync

expect inline fun <T> synchronized(
    lock: Any,
    block: () -> T
): T
