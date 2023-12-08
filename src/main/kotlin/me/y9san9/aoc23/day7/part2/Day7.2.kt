package me.y9san9.aoc23.day7.part2

import me.y9san9.aoc23.day7.part2.Hand.Card.Companion.JOKER_STRENGTH
import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

private val strengths = arrayOf(
    'A', 'K', 'Q',
    'J', 'T', '9',
    '8', '7', '6',
    '5', '4', '3',
    '2', 'J'
).reversed()

private data class Hand(
    val cards: List<Card>,
    val bid: Int
) {
    data class Card(val strength: Int) {
        companion object {
            const val JOKER_STRENGTH = 0
        }
    }
}

private fun Hand.score(): Int {
    return strengths.indices.maxOf { insteadOfJoker ->
        val withoutJoker = cards.map { card ->
            if (card.strength == JOKER_STRENGTH) Hand.Card(insteadOfJoker) else card
        }

        val counts = withoutJoker.groupingBy { it.strength }
            .eachCount().entries
            .map { (_, count) -> count }
            .sortedDescending()

        when (counts[0]) {
            5 -> 6
            4 -> 5
            3 -> when (counts[1]) {
                2 -> 4
                else -> 3
            }
            2 -> when (counts[1]) {
                2 -> 2
                else -> 1
            }
            else -> 0
        }
    }
}

fun main() {
    val lines = lines()

    val hands = lines.map { line ->
        val (cardsString, bidString) = line.split(" ")

        val cards = cardsString.map { char ->
            val strength = strengths.indexOf(char)
            Hand.Card(strength)
        }
        val bid = bidString.toInt()

        Hand(cards, bid)
    }

    val winners = hands.sortedWith(
        Comparator.comparingInt<Hand> { hand -> hand.score() }
            .thenComparingInt { hand -> hand.cards[0].strength }
            .thenComparingInt { hand -> hand.cards[1].strength }
            .thenComparingInt { hand -> hand.cards[2].strength }
            .thenComparingInt { hand -> hand.cards[3].strength }
            .thenComparingInt { hand -> hand.cards[4].strength }
    )

    val prod = winners.withIndex().sumOf { (i, winner) ->
        winner.bid * (i + 1)
    }

    println(prod)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day7/part2/Input.txt"
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
    return this.toFloat().pow(other).roundToInt()
}

private fun smallSqrt(int: Int): Int {
    return sqrt(int.toFloat()).roundToInt()
}

private fun solveQuadratic(a: Int, b: Int, c: Int): Pair<Int, Int> {
    val first = (-b + smallSqrt(b * b + 4 * a * c)) / 2
    val second = (-b + smallSqrt(b * b - 4 * a * c)) / 2
    return first to second
}

private fun smallSqrt(long: Long): Long {
    return sqrt(long.toFloat()).roundToLong()
}

private fun solveQuadratic(a: Long, b: Long, c: Long): Pair<Long, Long> {
    val first = (-b - smallSqrt(b * b - 4 * a * c)) / 2
    val second = (-b + smallSqrt(b * b - 4 * a * c)) / 2
    return first to second
}

