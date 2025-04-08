package tcg.pocket.dex

interface StringMatcher {
    fun isMatched(
        target: String,
        search: String,
    ): Boolean
}
