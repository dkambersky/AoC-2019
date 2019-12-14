fun main() = Day14().day()

class Day14 : Day(14) {
    fun day() {
        val recipes = inputLines().map { Recipe.fromStr(it) }
        val graph =

    }


}

data class Recipe(
        val inputs: List<Pair<Int, String>>,
        val output: Pair<Int, String>
) {
    companion object {
        fun fromStr(input: String): Recipe {
            val (inp, out) = input.split("=>")
            return Recipe(inp.split(",").map {
                it.trim().split(" ").let {
                    it[0].toInt() to it[1]
                }
            }, out.split(" ").let { it[0].toInt() to it[1] })
        }
    }
}