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

fun List<String>.toInts() = map { it.toInt() }

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
