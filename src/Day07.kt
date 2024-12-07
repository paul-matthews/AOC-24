fun main() {
    fun makesTotal(total: Long, values: List<Long>, part2: Boolean = false): Boolean {
        if (values.isEmpty()) return false
        if (values.size == 1) return (values.first() == total)
        val first = values.first()
        val second = values.second()
        val otherItems = values.drop(2)
        var operations = listOf(first + second, first * second)
        if (part2) { operations = operations + listOf("$first$second".toLong()) }
        operations.map {
            if (makesTotal(total, listOf(it) + otherItems, part2)) return true
        }
        return false
    }

    fun part1(input: List<String>): Long {
        return input.map {line ->
            val (total, partsStr) = line.split(": ")
            return@map if (makesTotal(total.toLong(), partsStr.splitOnWhitespace().map { it.toLong() })) {
                total.toLong()
            } else {
                0
            }
        }.sum()
    }

    fun part2(input: List<String>): Long {
        return input.map {line ->
            val (total, partsStr) = line.split(": ")
            return@map if (makesTotal(total.toLong(), partsStr.splitOnWhitespace().map { it.toLong() }, part2 = true)) {
                total.toLong()
            } else {
                0
            }
        }.sum()
    }

    measureExecutionTime {
        // Or read a large test input from the `src/Day07_test.txt` file:
        val testInput = readInput("Day07_test")
        check(part1(testInput) == 3749.toLong())
        check(part2(testInput) == 11387.toLong())

        // Read the input from the `src/Day07.txt` file.
        val input = readInput("Day07")
        part1(input).println()
        part2(input).println()
    }
}

