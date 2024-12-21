package tcg.pocket.dex

data class Evolution(
    val stage: Int,
    val evolveFrom: List<Card.Code>,
    val evolveTo: List<Card.Code>,
) {
    init {
        if (stage == 0) require(evolveFrom.isEmpty()) { "Basic pokemon must not have previous evolution" }
        if (stage > 0) require(evolveFrom.isNotEmpty()) { "Evolved pokemon must have previous evolution" }
    }
}
