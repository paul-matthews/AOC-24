fun main() {
    fun <T> ArrayDeque<T>.applyIfNotEmpty(action: (ArrayDeque<T>) -> Unit) {
        if (this.isNotEmpty()) {
            action(this)
        }
    }

    fun part1(input: Map<Int, Int>): Long {
        val files = mutableListOf<Int>()
        val space = mutableListOf<Int>()
        val filesStack = ArrayDeque<Int>()
        input.forEach() {(pos, value) ->
            if (pos.isEven()) {
                files.add(value)
                (0..<value).map { filesStack.addLast(files.size - 1) }
            } else {
                space.add(value)
            }
        }
        val diskMap = mutableListOf<Int>()
        files.withIndex().zip(space).forEach { (indexedFile, spacesCount) ->
            val (_, fileCount) = indexedFile
            repeat(fileCount) { filesStack.applyIfNotEmpty { diskMap.add(it.removeFirst()) } }
            repeat(spacesCount) { filesStack.applyIfNotEmpty { diskMap.add(it.removeLast()) } }
        }
        return diskMap.mapIndexed() { i, value -> i * value.toLong() }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test").first().toNumbers()
    check(part1(testInput) == 1928.toLong())

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09").first().toNumbers()
    part1(input).println()
//    part2(input).println()
}
