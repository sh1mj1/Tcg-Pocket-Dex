package tcg.pocket.dex

data class CardDeck(
    val name: String,
    val keyCards: List<Card>,
    val allCards: List<Card>,
) {
    init {
        require(allCards.containsAll(keyCards)) { "key cards in this deck aren't contained in allCards: keyCards - $keyCards" }
    }
}
