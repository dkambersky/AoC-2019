import intcode.Machine
import java.io.File

private val input = File("input-5.txt").readText()

suspend fun main() = Machine(input).run()