typealias Row = Map<Int, String>
typealias Area = Map<Int, Row>
typealias Coord = Pair<Int, Int>

fun Coord.move(firstDiff: Int = 0, secondDiff: Int = 0) = Coord(first + firstDiff, second + secondDiff)
fun Coord.isValid(area: Area): Boolean {
    return first >= 0 && second >= 0 &&
            first < area.size &&
            second < (area.getOrDefault(0, emptyMap()).size)
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
        val area: Area = input.mapIndexed {x, line -> x to
                line.mapIndexed {y, char ->
                    if (char == GUARD_CHAR) { guardVisits(Pair(x, y)) }
                    if (char == OBSTACLE_CHAR) { obstacles.add(Pair(x, y)) }
                    y to char.toString()
                }.toMap()
        }.toMap()

        /**
         * Moves the guard and states whether it can move again
         *
         */
        fun moveGuard(): Boolean {
            val newLocation = getNextLocation()
            if (newLocation.isValid(area)) {
                guardVisits(newLocation)
            }
            return getNextLocation().isValid(area)
        }

        fun hasObstacleOnTurnPath(): Coord? {
            val newDir = guardDirection.turn()
            var currentLocation = guardCoord
            while (!obstacles.contains(currentLocation) && currentLocation.isValid(area)) {
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
        var count = 0
        val c = positionsToTry.count {testCoord ->
            val testInput = input.addObstacle(testCoord)
            while (testInput.moveGuard() && !testInput.isInLoop()) {
                // Do nothing
            }
            testInput.isInLoop()
        }
        return c
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
