fun Coord.isValid(areaSize: Pair<Int, Int>): Boolean {
    return first >= 0 && second >= 0 &&
            first < areaSize.first &&
            second < areaSize.second
}

const val GUARD_CHAR = '^'
const val OBSTACLE_CHAR = '#'
const val NUM_VISITS_IS_LOOP = 5

enum class Direction {
    NORTH {
        override fun move(coord: Coord): Coord = coord.move(firstDiff = -1)
        override fun turn(): Direction = EAST
    },
    EAST {
        override fun move(coord: Coord): Coord = coord.move(secondDiff = 1)
        override fun turn(): Direction = SOUTH
    },
    SOUTH {
        override fun move(coord: Coord): Coord = coord.move(firstDiff = 1)
        override fun turn(): Direction = WEST
    },
    WEST {
        override fun move(coord: Coord): Coord = coord.move(secondDiff = -1)
        override fun turn(): Direction = NORTH
    };
    abstract fun move(coord: Coord): Coord
    abstract fun turn(): Direction
}

fun main() {
    data class MappedArea(val input: List<String>) {
        var guardLastCoord: Coord = Pair(-1, -1)
        var guardCoord: Coord = Pair(-1, -1)
        var guardDirection = Direction.NORTH
        var guardVisited = mutableMapOf<Coord, Int>()
        var obstacles = mutableListOf<Coord>()
        val area: MapInput = input.toMapInput {char, coord ->
            if (char == GUARD_CHAR) { guardVisits(coord) }
            if (char == OBSTACLE_CHAR) { obstacles.add(coord) }
            char.toString()
        }

        /**
         * Moves the guard and states whether it can move again
         *
         */
        fun moveGuard(): Boolean {
            val newLocation = getNextLocation()
            val areaSize = area.getSizeForSquare()
            if (newLocation.isValid(areaSize)) {
                guardVisits(newLocation)
            }
            return getNextLocation().isValid(areaSize)
        }

        fun hasObstacleOnTurnPath(): Coord? {
            val newDir = guardDirection.turn()
            var currentLocation = guardCoord
            while (!obstacles.contains(currentLocation) && currentLocation.isValid(area.getSizeForSquare())) {
                currentLocation = newDir.move(currentLocation)
            }
            return if (obstacles.contains(currentLocation)) guardDirection.move(guardCoord) else null
        }

        fun isInLoop(): Boolean =
            (guardVisited.getOrDefault(guardCoord, 0) > NUM_VISITS_IS_LOOP) &&
                    (guardVisited.getOrDefault(guardLastCoord, 0) > NUM_VISITS_IS_LOOP)

        fun addObstacle(coord: Coord): MappedArea = copy().also {
            it.obstacles.add(coord)
        }

        private fun getNextLocation(): Coord {
            var newLocation = guardDirection.move(guardCoord)
            if (obstacles.contains(newLocation)) {
                guardDirection = guardDirection.turn()
                newLocation = guardDirection.move(guardCoord)
            }
            return newLocation
        }

        private fun guardVisits(coord: Coord) {
            guardLastCoord = guardCoord
            guardCoord = coord
            guardVisited[coord] = guardVisited.getOrDefault(coord, 0) + 1
        }
    }
    fun part1(input: MappedArea): Int {
        while (input.moveGuard()) {
            // do nothing
        }
        return input.guardVisited.count()
    }

    fun part2(input: MappedArea): Int {
        val positionsToTry = mutableSetOf<Coord>()
        while (input.moveGuard()) {
            input.hasObstacleOnTurnPath()?.let {
                positionsToTry.add(it)
            }
        }
        return positionsToTry.count {testCoord ->
            val testInput = input.addObstacle(testCoord)
            while (testInput.moveGuard() && !testInput.isInLoop()) {
                // Do nothing
            }
            testInput.isInLoop()
        }
    }

    fun List<String>.prepareInput() = MappedArea(this)

    measureExecutionTime {
        // Or read a large test input from the `src/Day06_test.txt` file:
        val testInput = readInput("Day06_test")
        check(part1(testInput.prepareInput()) == 41)
        check(part2(testInput.prepareInput()) == 6)

        // Read the input from the `src/Day06.txt` file.
        val input = readInput("Day06")
        part1(MappedArea(input)).println()
        measureExecutionTime("part2") {
            part2(MappedArea(input)).println()
        }
    }
}
