package tcg.pocket.dex.tierdecks

data class DeckItemState(
    val content: DeckInformation,
    val expanded: Boolean = false,
) {
    fun expansionToggled(): DeckItemState = copy(expanded = !expanded)
}
