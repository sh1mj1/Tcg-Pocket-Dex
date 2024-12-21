package tcg.pocket.dex

sealed interface PokemonType {
    data object Fire : PokemonType

    data object Water : PokemonType

    data object Grass : PokemonType

    data object Fighting : PokemonType

    data object Normal : PokemonType

    data object Psychic : PokemonType

    data object Dark : PokemonType

    data object Electric : PokemonType

    data object Steel : PokemonType
}
