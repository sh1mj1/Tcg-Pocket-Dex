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

        if (normalizedSearch.length >= 4) {
            if (rabinKarp(normalizedTarget, normalizedSearch)) return true
        }

        return targetWords.any { word ->
            word.startsWith(normalizedSearch)
        }
    }

    private fun rabinKarp(
        text: String,
        pattern: String,
    ): Boolean {
        val base = 256
        val prime = 101
        val m = pattern.length
        val n = text.length

        var patternHash = 0
        var textHash = 0
        var hash = 1

        repeat(m) { hash = (hash * base) % prime }

        (0 until m).forEach { i ->
            patternHash = (base * patternHash + pattern[i].code) % prime
            textHash = (base * textHash + text[i].code) % prime
        }

        (0..(n - m)).forEach { i ->
            if (patternHash == textHash && text.substring(i, i + m) == pattern) {
                return true
            }
            if (i < n - m) {
                textHash = (base * (textHash - text[i].code * hash) + text[i + m].code) % prime
                if (textHash < 0) textHash += prime
            }
        }
        return false
    }
}
