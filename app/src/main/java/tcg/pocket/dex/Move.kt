package tcg.pocket.dex

data class Move(
    val name: String,
    val effect: String,
    val damage: Damage,
    val cost: Int,
) {
    sealed class Damage {
        abstract val basicValue: Int
        abstract val damagePostfix: String

        data class Simple(
            override val basicValue: Int,
        ) : Damage() {
            override val damagePostfix: String = ""
        }

        data class Multiplied(
            override val basicValue: Int,
        ) : Damage() {
            override val damagePostfix: String = "*"
        }

        data class Bonus(
            override val basicValue: Int,
            val bonus: Int,
        ) : Damage() {
            override val damagePostfix: String = "+"
        }
    }
}
