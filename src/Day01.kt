import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()
        input.map {
            val (left, right) = it.split("""\s+""".toRegex())
            leftList.add(left.toInt())
            rightList.add(right.toInt())
        }
        leftList.sort()
        rightList.sort()
        return leftList.zip(rightList).sumOf { (left, right) ->
            abs(left - right)
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
