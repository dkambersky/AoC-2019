import intcode.Machine
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import util.Permutations
import java.io.File
import kotlin.math.max


private val input = File("input-7.txt").readText()
private val input2 = """3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"""
private val input3 = """3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"""

suspend fun main() {
//    println(
//        chainThrusters(
//            Permutations(5)
//                .maxBy { chainThrusters(it) }!!
//        )
//    )


    GlobalScope.launch {
        println(feedbackLoop(listOf(9, 7, 8, 5, 6), input3).await())
    }.join()


//        for (perm in Permutations(5)) {
//            val phases = perm.map { (it + 5).toLong() }
//            maxScore = max(maxScore, feedbackLoop(phases, input).await())
//            println("MaxScore")
//        }
//        print(maxScore)
    //}.join()


}

//fun chainThrusters(settings: List<Int>, inp: String = input): Int {
//    var out = 0
//    for (i in 0 until 5) {
//        out = Machine(inp, listOf(settings[i], out)).let {
//            it.run()
//            it.outputs[0].toInt()
//        }
//    }
//    return out
//}

fun feedbackLoop(phases: List<Long>, inp: String = input): Deferred<Long> {
    val amplifiers = arrayOf(
        Machine(inp, 0),
        Machine(inp, 1),
        Machine(inp, 2),
        Machine(inp, 3),
        Machine(inp, 4)
    )
    amplifiers.forEachIndexed { i, machine ->
        machine.addInput(phases[i])
        machine.registerOutputCallback {
            val outMachine = if (i == 4) 0 else i + 1
//            println("Passing $it from $i to $outMachine ")
            amplifiers[outMachine].addInput(it)
        }

    }

    val jobs = amplifiers.map {
        it to GlobalScope.launch {
            it.run()
        }
    }

    jobs[0].first.addInput(0)
    return GlobalScope.async {
        for (job in jobs) {
            job.second.join()
            print(".")
        }
        println(" Iteration finished.")
        return@async amplifiers[4].outputs.last()
    }


}