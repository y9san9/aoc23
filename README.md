# Advent Of Code 2023 

## Restrictions

- <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-orange?logo=kotlin&logoColor=white&style=flat-square" width="50px"/>

- No [mutable state](#immutability) **at all** (`var` variables, mutable lists, etc.)

- To be done...

## Navigation

- Day 1
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day1/part1/Day1.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day1/part2/Day1.2.kt)

- Day 2
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day2/part1/Day2.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day2/part2/Day2.2.kt)

- Day 3
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day3/part1/Day3.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day3/part2/Day3.2.kt)

- Day 4
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day4/part1/Day4.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day4/part2/Day4.2.kt)

- Day 5
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day5/part1/Day5.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day5/part2/Day5.2.kt)

- Day 6
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day6/part1/Day6.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day6/part2/Day6.2.kt)

- Day 7
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day7/part1/Day7.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day7/part2/Day7.2.kt)

- Day 8
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day8/part1/Day8.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day8/part2/Day8.2.kt)

- Day 9
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day9/part1/Day9.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day9/part2/Day9.2.kt)

- Day 10
  - [Part 1](src/main/kotlin/me/y9san9/aoc23/day10/part1/Day10.1.kt)
  - [Part 2](src/main/kotlin/me/y9san9/aoc23/day10/part2/Day10.2.kt)

## Immutability

I know that lots of stdlib functions (like `map`, `sumOf`, etc.) use mutable state under the hood.
But that mutable stays in place meaning that I can implement such functions without
any mutability, so I still allowed to use them.


### Loops

You may ask how one will create loops which obviously use a concept
of a mutable variable and are obviously required to solve almost any task?

They will be emulated using tail-recursive functions like this:

```kotlin
fun loop(index: Int) {
    println(index)
    loop(index = index + 1)
}
```

This creates infinite loop without any mutability.

- But it will throw stackoverflow exception!!! – One may say

And they will be absolutely correct. It will throw exception unless we add 
a special modifier called `tailrec` which optimizes tail recursion and 
unwraps it back to loops after compilation. So:

```kotlin
tailrec fun loop(index: Int) {
    println(index)
    loop(index = index + 1)
}
```

This will not throw any exception and will infinitely print natural numbers.
