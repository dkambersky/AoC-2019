import intcode.Machine
import java.io.File


private val input = File("input-5.txt").readText()


fun main() {
    var machine = Machine(input)
    machine.run()
    println(machine.instructions[0])

}


