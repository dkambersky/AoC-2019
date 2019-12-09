package intcode

import intcode.Mode.*
import intcode.Operation.*
import kotlinx.coroutines.delay
import toInts
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

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

    suspend fun run() {
        while (step()) {
        }
    }


    private suspend fun step(): Boolean {
        val instruction = Instruction.construct((memory[instPointer] ?: 0L).toInts())
        val par = instruction.parameters.mapIndexed { i, par -> getValue(par, i) }
        println("\t Parameters: [${par.joinToString()}]")
        val ptr = instPointer

        when (instruction.operation) {
            HALT -> return false
            ADD -> setVal(par[2], par[0] + par[1])
            MULTIPLY -> setVal(par[2], par[0] * par[1])
            INPUT -> {
                val inp = readInput()
                println("Writing $inp to ${par[0]}; offset $relativeBase ")

                setVal(par[0], inp)
            }
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

    private fun getValue(parameter: Parameter, position: Int): Long {

        val simpleVal = memory[instPointer + position + 1]
        println("Parameter $parameter, position $position, simpleVal $simpleVal")
        return when (parameter.mode) {
            IMMEDIATE -> simpleVal ?: 0L
            POSITION -> memory[simpleVal] ?: 0L
            RELATIVE -> memory[relativeBase + (simpleVal ?: 0L)] ?: 0L
        }


    }

    private fun setVal(pos: Long, value: Long) {
//        println("Setting mem at $pos to $value : existing ${memory[pos]}")
        memory[pos] = value
    }

    fun addInput(input: Long) = inputs.add(input)
    fun registerOutputCallback(callback: (Long) -> Unit) = outputCallbacks.add(callback)
    private suspend fun readInput(): Long {
        while (inputs.size == inputRead) {
            delay(10)
            print(".")
        }
        return inputs[inputRead++]

    }

}

class Instruction(val operation: Operation, val parameters: List<Parameter>) {
    companion object {
        fun construct(ints: List<Int>): Instruction {
            print("Constructing: $ints | ")
            val operation = Operation.byOpcode(ints.takeLast(2).mergeInts())
            val modes = ints.dropLast(2).reversed().toList().toMutableList()
            modes.addAll(generateSequence { 0 }.take(operation.parameters().size - modes.size))
            val parameters = modes.mapIndexed { i, mode ->
                Parameter(Mode.values()[mode], write = operation.isWrite(i))
            }
            return Instruction(operation, parameters).apply {
                println("Constructed operation $this")
            }
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
