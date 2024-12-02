import kotlin.math.abs

fun main() {

    /** Check that all items are either ascending or descending */
    fun List<Int>.isMonotonic() = this == sorted() || this == sortedDescending()

    /** Get the diff of two numbers (regardless of sign) */
    fun Int.diff(other: Int) = abs(this - other)

    fun part1(input: List<List<Int>>): Int {
        return input.count {line ->
            // Assume that an empty line shouldn't be counted
            if (line.isEmpty()) return@count false

            // Look to discount line
            line.forEachIndexed() {i, current ->
                // Don't check the first one
                if (i == 0) return@forEachIndexed
                // Check that the value isn't the same or more than 3 different to previous value
                if (current.diff(line[i - 1]) !in 1..3) return@count false
            }
            if (!line.isMonotonic()) return@count false

            // Line is safe
            true
        }
    }

    fun part2(input: List<List<Int>>): Int {
        return input.size
    }

    fun List<String>.prepareInput() = map {
            it.splitOnWhitespace().toInts()
        }

    val testInput = readInput("Day02_test").prepareInput()
    println(part1(testInput))
    check(part1(testInput) == 2)

    val input = readInput("Day02").prepareInput()
    part1(input).println()
    part2(input).println()
}
