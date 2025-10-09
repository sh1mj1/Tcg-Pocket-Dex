package tcg.pocket.dex

import androidx.annotation.DrawableRes

sealed interface PokemonType {
    @get:DrawableRes
    val icon: Int

    data object Fire : PokemonType {
        override val icon: Int = R.drawable.fire_icon
    }

    data object Water : PokemonType {
        override val icon: Int = R.drawable.water_icon
    }

    data object Grass : PokemonType {
        override val icon: Int = R.drawable.grass_icon
    }

    data object Fighting : PokemonType {
        override val icon: Int = R.drawable.fighting_icon
    }

    data object Normal : PokemonType {
        override val icon: Int = R.drawable.colorless_icon
    }

    data object Psychic : PokemonType {
        override val icon: Int = R.drawable.psychic_icon
    }

    data object Dark : PokemonType {
        override val icon: Int = R.drawable.darkness_icon
    }

    data object Electric : PokemonType {
        override val icon: Int = R.drawable.lightning_icon
    }

    data object Steel : PokemonType {
        override val icon: Int = R.drawable.metal_icon
    }

    data object Fairy : PokemonType {
        override val icon: Int = R.drawable.fairy_icon
    }
    data object Dragon : PokemonType {
        override val icon: Int = R.drawable.dragon_icon
    }


    companion object {
        fun fromString(type: String): PokemonType {
            return when (type) {
                "Fire" -> Fire
                "Water" -> Water
                "Grass" -> Grass
                "Fighting" -> Fighting
                "Colorless" -> Normal
                "Psychic" -> Psychic
                "Darkness" -> Dark
                "Lightning" -> Electric
                "Metal" -> Steel
                "Fairy" -> Fairy
                "Dragon" -> Dragon
                else -> Normal
            }
        }
    }
}
