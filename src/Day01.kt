import kotlin.math.abs

fun main() {
    fun part1(input: List<Pair<Int, Int>>): Int {
        val (leftList, rightList) = input.unzip()
        return leftList.sorted().zip(rightList.sorted()).sumOf { (left, right) ->
            abs(left - right)
        }
    }

    fun part2(input: List<Pair<Int, Int>>): Int {
        return input.size
    }

    fun List<String>.prepareInput() = map {
            val (first, second) = it.splitOnWhitespace()
            Pair(first.toInt(), second.toInt())
        }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test").prepareInput()
    check(part1(testInput) == 11)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01").prepareInput()
    part1(input).println()
    part2(input).println()
}
