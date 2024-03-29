import intcode.Machine
import java.io.File

private val input = File("input-9.txt").readText()
suspend fun main() {
    Machine(input).apply {
        addInput(1)
        registerOutput { println(it) }
        run()
    }

    Machine(input).apply {
        addInput(2)
        registerOutput { println(it) }
        run()
    }
}
