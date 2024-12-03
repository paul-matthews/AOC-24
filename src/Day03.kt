fun main() {
    val multiplierRegex = """(mul\((\d{1,3}),(\d{1,3})\))""".toRegex()
    fun MatchResult.getMultipliers(): Pair<Int, Int> {
        return Pair(groupValues[2].toInt(),groupValues[3].toInt())
    }
    fun Pair<Int, Int>.multiply() = first * second

    fun part1(input: List<String>): Int =
        input.sumOf {
            multiplierRegex
                .findAll(it)
                .sumOf { mul -> mul.getMultipliers().multiply() }
        }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}