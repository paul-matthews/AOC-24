import kotlin.math.abs

fun main() {

    fun getAntinodes(a1: Coord, a2: Coord): Pair<Coord, Coord> {
        val diff1 = a1.first - a2.first
        val diff2 = a1.second - a2.second
        return Pair(a1.adjust(diff1, diff2), a2.adjust(-diff1, -diff2))
    }

    fun Coord.isInlineWith(a1: Coord, a2: Coord): Boolean {
        // Calculate the area of the triangle formed by the three points
        val area = abs(first * (a1.second - a2.second)
                + a1.first * (a2.second - second)
                + a2.first * (second - a1.second)) / 2.0

        // If the area is 0, the points are collinear (on the same line)
        return area == 0.0
    }

    fun MapInput.printReplacingCoords(coords: List<Coord>, replaceWith: String) =
        print() {coord, value ->
            return@print if (coord in coords) replaceWith else value
        }

    fun part1(input: List<String>): Int {
        val antennas: MutableMap<String, MutableSet<Coord>> = mutableMapOf()
        val theMap = input.toMapInput() {char, coord ->
            val charStr = char.toString()
            if (char.isLetter() || char.isDigit()) {
                antennas[charStr] = antennas.getOrDefault(charStr, mutableSetOf()).apply {
                    add(coord)
                }
            }
            charStr
        }
        val mapSize = theMap.getSizeForSquare()
        val antinodesSet = mutableSetOf<Coord>()
        antennas.map {(letter, values) ->
            values.map {first ->
                values.map {second ->
                    if (first != second) {
                        getAntinodes(first, second).toList().map {
                            if (it.isValid(mapSize)) antinodesSet.add(it)
                        }
                    }
                }
            }
        }
        return antinodesSet.size
    }

    fun part2(input: List<String>): Int {
        val antennas: MutableMap<String, MutableSet<Coord>> = mutableMapOf()
        val theMap = input.toMapInput() {char, coord ->
            val charStr = char.toString()
            if (char.isLetter() || char.isDigit()) {
                antennas[charStr] = antennas.getOrDefault(charStr, mutableSetOf()).apply {
                    add(coord)
                }
            }
            charStr
        }
        val mapSize = theMap.getSizeForSquare()
        val antinodesSet = mutableSetOf<Coord>()
        antennas.map {(_, values) ->
            values.map {first ->
                values.map {second ->
                    if (first != second) {
                        getAntinodes(first, second).toList().map {
                            if (it.isValid(mapSize)) antinodesSet.add(it)
                        }
                        theMap.forEach {(x, line) ->
                            line.forEach {(y, _) ->
                                val current = Coord(x, y)
                                if (current !in listOf(first, second)) {
                                    if (current.isInlineWith(first, second)) antinodesSet.add(current)
                                }
                            }
                        }
                    }
                }
            }
        }
        println("size: ${antinodesSet.size}")
        theMap.printReplacingCoords(antinodesSet.toList(), "#")
        return antinodesSet.size - 1
    }

    measureExecutionTime {
        // Or read a large test input from the `src/Day08_test.txt` file:
        val testInput = readInput("Day08_test")
        check(part1(testInput) == 14)

        val testInput2 = readInput("Day08_test2")
        check(part2(testInput2) == 34)

        // Read the input from the `src/Day08.txt` file.
        val input = readInput("Day08")
        part1(input).println()
        part2(input).println()
    }
}

