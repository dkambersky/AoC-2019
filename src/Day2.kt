import java.io.File
import java.lang.Exception
import kotlin.math.max


private val input = File("input-2.txt").readText()
private const val input2 = 19690720L

fun main() {
    var machine = Machine(input)

    machine.instructions[1] = 12
    machine.instructions[2] = 2
    machine.run()
    println(machine.instructions[0])


    val part2 = run {
        for (i in 0..99) {
            for (j in 0..99) {
                machine = Machine(input)
                machine.instructions[1] = i.toLong()
                machine.instructions[2] = j.toLong()
                machine.run()
                if (machine.instructions[0] == input2) {
                    return@run i * 100 + j
                }
            }
        }
        null
    }
    println(part2)
}