import intcode.Machine
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

suspend fun main() = Day13().day()
class Day13 : Day(13) {
    private lateinit var machine: Machine
    val inputs = mutableListOf<Int>()
    val screen = mutableMapOf<Point, Int>()
    var score = 0
    var pos = 0
    suspend fun day() {
        machine = Machine(input)
        machine.apply {
            outputCallbacks += { receive(it.toInt()) }
            memory[0] = 2
            run()
        }
    }

    fun receive(x: Int) {
        inputs.add(x)


        if ((pos + 1) % 3 == 0 && pos != 0)
            if (inputs[pos - 2] == -1 && inputs[pos - 1] == 0)
                score = inputs[pos]
            else
                paint(Point(inputs[pos - 2], inputs[pos - 1]), inputs[pos])

        pos++
    }


    private fun paint(loc: Point, type: Int) {
        screen[loc] = type
        if (type == 3) {
            // Move paddle under ball
            val ballLoc = screen.asSequence().first { it.value == 4 }.key
            machine.addInput(
                    when {
                        ballLoc.x > loc.x -> 1
                        ballLoc.x < loc.x -> -1
                        else -> 0
                    }
            )
            println("Sent input to machine")
        }

        draw()
    }

    private fun draw() {
        screen
                .map { (it.key.x to it.key.y) to it.value }
                .groupBy { it.first.second }
                .map {
                    it.key to it.value.sortedBy { it.first.first }.map { it.second }
                }.sortedBy { it.first }
                .map {
                    it.second.map { toSymbol(it) }.joinToString("")
                }.joinToString("\n")
                .apply {
                    println(this)
                    println("\nScore: $score")
                    println("Paddles: ${this.count { it == '2' }}")
                }
    }

    private fun toSymbol(num: Int) = when (num) {
        0 -> ' '
        1 -> '\u2588'
        2 -> 'X'
        3 -> '_'
        4 -> 'O'
        else -> 'z'
    }

}