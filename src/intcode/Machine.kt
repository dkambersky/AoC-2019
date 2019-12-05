package intcode

import intcode.Mode.IMMEDIATE
import intcode.Mode.POSITION
import intcode.Operation.*
import java.lang.IllegalArgumentException

class Machine(input: String) {
    private fun processInput(input: String) = input
        .splitToSequence(",")
        .map { it.toLong() }
        .toMutableList()

    var memory = processInput(input)
    var instPointer = 0

    fun run() {
        while (step()) {
        }
    }


    private fun step(): Boolean {
        val instruction = Instruction.construct(memory, instPointer)
        val par = instruction.parameters

        when (instruction.operation) {
            HALT -> return false
            ADD -> setVal(par[2], par[0] + par[1])
            MULTIPLY -> setVal(par[2], par[0] * par[1])
        }

        instPointer += instruction.operation.length()
        return true
    }

    private fun getVal(pos: Int): Long = memory[memory[pos].toInt()]

    private fun setVal(pos: Int, value: Long) {
        memory[pos] = value
    }

    private fun setVal(pos: Long, value: Long) {
        memory[pos.toInt()] = value
    }


}

class Instruction(val operation: Operation, val parameters: List<Long>) {
    companion object {
        fun construct(memory: List<Long>, pos: Int): Instruction {
            val input = memory[pos]
            val ints = input.toString().map { it.toString().toInt() }
            val operation = Operation.byOpcode(ints.takeLast(2).mergeInts())
            val modes = ints.dropLast(2).reversed().toMutableList()
            modes.addAll(generateSequence { 0 }.take(operation.parameters.size - modes.size))


            val parameters = modes.mapIndexed { i, num ->
                val j = i + 1
                // Output always position mode
                if (operation.parameters[i]) {
                    return@mapIndexed memory[pos + j]
                }

                when (Mode.values()[num]) {
                    IMMEDIATE -> memory[pos + j]
                    POSITION -> memory[memory[pos + j].toInt()]
                }
            }

            return Instruction(operation, parameters)

        }
    }

}


enum class Operation(
    val code: Int,
    val parameters: List<Boolean> = listOf()
) {
    HALT(99),
    ADD(1, listOf(false, false, true)),
    MULTIPLY(2, listOf(false, false, true));

    fun length() = parameters.size + 1

    companion object {
        fun byOpcode(code: Int) = values().firstOrNull { it.code == code }
            ?: throw IllegalArgumentException("Unknown operation: $code!")
    }
}


fun List<Int>.mergeInts() = joinToString("").toInt()

enum class Mode {
    POSITION,
    IMMEDIATE
}
