
typealias FileInstance = Pair</* ID */Int, /* Count */Int>

sealed class FileInfo() {
    abstract val size: Int
    data class File(val id: Int, override val size: Int): FileInfo() {
        override fun getReprOnDisk() = List(size) { id }
        override fun toString(): String = "F($size) #$id"
    }
    data class Space(override val size: Int): FileInfo() {
        override fun getReprOnDisk() = List(size) { 0 }
        override fun toString(): String = "S($size)"
    }

    abstract fun getReprOnDisk(): List<Int>
}

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

    fun <T> ArrayDeque<T>.replaceLastIfTrue(replaceWith: (T) -> T, compare: (T) -> Boolean): T? {
        (size - 1 downTo 0).forEach { i ->
            val item = this[i]
            if(compare(item)) {
                set(i, replaceWith(item))
                return item
            }
        }
        return null
    }


    fun part2(input: Map<Int, Int>): Long {
        val filesInfo = ArrayDeque<FileInfo>()
        var fileId = 0
        input.forEach() {(pos, value) ->
            if (pos.isEven()) {
                filesInfo.addLast(FileInfo.File(fileId++, value))
            } else {
                filesInfo.addLast(FileInfo.Space(value))
            }
        }
        val diskMap = mutableListOf<Int>()
        while (filesInfo.isNotEmpty()) {
            when (val current = filesInfo.removeFirst()) {
                is FileInfo.File -> {
                    diskMap.addAll(current.getReprOnDisk())}
                is FileInfo.Space -> {
                    for (biggestSpace in (current.size downTo  0)) {
                        if (filesInfo.replaceLastIfTrue({
                            FileInfo.Space(it.size)
                        }) { (it is FileInfo.File) && it.size <= biggestSpace }?.let { found ->
                                diskMap.addAll(found.getReprOnDisk())
                                if (found.size < current.size) {
                                    filesInfo.addFirst(FileInfo.Space(current.size - found.size))
                                }
                                true
                            } == true) { break }
                        if (biggestSpace == 0) {
                            diskMap.addAll(current.getReprOnDisk())
                        }
                    }
                }
            }
        }
        return diskMap.mapIndexed() { i, value -> i * value.toLong() }.sum()
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test").first().toNumbers()
    check(part1(testInput) == 1928.toLong())
    check(part2(testInput) == 2858.toLong())

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09").first().toNumbers()
    part1(input).println()
    part2(input).println()
}
