package tcg.pocket.dex

/**
 * Converts a map of [DeckStats] into a sorted list of [CalculatedDeck] with formatted percentages.
 *
 * This function performs the final calculation layer that:
 * - Converts win rates from decimals (0.0-1.0) to formatted percentages (0.0%-100.0%)
 * - Calculates usage share based on total deck appearances
 * - Sorts results by usage share (descending), then win rate (descending)
 *
 * @receiver Map of deck IDs to their corresponding [DeckStats]
 * @return Sorted list of [CalculatedDeck] ready for UI display. Returns empty list if input is empty.
 *
 * Example:
 * ```
 * val deckStatsMap = mapOf(
 *     "pikachu-mewtwo" to DeckStats("pikachu-mewtwo", "Pikachu + Mewtwo", 100, 55, 45),
 *     "charizard-moltres" to DeckStats("charizard-moltres", "Charizard + Moltres", 50, 30, 20)
 * )
 * val calculated = deckStatsMap.toCalculatedDecks()
 * // Returns list sorted by usage share, then win rate
 * ```
 */
fun Map<String, DeckStats>.toCalculatedDecks(): List<CalculatedDeck> {
    // Handle empty input
    if (isEmpty()) return emptyList()

    // Calculate total number of deck appearances across all decks
    val totalDecks = values.sumOf { it.count }

    // Handle edge case where total is zero (shouldn't happen in practice)
    if (totalDecks == 0) return emptyList()

    // Transform each DeckStats into a CalculatedDeck with formatted percentages
    return values.map { deckStats ->
        // Convert win rate from decimal (0.0-1.0) to percentage (0.0%-100.0%)
        val winRatePercentage = deckStats.winRate * 100.0

        // Calculate usage share as percentage of total decks
        val usageSharePercentage = (deckStats.count.toDouble() / totalDecks) * 100.0

        CalculatedDeck(
            deckId = deckStats.deckId,
            deckName = deckStats.deckName,
            winRate = formatPercentage(winRatePercentage),
            usageShare = formatPercentage(usageSharePercentage),
            appearances = deckStats.count,
            iconUrls = deckStats.iconUrls,
        )
    }.sortedWith(
        // Primary sort: usage share descending (most popular first)
        // Secondary sort: win rate descending (highest win rate first)
        compareByDescending<CalculatedDeck> { it.usageShare.removeSuffix("%").toDouble() }
            .thenByDescending { it.winRate.removeSuffix("%").toDouble() },
    )
}

/**
 * Formats a percentage value to one decimal place with % suffix.
 *
 * @param value The percentage value to format (e.g., 50.0, 33.333)
 * @return Formatted percentage string (e.g., "50.0%", "33.3%")
 */
private fun formatPercentage(value: Double): String = "%.1f%%".format(value)
