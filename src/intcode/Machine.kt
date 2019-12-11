package intcode

import intcode.Mode.*
import intcode.Operation.*
import kotlinx.coroutines.delay
import toInts

class Machine(
    input: String, val id: Int = -1
) {
    private fun processInput(input: String) = input
        .splitToSequence(",")
        .map { it.toLong() }
        .mapIndexed { index: Int, l: Long -> index to l }
        .associate { it.first.toLong() to it.second }
        .toMutableMap()

    var memory = processInput(input)
    val inputs = mutableListOf<Long>()
    var inputRead = 0
    var instPointer = 0L
    val outputCallbacks = mutableListOf<(Long) -> Unit>()
    var relativeBase = 0L
    var isOff = false

    suspend fun run() {
        while (step()) {
        }
        isOff = true
    }

    private suspend fun step(): Boolean {
        val instruction = Instruction.construct((memory[instPointer] ?: 0L).toInts())
        val par = instruction.parameters.mapIndexed { i, par -> getVal(par, i) }
        val ptr = instPointer

        when (instruction.operation) {
            HALT -> { isOff = true; return false }
            ADD -> setVal(par[2], par[0] + par[1])
            MULTIPLY -> setVal(par[2], par[0] * par[1])
            INPUT -> setVal(par[0], readInput())
            OUTPUT -> outputCallbacks.forEach { it.invoke(par[0]) }
            JUMP_IF_TRUE -> if (par[0] != 0L) instPointer = par[1]
            JUMP_IF_FALSE -> if (par[0] == 0L) instPointer = par[1]
            LESS_THAN -> setVal(par[2], if (par[0] < par[1]) 1L else 0L)
            EQUALS -> setVal(par[2], if (par[0] == par[1]) 1L else 0L)
            RELATIVE_BASE_OFFSET -> relativeBase += par[0]
        }

        if (instPointer == ptr)
            instPointer += instruction.operation.length()

        return true
    }

    private fun getVal(parameter: Parameter, position: Int): Long {
        val register = memory[instPointer + position + 1]

        if (parameter.write) {
            return when (parameter.mode) {
                IMMEDIATE, POSITION -> register ?: 0L
                RELATIVE -> relativeBase + (register ?: 0L)
            }
        }

        return when (parameter.mode) {
            IMMEDIATE -> register ?: 0L
            POSITION -> memory[register] ?: 0L
            RELATIVE -> memory[relativeBase + (register ?: 0L)] ?: 0L
        }
    }

    private fun setVal(pos: Long, value: Long) {
        memory[pos] = value
    }


    fun addInput(input: Long) = inputs.add(input)
    fun registerOutput(callback: (Long) -> Unit) = outputCallbacks.add(callback)
    fun registerOutputAsync(callback: (Long) -> Unit) = outputCallbacks.add(callback)
    private suspend fun readInput(): Long {
        while (inputs.size == inputRead) delay(1)
        return inputs[inputRead++]
    }

}

class Instruction(val operation: Operation, val parameters: List<Parameter>) {
    companion object {
        fun construct(ints: List<Int>): Instruction {
            val operation = Operation.byOpcode(ints.takeLast(2).mergeInts())
            val modes = ints.dropLast(2).reversed().toList().toMutableList()
            modes.addAll(generateSequence { 0 }.take(operation.parameters().size - modes.size))
            val parameters = modes.mapIndexed { i, mode ->
                Parameter(Mode.values()[mode], write = operation.isWrite(i))
            }
            return Instruction(operation, parameters)
        }
    }

    override fun toString(): String {
        return "[Operation ${this.operation}, parameters [${this.parameters.joinToString()}]"
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
    EQUALS(8, "IIO"),
    RELATIVE_BASE_OFFSET(9, "I");


    fun length() = parameters.length + 1
    fun parameters() = parameters.toModes()
    fun isWrite(i: Int): Boolean = parameters[i] == 'O'

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
    IMMEDIATE,
    RELATIVE
}

data class Parameter(
    val mode: Mode,
    val write: Boolean = false
)
