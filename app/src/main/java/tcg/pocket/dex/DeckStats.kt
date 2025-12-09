package tcg.pocket.dex

/**
 * Represents aggregated statistics for a deck across multiple tournament matches.
 *
 * This model stores raw win/loss counts and calculates win rate on demand.
 * Used as an intermediate representation before converting to [CalculatedDeck] for display.
 *
 * @property deckId Unique identifier for the deck
 * @property deckName Human-readable deck name (e.g., "Pikachu + Mewtwo")
 * @property count Number of times this deck appeared in tournament data
 * @property totalWins Total number of wins across all appearances
 * @property totalLosses Total number of losses across all appearances
 */
data class DeckStats(
    val deckId: String,
    val deckName: String,
    val count: Int,
    val totalWins: Int,
    val totalLosses: Int,
) {
    /**
     * Calculated win rate as a decimal value (0.0 to 1.0).
     *
     * Returns 0.0 if no games have been played (totalWins + totalLosses = 0).
     *
     * Example:
     * - 10 wins, 10 losses → 0.5 (50%)
     * - 30 wins, 10 losses → 0.75 (75%)
     * - 0 wins, 0 losses → 0.0 (no data)
     */
    val winRate: Double
        get() {
            val totalGames = totalWins + totalLosses
            return if (totalGames > 0) {
                totalWins.toDouble() / totalGames
            } else {
                0.0
            }
        }

    init {
        require(count >= 0) { "count must be non-negative, but was $count" }
        require(totalWins >= 0) { "totalWins must be non-negative, but was $totalWins" }
        require(totalLosses >= 0) { "totalLosses must be non-negative, but was $totalLosses" }
    }
}
