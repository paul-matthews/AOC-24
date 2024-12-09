fun main() {

    fun getAntinodes(a1: Coord, a2: Coord): Pair<Coord, Coord> {
        val diff1 = a1.first - a2.first
        val diff2 = a1.second - a2.second
        return Pair(a1.adjust(diff1, diff2), a2.adjust(-diff1, -diff2))
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
        return input.size
    }

    measureExecutionTime {
        // Or read a large test input from the `src/Day08_test.txt` file:
        val testInput = readInput("Day08_test")
        check(part1(testInput) == 14)

        // Read the input from the `src/Day08.txt` file.
        val input = readInput("Day08")
        part1(input).println()
//        part2(input).println()
    }
}

