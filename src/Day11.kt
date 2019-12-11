import Direction.UP
import intcode.Machine
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


suspend fun main() = Day11().day()

class Day11 : Day(11) {
    suspend fun day() {
        Robot(input, 0).apply {
            run()
            println(painted.size)
        }

        Robot(input, 1).apply {
            run()
            print(drawMap())
        }
    }

}

class Robot(
    program: String,
    private val initial: Long
) {
    private val outputCallback: (Long) -> Unit
    private val computer = Machine(program)
    private var facing = UP
    private var position = Point()
    private val inputs = mutableListOf<Long>()
    private var consumed = 0
    private val map: MutableMap<Point, Long> = mutableMapOf()
    val painted = mutableSetOf<Point>()

    init {
        outputCallback = { computer.addInput(it) }
        computer.registerOutput { inputs.add(it) }
    }


    fun output(output: Long) = outputCallback(output)
    fun input(input: Long) = println(input)

    suspend fun run() {
        GlobalScope.launch { computer.run() }
        computer.addInput(initial)
        while (!computer.isOff) step(readInput(), readInput())

    }

    private suspend fun readInput(): Long {
        while (inputs.size == consumed) delay(1)
        return inputs[consumed++]
    }

    private fun step(color: Long, turn: Long) {
        map[position] = color // Paint
        painted.add(position)

        facing = if (turn == 0L)
            facing.rotateCounterclockwise()
        else
            facing.rotateClockwise()

        position = position.inDirection(facing)
        output(map[position] ?: 0L)
    }

    fun drawMap() =
        map.entries
            .groupBy { it.key.y }
            .toSortedMap()
            .map { (_, points) ->
                val primary = points
                    .sortedBy { it.key.x }
                    .map { it.value }
                    .joinToString("")
                " ".repeat(points.minBy { it.key.x }!!.key.x) + primary
            }.reversed().joinToString("\n")
            .replace('1', '\u2588')
            .replace('0', ' ')
}