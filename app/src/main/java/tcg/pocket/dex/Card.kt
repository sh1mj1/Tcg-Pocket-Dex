package tcg.pocket.dex

sealed class Card {
    abstract val code: Code
    abstract val name: String
    abstract val rarity: Rarity
    abstract val illustrator: String
    abstract val relatedCardCodes: List<Code>
    val price: Int
        get() = this.rarity.price

    data class Code(
        val number: Int,
        val expansionPack: ExpansionPack,
    )

    data class Pokemon(
        override val code: Code,
        override val name: String,
        override val rarity: Rarity,
        override val illustrator: String,
        override val relatedCardCodes: List<Code>,
        val battleAttributes: BattleAttributes,
        val flavorText: FlavorText,
        val evolution: Evolution,
    ) : Card() {
        data class BattleAttributes(
            val type: PokemonType,
            val hp: Int,
            val moves: List<Move>,
            val ability: Ability = Ability.NONE,
            val weakness: PokemonType,
            val retreatCost: Int,
        ) {
            data class Ability(
                val name: String,
                val description: String,
            ) {
                companion object {
                    val NONE =
                        Ability(
                            name = "None",
                            description = "No ability",
                        )
                }
            }
        }

        data class FlavorText(
            val dexId: Int,
            val species: String,
            val height: Float,
            val weight: Float,
            val description: String,
        )

        data class Evolution(
            val stage: Int,
            val evolveFrom: List<Code> = emptyList(),
            val evolveTo: List<Code> = emptyList(),
        ) {
            init {
                if (stage == 0) require(evolveFrom.isEmpty()) { "Basic pokemon must not have previous evolution" }
                if (stage > 0) require(evolveFrom.isNotEmpty()) { "Evolved pokemon must have previous evolution" }
            }
        }
    }

    data class Support(
        override val code: Code,
        override val name: String,
        override val rarity: Rarity,
        override val illustrator: String,
        override val relatedCardCodes: List<Code>,
    ) : Card()

    data class Item(
        override val code: Code,
        override val name: String,
        override val rarity: Rarity,
        override val illustrator: String,
        override val relatedCardCodes: List<Code>,
    ) : Card()
}
