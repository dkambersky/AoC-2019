import intcode.Machine
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import util.Permutations
import java.io.File
import kotlin.math.max


private val input = File("input-7.txt").readText()

suspend fun main() {
    // Part 1 in commit history for now
    GlobalScope.launch {
        var maxScore = 0L
        for (perm in Permutations(5)) {
            val phases = perm.map { (it + 5).toLong() }
            maxScore = max(maxScore, feedbackLoop(phases, input).await())
        }
        print(maxScore)
    }.join()
}

private fun feedbackLoop(phases: List<Long>, inp: String = input): Deferred<Long> {
    val amplifiers = arrayOf(
        Machine(inp, 0),
        Machine(inp, 1),
        Machine(inp, 2),
        Machine(inp, 3),
        Machine(inp, 4)
    )
    amplifiers.forEachIndexed { i, machine ->
        machine.addInput(phases[i])
        machine.registerOutput {
            val outMachine = if (i == 4) 0 else i + 1
            amplifiers[outMachine].addInput(it)
        }
    }

    val jobs = amplifiers.map {
        it to GlobalScope.launch { it.run() }
    }

    // Capture outputs of last machine
    val outputs = mutableListOf<Long>()
    jobs[4].first.registerOutput { outputs.add(it) }

    // Start the process
    jobs[0].first.addInput(0)

    return GlobalScope.async {
        jobs.forEach { it.second.join() }
        return@async outputs.last()
    }


}