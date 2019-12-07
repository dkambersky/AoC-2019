package intcode

import intcode.Mode.IMMEDIATE
import intcode.Mode.POSITION
import intcode.Operation.*
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

class Machine(
    input: String,
    private val inputs: List<Int>? = null
) {
    private fun processInput(input: String) = input
        .splitToSequence(",")
        .map { it.toLong() }
        .toMutableList()

    var memory = processInput(input)
    var inputRead = 0
    var instPointer = 0
    var outputs = mutableListOf<Long>()

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
            INPUT -> {
                setVal(par[0], inputs?.get(inputRead)?.toLong()!!)
                inputRead++
            }
            OUTPUT -> outputs.add(par[0])
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
            modes.addAll(generateSequence { 0 }.take(operation.parameters().size - modes.size))

            val io = operation.parameters()
            val parameters = modes.mapIndexed { i, num ->
                val j = i + 1
                // Output always position mode

                if (io[i]) {
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
    private val parameters: String = ""
) {
    HALT(99),
    ADD(1, "IIO"),
    MULTIPLY(2, "IIO"),
    INPUT(3, "O"),
    OUTPUT(4, "I"),
    JUMP_IF_TRUE(5, "II"),
    JUMP_IF_FALSE(6, "II"),
    LESS_THAN(7, "IIO"),
    EQUALS(8, "IIO");


    fun length() = parameters.length + 1
    fun parameters() = parameters.toModes()

    companion object {
        fun byOpcode(code: Int) = values().firstOrNull { it.code == code }
            ?: throw IllegalArgumentException("Unknown operation: $code!")
    }
}


fun List<Int>.mergeInts() = joinToString("").toInt()
fun String.toModes(): List<Boolean> {
    return map {
        when (it) {
            'I' -> false
            'O' -> true
            else -> false
        }
    }
}

enum class Mode {
    POSITION,
    IMMEDIATE
}
