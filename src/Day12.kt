fun main() {
    fun coordGetRegion(mapInput: MapInput, mapSize: Pair<Int, Int>, coord: Coord, visted: Set<Coord> = emptySet()): Set<Coord> {
        val coordValue = mapInput.getCoord(coord)
        if (coordValue.isNullOrEmpty()) {
            return emptySet()
        }
        val foundCoords = (visted + setOf(coord)).toMutableSet()
        coord.getEdges(mapSize).map {edge ->
            if (edge in visted) {
                return@map
            }
            mapInput.getCoord(edge)?.let {visitingValue ->
                if (visitingValue != coordValue) {
                    return@map
                }
                foundCoords.addAll(coordGetRegion(mapInput, mapSize, edge, foundCoords))
            }
        }
        return foundCoords
    }
    fun countInternalEdges(coords: Set<Coord>) {
    }

    fun part1(input: List<String>): Int {
        val matchingEdges = mutableMapOf<Coord, MutableMap<Coord, String>>()
        val regions = mutableListOf<Pair<String, Set<Coord>>>()
        val mapInput = input.toMapInput() {char, coord ->
            matchingEdges[coord] = mutableMapOf()
            char.toString()
        }
        val mapSize = mapInput.getSizeForSquare()
        val visitedCoords = mutableSetOf<Coord>()
        mapInput.walk() {coord, value ->
            val letter = mapInput.getCoord(coord) ?: ""
            if (coord in visitedCoords) return@walk
            val region = coordGetRegion(mapInput, mapSize, coord)
            regions.add(Pair(letter, region))
            visitedCoords.addAll(region)
            coord.getEdges(mapSize).map {edge ->
                mapInput.getCoord(edge)?.let {
                    if (it == value) matchingEdges[coord]?.set(edge, it)
                }
            }
        }
        println("> Regions: $regions")
        regions.map { (letter, coords) ->
            println(">> [r: $letter] $coords (${coords.size})")
        }
//        coordsToCheck.map {
//            // Foreach coord
//            // Walk all the edges
//        }
//        mapInput.print()
//        matchingEdges.map { (coord, edgeValues) ->
//            val value = edgeValues.firstNotNullOf { (_, v) -> v }
//            println("> [$coord:$value] $edgeValues")
//        }

        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day12_test.txt` file:
    val testInput = readInput("Day12_test")
    val testInput2 = readInput("Day12_test2")
    val testInput3 = readInput("Day12_test3")
    check(part1(testInput) == 140)
//    check(part1(testInput2) == 772)
//    check(part1(testInput3) == 1930)
//    check(part2(testInput) == 2858)

    // Read the input from the `src/Day12.txt` file.
    val input = readInput("Day12")
//    part1(input).println()
//    part2(input).println()
}
