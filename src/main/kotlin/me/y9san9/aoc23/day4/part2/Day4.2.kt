package me.y9san9.aoc23.day4.part2

import java.io.File

private data class Card(
    val index: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>
) {
    val points = numbers.count { number -> number in winningNumbers }
    override fun toString(): String = "Card #${index + 1}"
}

private fun main() {
    val cards = lines().map { line -> line.substringAfter(": ") }
        .map { line ->
            line.split(" | ").map { string ->
                string.split(" ").mapNotNull(String::toIntOrNull)
            }
        }.mapIndexed { i, (winningNumbers, numbers) -> Card(i, winningNumbers, numbers) }

    tailrec fun processCards(
        cards: List<Card>,
        processCards: List<Card> = cards.asReversed(),
        cache: Map<Card, Int> = emptyMap()
    ): Int {
        val card = processCards.firstOrNull() ?: return cache.values.sum()
        val producedCards = cards.drop(n = card.index + 1).take(card.points)
        val int = producedCards.sumOf(cache::getValue) + 1
        return processCards(
            cards = cards,
            processCards = processCards.drop(n = 1),
            cache = cache + (card to int)
        )
    }

    println(processCards(cards))
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day4/part2/Input.txt"
)

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

