package intcode

import java.lang.Exception
import kotlin.math.max

class Machine(input: String) {
    private fun processInput(input: String) = input
        .splitToSequence(",")
        .map { it.toLong() }
        .toMutableList()

    var instructions = processInput(input)
    var pos = 0

    fun run() {
        while (step()) {
            pos += 4
        }
    }


    private fun step(): Boolean {
        when (instructions[pos]) {
            99L -> return false
            1L -> setVal(pos + 3, getVal(pos + 1) + getVal(pos + 2))
            2L -> setVal(pos + 3, getVal(pos + 1) * getVal(pos + 2))
            else -> throw Exception("WTF")
        }

        return true
    }

    private fun getVal(pos: Int): Long = instructions[instructions[pos].toInt()]

    private fun setVal(pos: Int, value: Long) {
        instructions[instructions[pos].toInt()] = value
    }
}
