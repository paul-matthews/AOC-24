import kotlin.math.abs

fun main() {
    fun part1(input: List<List<Int>>): Int {
        return input.count {line ->
            // Assume that an empty line shouldn't be counted
            if (line.isEmpty()) return@count false
            // Check for items not being too distant from each other
            line.forEachIndexed() {i, current ->
                if (i == 0) {
                    return@forEachIndexed
                }
                // Check that the value isn't the same or more than 3 different to previous value
                val previous = line[i - 1]
                if (previous == current || abs(previous - current) > 3) {
                    return@count false
                }
            }
            // Check that the values are either all descending or ascending
            if (line != line.sorted() && line != line.sortedDescending()) {
                return@count false
            }
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
    check(part1(testInput) == 2)

    val input = readInput("Day02").prepareInput()
    part1(input).println()
    part2(input).println()
}
