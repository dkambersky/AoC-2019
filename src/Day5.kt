import intcode.Machine
import java.io.File


private val input = File("input-5.txt").readText()


fun main() {
    // The bulk of this day's work is in the Machine class
    Machine(input).apply {
        run()
    }

}


