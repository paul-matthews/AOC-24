typealias Position = Pair<Int, Int>
typealias LetterMap = Map<Int, Map<Int, String>>

fun main() {
    val xmasRegex = """XMAS""".toRegex()
    val samxRegex = """SAMX""".toRegex()

    fun List<String>.toLetterMap(): LetterMap = mapIndexed {i, v ->
        i to v.toList().mapIndexed {j, w -> j to w.toString()}.toMap()
    }.toMap()
    fun getStringFromInput(input: LetterMap, positions: List<Position>): String {
        val pos = positions.first()
        input[pos.first]?.let {line ->
            line[pos.second]?.let {letter ->
                var remainingLetters = ""
                if (positions.size > 1) {
                    remainingLetters = getStringFromInput(input, positions.subList(1, positions.size))
                }
                return letter + remainingLetters
            }
        }
        return ""
    }

    fun getDirections(input: LetterMap, position: Position): List<String> {
        val upPositions = (position.first..position.first + 3).map { Position(it, position.second) }
        val upRight = (position.first..position.first + 3).zip((position.second..position.second + 3))
        val upLeft = (position.first..position.first + 3).zip((position.second downTo position.second - 3))
        val downPositions = (position.first downTo position.first - 3).map { Position(it, position.second) }
        val downRight = (position.first downTo position.first - 3).zip((position.second..position.second + 3))
        val downLeft = (position.first downTo position.first - 3).zip((position.second downTo position.second - 3))
        return listOf(
            getStringFromInput(input, upPositions),
            getStringFromInput(input, upRight),
            getStringFromInput(input, upLeft),
            getStringFromInput(input, downPositions),
            getStringFromInput(input, downRight),
            getStringFromInput(input, downLeft),
        )
    }

    fun part1(input: List<String>): Int {
        val inputMap = input.toLetterMap()
        var count = 0
        input.mapIndexed() {lineNo, line ->
            line.indices.map {i ->
                val current = line[i].toString()
                if (current == "X") {
                    count += getDirections(inputMap, Position(lineNo, i)).count { it == "XMAS" }
                }
            }
        }
        count += input.sumOf {
            xmasRegex.findAll(it).count() +
                    samxRegex.findAll(it).count()
        }
        return count
    }

    fun part2(input: List<String>): Int = input.size


    // Or read a large test input from the `src/Day04_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    return
    part2(input).println()
}
