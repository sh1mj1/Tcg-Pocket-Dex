package tcg.pocket.dex.repo.allcards

import tcg.pocket.dex.allcards.CardData

interface CardsRepo {
    fun allCards(): List<CardData>
}
