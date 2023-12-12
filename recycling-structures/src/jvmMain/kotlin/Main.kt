
import me.y9san9.recycling.RecyclingList
import me.y9san9.recycling.RecyclingMode
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    repeat(10) {
        testFunction(n = 10)
    }
    System.gc()
    testFunction(n = 100)
}

private fun testFunction(n: Int) {
    print("Immutable List (ops/sec): ")
    (2_000_000 * n / measureTimeMillis {
        repeat(n) {
            val random = Random(0)
            val list = List(1_000_000) { random.nextLong() }
            val newList = list.map { number -> number * random.nextLong() }
            blackHole(newList)
        }
    }).let(::println)

    System.gc()
    Thread.sleep(300)

    print("Recycling List (ops/sec): ")
    (2_000_000 * n / measureTimeMillis {
        repeat(n) {
            val random = Random(2)
            val list = RecyclingList(1_000_000, RecyclingMode.Fast) { random.nextLong() }
            val newList = mapRecyclingList(list, random)
            blackHole(newList)
        }
    }).let(::println)

    System.gc()
    Thread.sleep(300)

    print("Mutable List (ops/sec): ")
    (2_000_000 * n / measureTimeMillis {
        repeat(n) {
            val random = Random(1)
            val list = MutableList(1_000_000) { random.nextLong() }
            for (i in list.indices) {
                list[i] = list[i] * random.nextLong()
            }
            blackHole(list)
        }
    }).let(::println)

    System.gc()
    Thread.sleep(300)

    print("Immutable List Recursive (ops/sec): ")
    (20_000 * n / measureTimeMillis {
        repeat(n) {
            val random = Random(2)
            val list = List(10_000) { random.nextLong() }
            val newList = mapList(list, random)
            blackHole(newList)
        }
    }).let(::println)

    System.gc()
    Thread.sleep(300)

    println()
}

tailrec fun mapRecyclingList(
    source: RecyclingList<Long>,
    random: Random,
    index: Int = 0
): RecyclingList<Long> {
    if (index == source.size) return source
    return mapRecyclingList(
        source = source.calculate(index) { number -> number * random.nextLong() },
        random = random,
        index = index + 1
    )
}

tailrec fun mapList(
    source: List<Long>,
    random: Random,
    acc: List<Long> = emptyList()
): List<Long> {
    if (source.isEmpty()) return acc
    return mapList(
        source = source.drop(n = 1),
        random = random,
        acc = acc + source.first() * random.nextLong()
    )
}

private var someValue = Any()

private fun blackHole(value: Any?) {
    if (value === someValue) {
        println(value)
    }
}
