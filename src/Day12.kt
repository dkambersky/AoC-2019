import kotlin.math.abs

fun main() = Day12().day()

class Day12 : Day(12) {
    val regex = Regex("=([\\d\\-]*)")
    val steps = 1000
    fun day() {
        val testLines = """<x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>""".trimIndent().lines()
        val moons = inputLines().mapIndexed { i, line ->
            regex.findAll(line)
                .map { it.groupValues[1].toInt() }.toList()
                .let { CelestialBody(Point3(it[0], it[1], it[2]), Point3(), i) }
        }


        println("\nAfter 0 steps:\n${moons.map { it.position to it.velocity }.joinToString("\n")}")
        for (step in 1..steps) {
            val pairs = moons.map { first ->
                moons.filterNot { it.id == first.id }.map { first to it }
            }.flatten().toMutableSet()

            for (moon in moons) {
                val candidates = pairs.filter { it.first == moon }
                    .forEach { pair ->
                        pairs.remove(pairs.firstOrNull {
                            it.first == pair.second && it.second == pair.first
                        })
                    }
            }


            pairs.forEach { applyGravity(it.first, it.second) }

            moons.forEach { it.position += it.velocity }
            println("\nAfter $step steps:\n${moons.map { it.position to it.velocity }.joinToString("\n")}")
        }

        println("Total energy: ${moons.sumBy { it.kineticEnergy() * it.potentialEnergy() }} ")

    }

    fun applyGravity(first: CelestialBody, second: CelestialBody) {
        if (first.position.x > second.position.x) {
            first.velocity.x--
            second.velocity.x++
        } else if (first.position.x < second.position.x) {
            first.velocity.x++
            second.velocity.x--
        }

        if (first.position.y > second.position.y) {
            first.velocity.y--
            second.velocity.y++
        } else if (first.position.y < second.position.y) {
            first.velocity.y++
            second.velocity.y--
        }
        if (first.position.z > second.position.z) {
            first.velocity.z--
            second.velocity.z++
        } else if (first.position.z < second.position.z) {
            first.velocity.z++
            second.velocity.z--
        }


    }

}

data class Point3(
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0
) {
    operator fun plus(other: Point3): Point3 {
        return Point3(
            x + other.x,
            y + other.y,
            z + other.z
        )
    }

    override fun toString() = "[$x, $y, $z]"
}

data class CelestialBody(
    var position: Point3,
    var velocity: Point3,
    val id: Int = 0
) {
    fun potentialEnergy() = abs(position.x) + abs(position.y) + abs(position.z)
    fun kineticEnergy() = abs(velocity.x) + abs(velocity.y) + abs(velocity.z)
}
