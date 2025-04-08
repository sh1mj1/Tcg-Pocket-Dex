package tcg.pocket.dex

class EnglishStringMatcher {
    fun isMatched(
        target: String,
        search: String,
    ): Boolean {
        val normalizedSearch = search.trim().lowercase()
        val normalizedTarget = target.trim().lowercase()

        if (normalizedTarget == normalizedSearch) return true

        val targetWords = normalizedTarget.split(" ")

        if (normalizedSearch.length == 1) {
            return targetWords.first().startsWith(normalizedSearch)
        }

        if (normalizedSearch.length >= 4 && normalizedTarget.contains(normalizedSearch)) {
            return true
        }

        return targetWords.any { word ->
            word.startsWith(normalizedSearch)
        }
    }
}
