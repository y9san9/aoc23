@file:OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)

package me.y9san9.aoc23.day4.part2

import kotlinx.cinterop.*
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import kotlin.time.measureTime

data class Card(
    val index: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>
) {
    val points = numbers.count { number -> number in winningNumbers }
//    override fun toString(): String = "Card #${index + 1}"
}

fun day4part2() {
//    println("Run mapBased:")
//    mapBased()
    repeat(100) {
        println("Run rightToLeft:")
        rightToLeft()
    }
}

private fun rightToLeft() = measureTime {
    val winningNumbers = IntArray(10)
    val numbers = IntArray(25)

    val cards = lines().mapIndexed { i, line ->

        var state = 0
        var number = 0
        var winningIndex = 0
        var numbersIndex = 0

        val digits = charArrayOf(
            '0', '1', '2',
            '3', '4', '5',
            '6', '7', '8',
            '9'
        )

        for (char in line) {
            when (state) {
                0 -> {
                    if (char == ':') state++
                }
                1 -> {
                    state++; continue
                }
                3 -> {
                    state++; continue
                }
                2, 4 -> {
                    val digitIndex = digits.indexOf(char)
                    if (digitIndex == -1) {
                        if (char == '|') { state++; continue }
                        if (number != 0) when (state) {
                            2 -> winningNumbers[winningIndex++] = number
                            4 -> numbers[numbersIndex++] = number
                        }
                        number = 0
                    } else {
                        number = number * 10 + digitIndex
                    }
                }
            }
        }

        Card(i, winningNumbers.toList(), numbers.toList())
    }

    tailrec fun processCards(
        cards: Iterator<Card>,
        cache: LongArray = LongArray(500_000)
    ): Long {
        if (!cards.hasNext()) return cache.sum()
        val card = cards.next()
        val int = (card.index + 1..card.index + card.points).sumOf { produced -> cache[produced] } + 1
        cache[card.index] = int
        return processCards(cards, cache)
    }

    println(processCards(cards.iterator()))
}.also(::println)

// SCAFFOLD

// sorry I don't want waste time to get it work with relative paths :((
private const val FILE_URI = "/Users/y9san9/IdeaProjects/aoc23/native/src/nativeMain/kotlin/me/y9san9/aoc23/day4/part2/Input.txt"

private fun lines() = sequence {
    measureTime {

        val file = fopen(FILE_URI, "r") ?: stub()

        try {
            memScoped {
                val readBufferLength = 64 * 1024
                val buffer = allocArray<ByteVar>(readBufferLength)
                while (true) {
                    val line = fgets(buffer, readBufferLength, file)?.toKString() ?: break
                    yield(line)
                }
            }
        } finally {
            fclose(file)
        }
    }.also(::println)
}

private inline fun <T> List<T>.indexOfFirstOrNull(
    block: (T) -> Boolean
): Int? = indexOfFirst(block).takeIf { it != -1 }

private inline fun <T, R> Iterable<T>.lastNotNullOf(block: (T) -> R?): R {
    return reversed().firstNotNullOf(block)
}

private inline fun <T, R> List<T>.lastNotNullOf(block: (T) -> R?): R {
    return asReversed().firstNotNullOf(block)
}

private fun stub(): Nothing = error("stub!")

private fun zeroIntTriple() = Triple(0, 0, 0)

private infix fun Int.smallPow(other: Int): Int {
    val base = this

    tailrec fun body(times: Int = other, acc: Int = 1): Int = when {
        times == 0 -> acc
        else -> body(times = times - 1, acc = acc * base)
    }

    return body()
}
