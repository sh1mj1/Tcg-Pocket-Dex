package tcg.pocket.dex.navigation

sealed interface Screen {
    data object Search : Screen

    data object Setting : Screen

    data class DeckDetail(val deckId: String) : Screen

    sealed interface BottomNavigation : Screen {
        data object TierDecks : BottomNavigation

        data object AllCards : BottomNavigation

        data object ExtensionPacks : BottomNavigation
    }
}
