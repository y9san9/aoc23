package me.y9san9.aoc23.day8.part2

import me.y9san9.aoc23.day8.part2.Turn.Left
import me.y9san9.aoc23.day8.part2.Turn.Right
import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

data class BinaryTree(val nodes: Map<String, BinaryNode>) {
    val BinaryNode.left: BinaryNode get() = nodes.getValue(leftId)
    val BinaryNode.right: BinaryNode get() = nodes.getValue(rightId)
}

data class BinaryNode(
    val id: String,
    val leftId: String,
    val rightId: String
) {
    override fun toString(): String = "$leftId <-- [$id] --> $rightId"

    companion object {
        const val START_ID = "AAA"
        const val FINAL_ID = "ZZZ"
    }
}

enum class Turn { Left, Right }

private fun main() {
    val lines = lines()

    val turns = sequence {
        val line = lines.first()
        while (true) {
            yieldAll(line.iterator())
        }
    }.map { char ->
        when (char) {
            'L' -> Left
            'R' -> Right
            else -> stub()
        }
    }

    val nodes = lines.drop(n = 2).map { line ->
        val id = line.take(n = 3)
        val leftId = line.slice(7..9)
        val rightId = line.slice(12..14)
        require(leftId.length == 3)
        require(rightId.length == 3)
        BinaryNode(id, leftId, rightId)
    }.associateBy(BinaryNode::id)

    val tree = BinaryTree(nodes)

    tailrec fun findPathLength(
        turns: Iterator<Turn>,
        tree: BinaryTree,
        node: BinaryNode,
        turnsAcc: Int = 1
    ): Int {
        val turn = turns.next()
        val nextNode = when (turn) {
            Left -> with (tree) { node.left }
            Right -> with (tree) { node.right }
        }
        if (turnsAcc % 1_000_000 == 0) println(turnsAcc)
        if (nextNode.id.endsWith('Z')) return turnsAcc
        return findPathLength(
            turns = turns,
            tree = tree,
            node = nextNode,
            turnsAcc = turnsAcc + 1
        )
    }

    val startNodes = nodes.filter { (id) -> id.endsWith('A') }.values.toList()

    val lengths = startNodes.map { startNode ->
        findPathLength(
            turns = turns.iterator(),
            tree = tree,
            node = startNode
        )
    }

    println(lengths.fold(1L) { acc, i -> lcm(acc, i.toLong()) })
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day8/part2/Input.txt"
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

private inline fun <T> Sequence<T>.takeWhileInclusive(
    crossinline block: (T) -> Boolean
): Sequence<T> = sequence {
    for (element in this@takeWhileInclusive) {
        yield(element)
        if (!block(element)) break
    }
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

private fun gcd(a: Long, b: Long): Long {
    tailrec fun loop(
        a: Long,
        b: Long
    ): Long {
        if (b <= 0) return a
        return loop(
            a = b,
            b = a % b
        )
    }
    return loop(a, b)
}

private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}
