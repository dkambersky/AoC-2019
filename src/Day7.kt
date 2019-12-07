import intcode.Machine
import util.Permutations
import java.io.File

private val input = File("input-7.txt").readText()

fun main() {
    println(
        chainThrusters(
            Permutations(5)
                .maxBy { chainThrusters(it) }!!
        )
    )
}

fun chainThrusters(settings: List<Int>, inp: String = input): Int {
    var out = 0
    for (i in 0 until 5) {
        out = Machine(inp, listOf(settings[i], out)).let {
            it.run()
            it.outputs[0].toInt()
        }
    }
    return out
}