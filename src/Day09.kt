
typealias FileInstance = Pair</* ID */Int, /* Count */Int>

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

    fun <T> ArrayDeque<T>.popLastIfTrue(compare: (T) -> Boolean): T? {
        (size - 1 downTo 0).forEach { i ->
            val item = this[i]
            if(compare(item)) {
                removeAt(i)
                return item
            }
        }
        return null
    }

    fun part2(input: Map<Int, Int>): Long {
        val files = mutableListOf<Int>()
        val space = mutableListOf<Int>()
        val filesStack = ArrayDeque<FileInstance>()
        input.forEach() {(pos, value) ->
            if (pos.isEven()) {
                files.add(value)
                filesStack.addLast(Pair(files.size - 1, value))
            } else {
                space.add(value)
            }
        }
        val diskMap = mutableListOf<Int>()
        files.withIndex().zip(space).forEach { (indexedFile, spacesCount) ->
            if (filesStack.isEmpty()) {
                return@forEach
            }

            val (_, fileCount) = indexedFile
            println("[${fileCount}]>> $filesStack")
            val (fileId, _) = filesStack.removeFirst()
            repeat(fileCount) { diskMap.add(fileId) }
            // Get the biggest file that can fit in the space
            var spaceTarget = spacesCount
            var remainingSpaces = 0
            while (spaceTarget > 0) {
                if (filesStack.isEmpty()) {
                    remainingSpaces += spaceTarget
                    break
                }
                val spaceFoundFile = filesStack.popLastIfTrue {(id, fileSize) ->
                    return@popLastIfTrue fileSize <= spaceTarget
                }

                if (spaceFoundFile != null) {
                    repeat(spaceFoundFile.second) { diskMap.add(spaceFoundFile.first) }
                    remainingSpaces = spaceTarget - spaceFoundFile.second
                    if (remainingSpaces > 0) {
                        spaceTarget = remainingSpaces
                        remainingSpaces = 0
                        continue
                    }
                    break
                }
                spaceTarget--
                remainingSpaces++
            }
            if (remainingSpaces > 0) {
                repeat(remainingSpaces) {  diskMap.add(0) }
            }
        }
        println("> $diskMap")
        return diskMap.mapIndexed() { i, value -> i * value.toLong() }.sum()
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test").first().toNumbers()
    check(part1(testInput) == 1928.toLong())
    check(part2(testInput) == 2858.toLong())

    return
    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09").first().toNumbers()
    part1(input).println()
//    part2(input).println()
}
