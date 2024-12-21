package tcg.pocket.dex

val fakePikachuCardCode: Card.Code by lazy {
    Card.Code(
        number = 94,
        expansionPack = ExpansionPack.geneticApexPikachu,
    )
}

val fakeRaichuCardCode: Card.Code by lazy {
    Card.Code(
        number = 95,
        expansionPack = ExpansionPack.geneticApexPikachu,
    )
}

val fakePikachuCard: Card.Pokemon by lazy {
    Card.Pokemon(
        code = fakePikachuCardCode,
        name = "pikachu",
        rarity = Rarity.Regular.common,
        illustrator = "sh1mj1",
        relatedCardCodes = listOf(fakeRaichuCardCode),
        battleAttributes =
            Card.Pokemon.BattleAttributes(
                type = PokemonType.Electric,
                hp = 60,
                moves =
                    listOf(
                        Move(
                            name = "Gnaw",
                            effect = "",
                            damage =
                                Move.Damage.Simple(
                                    basicValue = 10,
                                ),
                            cost = 1,
                        ),
                    ),
                ability = Card.Pokemon.BattleAttributes.Ability.NONE,
                weakness = PokemonType.Fighting,
                retreatCost = 1,
            ),
        flavorText =
            Card.Pokemon.FlavorText(
                dexId = 25,
                species = "Mouse",
                height = 0.4f,
                weight = 6.0f,
                description = "When it is angered, it immediately discharges the energy stored in the pouches in its cheeks.",
            ),
        evolution =
            Card.Pokemon.Evolution(
                stage = 0,
                evolveFrom = emptyList(),
                evolveTo = listOf(fakeRaichuCardCode),
            ),
    )
}

val fakeRaichuCard: Card.Pokemon by lazy {
    Card.Pokemon(
        code = fakeRaichuCardCode,
        name = "raichu",
        rarity = Rarity.Regular.rare,
        illustrator = "sh1mj1",
        relatedCardCodes = listOf(fakePikachuCardCode),
        battleAttributes =
            Card.Pokemon.BattleAttributes(
                type = PokemonType.Electric,
                hp = 100,
                moves =
                    listOf(
                        Move(
                            name = "Thunderbolt",
                            effect = "Discard all Energy attached to this Pokémon.",
                            damage =
                                Move.Damage.Simple(
                                    basicValue = 140,
                                ),
                            cost = 3,
                        ),
                    ),
                ability = Card.Pokemon.BattleAttributes.Ability.NONE,
                weakness = PokemonType.Fighting,
                retreatCost = 1,
            ),
        flavorText =
            Card.Pokemon.FlavorText(
                dexId = 26,
                species = "Mouse",
                height = 0.8f,
                weight = 30.0f,
                description = "Its tail discharges electricity into the ground, protecting it from getting shocked.",
            ),
        evolution =
            Card.Pokemon.Evolution(
                stage = 1,
                evolveFrom = listOf(fakePikachuCardCode),
                evolveTo = emptyList(),
            ),
    )
}
