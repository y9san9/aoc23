package me.y9san9.aoc23.day5.part2

import java.io.File

private fun main() {
    val lines = lines()

    val seeds = lines.first().substringAfter(": ").split(" ")
        .map(String::toLong).chunked(2) { (start, length) -> start..<(start + length) }

    val mappings = lines.drop(n = 1).filter(String::isNotEmpty).fold(emptyList<List<Pair<LongRange, LongRange>>>()) { mappings, line ->
        if (!line[0].isDigit()) return@fold mappings.plusElement(emptyList())
        val (destination, source, length) = line.split(" ").map(String::toLong)
        val destinationRange = destination..<(destination + length)
        val sourceRange = source..<(source + length)
        val parsedMapping = sourceRange to destinationRange
        mappings.dropLast(n = 1).plusElement(element = mappings.last() + parsedMapping)
    }

    fun extractFromMapping(mapping: Pair<LongRange, LongRange>, long: Long): Long {
        val (source, destination) = mapping
        val difference = long - source.first
        return destination.first + difference
    }

    tailrec fun extractRanges(
        ranges: List<LongRange>,
        step: Int = 0
    ): List<LongRange> {
        if (step == mappings.size) return ranges
        val rangesMapping = mappings[step]
        val allRanges = ranges + rangesMapping.flatMap { (source, destination) -> listOf(source, destination) }
        // Combining all possible points –
        // only at such points suitable interval may start or end.
        // Between them it's guaranteed that interval will be suitable or not
        val combined = allRanges
            .fold(emptyList<Long>()) { acc, range -> acc + listOf(range.first, range.last + 1) }
            .sorted()

        return extractRanges(
            ranges = combined.zipWithNext { start, endExclusive ->
                // To check whether the given interval 'start..<endExclusive'
                // is suitable, we first check if it's even in the input ranges.
                // I can check any number of `start..<endExclusive` and the check
                // will remain valid for the entire interval
                if (ranges.none { range -> start in range }) return@zipWithNext null
                // Find a valid range that can map values of the given range
                val mapping = rangesMapping.firstOrNull { (source) -> start in source }
                    ?: return@zipWithNext start..<endExclusive
                // Map only start and end of the given range, other values will adjust
                // automatically
                val mappedStart = extractFromMapping(mapping, start)
                val mappedEnd = extractFromMapping(mapping, endExclusive - 1)
                mappedStart..mappedEnd
            }.filterNotNull(),
            step = step + 1
        )
    }

    val locations = extractRanges(seeds)
    val min = locations.mapNotNull { range -> range.minOrNull() }.min()

    println(min)
}

// SCAFFOLD

private fun lines() = inputFile().readLines()

private fun inputFile(): File = File(
    System.getenv("user.dir"),
    "src/main/kotlin/me/y9san9/aoc23/day5/part2/Input.txt"
)
