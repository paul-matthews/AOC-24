import kotlin.math.abs

fun main() {

    /** Check that all items are either ascending or descending */
    fun List<Int>.isMonotonic() = this == sorted() || this == sortedDescending()

    /** Get the diff of two numbers (regardless of sign) */
    fun Int.diff(other: Int) = abs(this - other)

    /** Test if a line is safe */
    fun List<Int>.isSafe(): Boolean {
        // Assume that an empty line shouldn't be counted
        if (isEmpty()) return false

        // Look to discount line
        forEachIndexed() {i, current ->
            // Don't check the first one
            if (i == 0) return@forEachIndexed
            // Check that the value isn't the same or more than 3 different to previous value
            if (current.diff(this[i - 1]) !in 1..3) return false
        }
        if (!isMonotonic()) return false

        // Line is safe
        return true
    }


    /** Check if a variant is safe **/
    fun List<Int>.variantIsSafe(): Boolean {
        forEachIndexed() { i, _ ->
            toMutableList().let {
                it.removeAt(i)
                if (it.isSafe()) return true
            }
        }
        return false
    }

    fun part1(input: List<List<Int>>): Int = input.count { it.isSafe() }

    fun part2(input: List<List<Int>>): Int = input.count {
        it.isSafe() || it.variantIsSafe()
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