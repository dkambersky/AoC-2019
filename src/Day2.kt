import java.io.File
import java.lang.Exception
import kotlin.math.max


private val input = File("input-2.txt").readText()
private const val input2 = 19690720L

fun main() {
    var machine = Machine(input)

    machine.instructions[1] = 12
    machine.instructions[2] = 2
    machine.run()
    println(machine.instructions[0])


    val part2 = run {
        for (i in 0..99) {
            for (j in 0..99) {
                machine = Machine(input)
                machine.instructions[1] = i.toLong()
                machine.instructions[2] = j.toLong()
                machine.run()
                if (machine.instructions[0] == input2) {
                    return@run i * 100 + j
                }
            }
        }
        null
    }
    println(part2)
}

class Machine(input: String) {

    fun processInput(input: String) = input
        .splitToSequence(",")
        .map { it.toLong() }
        .toMutableList()


    var instructions = processInput(input)
    var pos = 0

    fun run() {
        while (step()) {
        }
    }


    private fun step(): Boolean {
//        println("CODE ${instructions[pos]}, ${pos + 3}, ${getVal(pos + 1)}, ${getVal(pos + 2)}")

        when (instructions[pos]) {
            99L -> return false
            1L -> {
//                println(getVal(0))
                setVal(pos + 3, getVal(pos + 1) + getVal(pos + 2))
//                println(getVal(0))
            }

            2L -> setVal(pos + 3, getVal(pos + 1) * getVal(pos + 2))
            else -> throw Exception("WTF")
        }
//        println(instructions[0])
//        println(pos)
        pos += 4
        return true
    }

    internal var maxGet = 0
    private fun getVal(pos: Int): Long {
        maxGet = max(pos, maxGet)
        return instructions[instructions[pos].toInt()]
    }

    private fun setVal(pos: Int, value: Long) {
//        println("$pos $value")
        instructions[instructions[pos].toInt()] = value
    }
}