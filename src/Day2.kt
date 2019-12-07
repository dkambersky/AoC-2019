import intcode.Machine
import java.io.File


private val input = File("input-2.txt").readText()
private const val input2 = 19690720L

suspend fun main() {
//    var machine = Machine("1,0,0,0,99")
    var machine = Machine(input)

    machine.memory[1] = 12
    machine.memory[2] = 2
    machine.run()

    println(machine.memory[0])


    val part2 = run {
        for (noun in 0..99) {
            for (verb in 0..99) {
                machine = Machine(input)
                machine.memory[1] = noun.toLong()
                machine.memory[2] = verb.toLong()
                machine.run()
                if (machine.memory[0] == input2) {
                    return@run noun * 100 + verb
                }
            }
        }
        null
    }
    println(part2)
}