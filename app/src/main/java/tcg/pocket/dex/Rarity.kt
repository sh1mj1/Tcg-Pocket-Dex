package tcg.pocket.dex

import androidx.annotation.DrawableRes

sealed interface Rarity {
    @get:DrawableRes
    val icon: Int

    data object Common : Rarity {
        override val icon: Int = R.drawable.common
    }

    data object Uncommon : Rarity {
        override val icon: Int = R.drawable.uncommon
    }

    data object Rare : Rarity {
        override val icon: Int = R.drawable.rare
    }

    data object DoubleRare : Rarity {
        override val icon: Int = R.drawable.double_rare_or_ultra_rare
    }

    data object IllustrationRare : Rarity {
        override val icon: Int = R.drawable.illustration_rare_or_art_rare
    }

    data object SuperRare : Rarity {
        override val icon: Int = R.drawable.super_rare_or_special_illustration_rare_or_special_art_rare
    }

    data object ImmersiveRare : Rarity {
        override val icon: Int = R.drawable.immersive_rare
    }

    data object CrownRare : Rarity {
        override val icon: Int = R.drawable.crown_rare
    }

    data object ShinyRare : Rarity {
        override val icon: Int = R.drawable.shiny_rare
    }

    data object DoubleShinyRare : Rarity {
        override val icon: Int = R.drawable.double_shiny_rare
    }

    companion object {
        fun fromString(rarity: String): Rarity {
            return when (rarity) {
                "Common" -> Common
                "Uncommon" -> Uncommon
                "Rare" -> Rare
                "Double Rare", "Ultra Rare" -> DoubleRare
                "Illustration Rare", "Art Rare" -> IllustrationRare
                "Super Rare", "Special Illustration Rare", "Special Art Rare" -> SuperRare
                "Immersive Rare" -> ImmersiveRare
                "Crown Rare" -> CrownRare
                "Shiny Rare" -> ShinyRare
                "Double Shiny Rare" -> DoubleShinyRare
                else -> Common // Default case
            }
        }
    }
}
