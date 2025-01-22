package tcg.pocket.dex.navigation

sealed class Screen {
    data object TierDecks : Screen()

    data object Search : Screen()

    data object Setting : Screen()

    data class DeckDetail(val deckId: String) : Screen()
}
