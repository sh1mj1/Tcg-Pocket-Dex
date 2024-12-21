package tcg.pocket.dex

sealed class Rarity {
    abstract val value: String
    abstract val price: Int
    abstract val requiredExtraCardsForEffect: Int

    data class Regular(
        override val value: String,
        override val price: Int,
        override val requiredExtraCardsForEffect: Int,
    ) : Rarity() {
        companion object {
            val common =
                Regular(
                    value = "Common",
                    price = 35,
                    requiredExtraCardsForEffect = 3,
                )
            val uncommon =
                Regular(
                    value = "Uncommon",
                    price = 70,
                    requiredExtraCardsForEffect = 2,
                )
            val rare =
                Regular(
                    value = "Rare",
                    price = 150,
                    requiredExtraCardsForEffect = 1,
                )
            val doubleRare =
                Regular(
                    value = "Double Rare",
                    price = 500,
                    requiredExtraCardsForEffect = 1,
                )
        }
    }

    data class Irregular(
        override val value: String,
        override val price: Int,
        override val requiredExtraCardsForEffect: Int,
    ) : Rarity() {
        companion object {
            val artRare =
                Irregular(
                    value = "Art Rare",
                    price = 400,
                    requiredExtraCardsForEffect = 1,
                )
            val superRare =
                Irregular(
                    value = "Super Rare",
                    price = 1_250,
                    requiredExtraCardsForEffect = 1,
                )
            val superArtRare =
                Irregular(
                    value = "Super Art Rare",
                    price = 1_250,
                    requiredExtraCardsForEffect = 1,
                )
            val immersiveRare =
                Irregular(
                    value = "Immersive Rare",
                    price = 1_500,
                    requiredExtraCardsForEffect = 1,
                )
        }
    }
}
