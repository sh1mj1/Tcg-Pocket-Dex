package tcg.pocket.dex

data class ExpansionPack(
    val code: String,
    val name: String,
) {
    companion object {
        private const val GENETIC_APEX_CODE = "A1"
        private const val GENETIC_APEX = "Genetic Apex"

        private const val MYTHICAL_ISLAND_CODE = "A1a"
        private const val MYTHICAL_ISLAND = "Mythical Island"

        private const val PROMO_A_CODE = "PROMO-A"
        private const val PROMO_A = "PROMO-A"

        val geneticApexMewtwo =
            ExpansionPack(
                code = GENETIC_APEX_CODE,
                name = "$GENETIC_APEX Mewtwo",
            )

        val geneticApexPikachu =
            ExpansionPack(
                code = GENETIC_APEX_CODE,
                name = "$GENETIC_APEX Pikachu",
            )

        val geneticApexCharizard =
            ExpansionPack(
                code = GENETIC_APEX_CODE,
                name = "$GENETIC_APEX Charizard",
            )

        val mythicalIsland =
            ExpansionPack(
                code = MYTHICAL_ISLAND_CODE,
                name = MYTHICAL_ISLAND,
            )

        val promoA =
            ExpansionPack(
                code = PROMO_A_CODE,
                name = PROMO_A,
            )
    }
}
