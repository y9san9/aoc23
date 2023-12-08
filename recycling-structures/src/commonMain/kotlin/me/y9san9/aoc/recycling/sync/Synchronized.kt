package me.y9san9.aoc.recycling.sync

expect inline fun <T> synchronized(
    lock: Any,
    block: () -> T
): T
