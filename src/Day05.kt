import kotlin.math.abs

fun main() {
    class RulesIndex() {
        val notBeforeRules: MutableMap<Int, List<Int>> = mutableMapOf()

        fun addRule(before: Int, after: Int) {
            notBeforeRules[after] = notBeforeRules.getOrDefault(after, listOf()) + before
        }

        fun getNotAllowedBefore(element: Int) = notBeforeRules.getOrDefault(element, listOf())
    }

    fun checkRules(index: RulesIndex, items: List<Int>): Boolean {
        if (items.size < 2) return true
        val current = items.first()
        index.getNotAllowedBefore(current).forEach() {
            if(items.contains(it)) return false
        }
        return checkRules(index, items.drop(1))
    }

    fun List<Int>.getMiddleElement() = getOrNull(size / 2) ?: getOrNull(size / 2 - 1)

    fun part1(inputUpdates: List<List<Int>>, inputRules: RulesIndex): Int {
       return inputUpdates.map {
            if (checkRules(inputRules, it)) {
                return@map it.getMiddleElement() ?: 0
            }
            0
        }.sum()
    }

    fun part2(inputUpdates: List<String>, inputRules: List<String>): Int {
        return 0
    }

    fun List<String>.getRules() = map {
        val (f, s) = it.split("|")
        return@map Pair(f.toInt(), s.toInt())
    }

    fun List<String>.getInput() = map { it.split(",").map { item -> item.toInt() } }

    // Or read a large test input from the `src/Day05_test.txt` file:
    val testInput = readInput("Day05_test").getInput()
    val testInputRules: RulesIndex = RulesIndex().apply {
        readInput("Day05_test_rules").getRules().map {
            addRule(it.first, it.second)
        }
    }
    check(part1(testInput, testInputRules) == 143)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05").getInput()
    val inputRules: RulesIndex = RulesIndex().apply {
        readInput("Day05_rules").getRules().map {
            addRule(it.first, it.second)
        }
    }
    part1(input, inputRules).println()
    return
//    part2(input, inputRules).println()
}
