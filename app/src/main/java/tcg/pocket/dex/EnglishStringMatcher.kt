package tcg.pocket.dex

class EnglishStringMatcher: StringMatcher {
    private val rabinKarpMatcher by lazy { RabinKarpMatcher() }

    override fun isMatched(
        target: String,
        search: String,
    ): Boolean {
        val normalizedSearch = search.trim().lowercase()
        val normalizedTarget = target.trim().lowercase()

        if (normalizedTarget == normalizedSearch) return true

        val targetWords = normalizedTarget.split(" ")

        if (normalizedSearch.length == 1) {
            return targetWords.first()[0] == normalizedSearch[0]
        }

        if (normalizedSearch.length >= 4) {
            if (rabinKarpMatcher.contains(
                    text = normalizedTarget,
                    pattern = normalizedSearch,
                )
            ) {
                return true
            }
        }

        return targetWords.any { word ->
            word.startsWith(normalizedSearch)
        }
    }
}
