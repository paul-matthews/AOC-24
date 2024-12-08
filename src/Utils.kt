import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Split a string based on whitespace
 */
fun String.splitOnWhitespace() = split("""\s+""".toRegex())


/**
 * Convert a List of String to a List of Int
 *
 * A convenience method
 */
fun List<String>.toInts() = map { it.toInt() }

/**
 * An Coordinate representing object
 */
typealias Coord = Pair<Int, Int>

/**
 * Provide a function to move to another coordinate
 */
fun Coord.move(firstDiff: Int = 0, secondDiff: Int = 0) = Coord(first + firstDiff, second + secondDiff)

/**
 * A representation of a map
 */
typealias MapInput = Map<Int, Map<Int, String>>
fun List<String>.toMapInput(block: (Char, Coord) -> String = {c, _ -> c.toString()}): MapInput =
    mapIndexed { x, line ->
        x to line.mapIndexed { y, char ->
            y to block(char, Coord(x, y))
        }.toMap()
    }.toMap()

/**
 * Measure the execution time of a block in ms
 *
 * @param key is the label to apply, otheriwse "main is used"
 * @param block is the block to measure
 */
fun measureExecutionTime(key: String = "main", block: () -> Unit) {
    val startTime = System.currentTimeMillis()
    block()
    val endTime = System.currentTimeMillis()
    val executionTime = endTime - startTime

    println("#> [$key] Execution time: ${executionTime}ms")
}

/**
 * Get the second element in a list or throw an exception
 */
fun <T> List<T>.second(): T {
    if (size < 2)
        throw NoSuchElementException("List is empty.")
    return this[1]
}
