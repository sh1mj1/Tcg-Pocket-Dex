package tcg.pocket.dex

/**
 * Represents a deck with calculated win rate and usage share statistics.
 *
 * This model is the final output of deck statistics calculation, providing
 * formatted percentage strings ready for UI display.
 *
 * @property deckId Unique identifier for the deck
 * @property deckName Human-readable deck name (e.g., "Pikachu + Mewtwo")
 * @property winRate Formatted win rate percentage (e.g., "50.5%")
 * @property usageShare Formatted usage share percentage representing how often this deck appears (e.g., "25.0%")
 * @property appearances Number of times this deck appeared in tournament data
 */
data class CalculatedDeck(
    val deckId: String,
    val deckName: String,
    val winRate: String,
    val usageShare: String,
    val appearances: Int,
    val iconUrls: List<String> = emptyList(),
) {
    init {
        require(appearances >= 0) { "Appearances must be non-negative, but was $appearances" }
    }

    val pokemonNames: List<String>
        get() = deckId.split("|").filter { it.isNotBlank() }
}
