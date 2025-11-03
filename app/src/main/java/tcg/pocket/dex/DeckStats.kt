package tcg.pocket.dex

/**
 * Aggregated statistics for a deck across multiple tournament standings.
 *
 * @property deckId Unique identifier generated from sorted Pokemon composition
 * @property deckName Human-readable deck name (e.g., "Pikachu + Mewtwo")
 * @property count Number of times this deck appeared in standings
 * @property totalWins Sum of all wins across all standings for this deck
 * @property totalLosses Sum of all losses across all standings for this deck
 */
data class DeckStats(
    val deckId: String,
    val deckName: String,
    val count: Int,
    val totalWins: Int,
    val totalLosses: Int,
) {
    init {
        require(count >= 0) { "count must be non-negative, but was $count" }
        require(totalWins >= 0) { "totalWins must be non-negative, but was $totalWins" }
        require(totalLosses >= 0) { "totalLosses must be non-negative, but was $totalLosses" }
    }

    /**
     * Calculate win rate as a decimal between 0.0 and 1.0.
     * Returns 0.0 if there are no games (both wins and losses are 0).
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
}
