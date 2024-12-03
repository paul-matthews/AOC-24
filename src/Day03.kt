fun main() {
    val matchInstructionRegex = """((mul|do|don't)\(([0-9,]{0,7})\))""".toRegex()
    fun MatchResult.getInstruction(): String = groupValues[2]

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
        var isMatching = true
        var total = 0
        input.map {line ->
            matchInstructionRegex
                .findAll(line)
                .forEach {instruction ->
                    when(instruction.getInstruction()) {
                        "don't" -> isMatching = false
                        "do" -> isMatching = true
                        "mul" -> if (isMatching) total += part1(listOf(instruction.value))
                    }
                }
        }
        return total
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)

    val testInput2 = readInput("Day03_test2")
    check(part2(testInput2) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}