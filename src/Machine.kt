import java.lang.Exception
import kotlin.math.max

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