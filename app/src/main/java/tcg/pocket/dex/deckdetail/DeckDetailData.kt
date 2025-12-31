package tcg.pocket.dex.deckdetail

import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.allcards.CardDetail

data class DeckDetailData(
    val deck: CalculatedDeck,
    val pokemonCards: List<CardDetail>,
)
