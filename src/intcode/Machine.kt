package intcode

import intcode.Mode.IMMEDIATE
import intcode.Mode.POSITION
import intcode.Operation.*
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

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
        val ptr = instPointer

        when (instruction.operation) {
            HALT -> return false
            ADD -> setVal(par[2], par[0] + par[1])
            MULTIPLY -> setVal(par[2], par[0] * par[1])
            INPUT -> setVal(
                par[0], readLine()?.toLongOrNull()
                    ?: throw NumberFormatException("Please enter an int!")
            )
            OUTPUT -> println(par[0])
            JUMP_IF_TRUE -> if (par[0] != 0L) instPointer = par[1].toInt()
            JUMP_IF_FALSE -> if (par[0] == 0L) instPointer = par[1].toInt()
            LESS_THAN -> setVal(par[2], if (par[0] < par[1]) 1L else 0L)
            EQUALS -> setVal(par[2], if (par[0] == par[1]) 1L else 0L)
        }

        if (instPointer == ptr)
            instPointer += instruction.operation.length()

        return true
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
    MULTIPLY(2, listOf(false, false, true)),
    INPUT(3, listOf(true)),
    OUTPUT(4, listOf(false)),
    JUMP_IF_TRUE(5, listOf(false, false)),
    JUMP_IF_FALSE(6, listOf(false, false)),
    LESS_THAN(7, listOf(false, false, true)),
    EQUALS(8, listOf(false, false, true));


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
