package me.y9san9.aoc.recycling.sync

actual inline fun <T> synchronized(
    lock: Any,
    block: () -> T
): T = kotlin.synchronized(lock, block)
