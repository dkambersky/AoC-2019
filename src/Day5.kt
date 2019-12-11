import intcode.Machine
import java.io.File

private val input = File("input-5.txt").readText()

suspend fun main() { run() }

suspend fun run() = Machine(input).apply {
    registerOutput { println(it) }
    run()
}