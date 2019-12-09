import intcode.Machine
import java.io.File

private val input = File("input-9.txt").readText()
private val input2 = "1102,34915192,34915192,7,4,7,99,0"
private val input3 = "104,1125899906842624,99"
private val input4 = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
suspend fun main() {
    Machine(input2).apply {
        val out = mutableListOf<String>()
        addInput(1)
        registerOutputCallback { out.add(it.toString())}
        run()

        print(out.joinToString())
    }

//    print("1219070632396864".length)

}
