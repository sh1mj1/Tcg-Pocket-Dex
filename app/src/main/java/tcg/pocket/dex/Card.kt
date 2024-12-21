package tcg.pocket.dex

sealed class Card {
    data class Code(
        val number: Int,
        val expansionPack: ExpansionPack,
    )
}
