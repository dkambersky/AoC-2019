fun main() = Day14().day()

class Day14 : Day(14) {
    val altInput1 = """10 ORE => 10 A
1 ORE => 1 B
7 A, 1 B => 1 C
7 A, 1 C => 1 D
7 A, 1 D => 1 E
7 A, 1 E => 1 FUEL""".lines()
    val recipes = altInput1.map { Recipe.fromStr(it) }.filterNotNull()

    fun day() {
        println(computePathTo(Reagent(1, "FUEL")))
    }

    fun fullPath(product: Reagent) {
        val reagents = mutableSetOf<Reagent>()

        var higherOrder = mutableSetOf<Reagent>()
        do {
            higherOrder = higherOrder(reagents)
            reagents -= higherOrder
            reagents += higherOrder.map { computePathTo(it) }.flatten()

        } while (higherOrder.isNotEmpty())


    }

    fun higherOrder(reagents: MutableSet<Reagent>) = reagents.filter { tested ->
        recipes.first { it.output == tested && it.inputs.all { it.type == "ORE" } } != null
    }.filterNotNull().toMutableSet()


    fun computePathTo(product: Reagent): MutableSet<Reagent> =
        recipes.filter { it.output == product }.map { it.inputs }.flatten().toMutableSet()


}

data class Recipe(
    val inputs: List<Reagent>,
    val output: Reagent
) {
    companion object {
        fun fromStr(input: String): Recipe? {
            if (input.isEmpty()) return null
            val (inp, out) = input.split("=>").map { it.trim() }

            return Recipe(inp.split(",").map {
                it.trim().split(" ").let { Reagent(it[0].toInt(), it[1]) }
            }, out.split(" ").let { Reagent(it[0].toInt(), it[1]) })
        }
    }
}

data class Reagent(val amount: Int, val type: String)