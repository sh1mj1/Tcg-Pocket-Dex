package tcg.pocket.dex.repo.decks

import tcg.pocket.dex.tierdecks.DeckInformation

interface DecksRepo {
    suspend fun allTierDecks(): List<DeckInformation>
}
