package tcg.pocket.dex.remote.response

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StandingsResponseTest : FunSpec({

    test("toStanding should map all fields correctly with complete data") {
        val response =
            StandingResponse(
                placing = 1,
                player =
                    PlayerResponse(
                        name = "John Doe",
                        country = "USA",
                        region = "North America",
                    ),
                deck =
                    DeckResponse(
                        pokemon = listOf("Pikachu ex", "Zapdos ex", "Articuno ex"),
                    ),
                record =
                    RecordResponse(
                        wins = 8,
                        losses = 1,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.placing shouldBe 1
        result.playerName shouldBe "John Doe"
        result.country shouldBe "USA"
        result.region shouldBe "North America"
        result.deckPokemon shouldBe listOf("Pikachu ex", "Zapdos ex", "Articuno ex")
        result.wins shouldBe 8
        result.losses shouldBe 1
        result.ties shouldBe 0
    }

    test("toStanding should handle nullable fields correctly when all are null") {
        val response =
            StandingResponse(
                placing = 5,
                player =
                    PlayerResponse(
                        name = "Anonymous Player",
                        country = null,
                        region = null,
                    ),
                deck = null,
                record =
                    RecordResponse(
                        wins = 5,
                        losses = 3,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.placing shouldBe 5
        result.playerName shouldBe "Anonymous Player"
        result.country shouldBe null
        result.region shouldBe null
        result.deckPokemon shouldBe emptyList()
        result.wins shouldBe 5
        result.losses shouldBe 3
        result.ties shouldBe 0
    }

    test("toStanding should handle null deck with null pokemon list") {
        val response =
            StandingResponse(
                placing = 10,
                player =
                    PlayerResponse(
                        name = "Jane Smith",
                        country = "Canada",
                        region = "North America",
                    ),
                deck =
                    DeckResponse(
                        pokemon = null,
                    ),
                record =
                    RecordResponse(
                        wins = 4,
                        losses = 4,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.deckPokemon shouldBe emptyList()
    }

    test("StandingResponse should use default null values for optional fields") {
        val response =
            StandingResponse(
                placing = 3,
                player =
                    PlayerResponse(
                        name = "Minimal Player",
                    ),
                record =
                    RecordResponse(
                        wins = 6,
                        losses = 2,
                        ties = 0,
                    ),
            )

        response.deck shouldBe null
        response.player.country shouldBe null
        response.player.region shouldBe null
    }

    test("PlayerResponse should have all fields accessible") {
        val player =
            PlayerResponse(
                name = "Alice Johnson",
                country = "United Kingdom",
                region = "Europe",
            )

        player.name shouldBe "Alice Johnson"
        player.country shouldBe "United Kingdom"
        player.region shouldBe "Europe"
    }

    test("DeckResponse should contain all Pokemon in list") {
        val deck =
            DeckResponse(
                pokemon = listOf("Mewtwo ex", "Gardevoir", "Ralts", "Kirlia"),
            )

        deck.pokemon shouldBe listOf("Mewtwo ex", "Gardevoir", "Ralts", "Kirlia")
        deck.pokemon?.size shouldBe 4
    }

    test("RecordResponse should have correct wins, losses, and ties") {
        val record =
            RecordResponse(
                wins = 7,
                losses = 2,
                ties = 1,
            )

        record.wins shouldBe 7
        record.losses shouldBe 2
        record.ties shouldBe 1
    }

    test("DeckResponse with empty Pokemon list should have empty list") {
        val response =
            StandingResponse(
                placing = 8,
                player =
                    PlayerResponse(
                        name = "Bob Williams",
                    ),
                deck =
                    DeckResponse(
                        pokemon = emptyList(),
                    ),
                record =
                    RecordResponse(
                        wins = 3,
                        losses = 5,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.deckPokemon shouldBe emptyList()
    }

    test("toStanding with placing = 1 should preserve winner placing") {
        val response =
            StandingResponse(
                placing = 1,
                player =
                    PlayerResponse(
                        name = "Tournament Winner",
                        country = "Japan",
                    ),
                deck =
                    DeckResponse(
                        pokemon = listOf("Charizard ex"),
                    ),
                record =
                    RecordResponse(
                        wins = 9,
                        losses = 0,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.placing shouldBe 1
        result.playerName shouldBe "Tournament Winner"
        result.wins shouldBe 9
        result.losses shouldBe 0
    }

    test("toStanding with high placing number should preserve placing value") {
        val response =
            StandingResponse(
                placing = 100,
                player =
                    PlayerResponse(
                        name = "Lower Rank Player",
                    ),
                record =
                    RecordResponse(
                        wins = 1,
                        losses = 8,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.placing shouldBe 100
        result.wins shouldBe 1
        result.losses shouldBe 8
    }

    test("toStanding should preserve unicode characters in player name") {
        val response =
            StandingResponse(
                placing = 2,
                player =
                    PlayerResponse(
                        name = "ポケモントレーナー",
                        country = "日本",
                        region = "アジア",
                    ),
                deck =
                    DeckResponse(
                        pokemon = listOf("ピカチュウex"),
                    ),
                record =
                    RecordResponse(
                        wins = 7,
                        losses = 1,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.playerName shouldBe "ポケモントレーナー"
        result.country shouldBe "日本"
        result.region shouldBe "アジア"
        result.deckPokemon shouldBe listOf("ピカチュウex")
    }

    test("toStanding should handle special characters in player name") {
        val response =
            StandingResponse(
                placing = 4,
                player =
                    PlayerResponse(
                        name = "O'Brien-Müller",
                        country = "Germany",
                    ),
                record =
                    RecordResponse(
                        wins = 6,
                        losses = 2,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.playerName shouldBe "O'Brien-Müller"
    }

    test("toStanding with record having only ties should map correctly") {
        val response =
            StandingResponse(
                placing = 15,
                player =
                    PlayerResponse(
                        name = "Tie Master",
                    ),
                record =
                    RecordResponse(
                        wins = 0,
                        losses = 0,
                        ties = 9,
                    ),
            )

        val result = response.toStanding()

        result.wins shouldBe 0
        result.losses shouldBe 0
        result.ties shouldBe 9
    }

    test("toStanding with record having no ties should have ties = 0") {
        val response =
            StandingResponse(
                placing = 7,
                player =
                    PlayerResponse(
                        name = "No Ties Player",
                    ),
                record =
                    RecordResponse(
                        wins = 5,
                        losses = 4,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.ties shouldBe 0
    }

    test("toStanding with large Pokemon deck list should preserve all entries") {
        val pokemonList =
            listOf(
                "Pikachu ex",
                "Zapdos ex",
                "Raichu",
                "Electrode",
                "Voltorb",
                "Magneton",
                "Magnemite",
                "Jolteon",
                "Electabuzz",
                "Zebstrika",
            )
        val response =
            StandingResponse(
                placing = 3,
                player =
                    PlayerResponse(
                        name = "Electric Deck Player",
                    ),
                deck =
                    DeckResponse(
                        pokemon = pokemonList,
                    ),
                record =
                    RecordResponse(
                        wins = 8,
                        losses = 1,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.deckPokemon shouldBe pokemonList
        result.deckPokemon.size shouldBe 10
    }

    test("StandingsResponse should contain multiple standings") {
        val standingsResponse =
            StandingsResponse(
                standings =
                    listOf(
                        StandingResponse(
                            placing = 1,
                            player = PlayerResponse(name = "First Place"),
                            record = RecordResponse(wins = 9, losses = 0, ties = 0),
                        ),
                        StandingResponse(
                            placing = 2,
                            player = PlayerResponse(name = "Second Place"),
                            record = RecordResponse(wins = 8, losses = 1, ties = 0),
                        ),
                        StandingResponse(
                            placing = 3,
                            player = PlayerResponse(name = "Third Place"),
                            record = RecordResponse(wins = 7, losses = 2, ties = 0),
                        ),
                    ),
            )

        standingsResponse.standings.size shouldBe 3
        standingsResponse.standings[0].placing shouldBe 1
        standingsResponse.standings[1].placing shouldBe 2
        standingsResponse.standings[2].placing shouldBe 3
    }

    test("StandingsResponse with empty standings list") {
        val standingsResponse =
            StandingsResponse(
                standings = emptyList(),
            )

        standingsResponse.standings shouldBe emptyList()
        standingsResponse.standings.size shouldBe 0
    }

    test("toStanding with very long player name") {
        val longName = "VeryLongPlayerName" + "X".repeat(100)
        val response =
            StandingResponse(
                placing = 20,
                player =
                    PlayerResponse(
                        name = longName,
                    ),
                record =
                    RecordResponse(
                        wins = 4,
                        losses = 4,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.playerName shouldBe longName
    }

    test("toStanding with emoji in player name") {
        val response =
            StandingResponse(
                placing = 12,
                player =
                    PlayerResponse(
                        name = "Player ⚡ Lightning ⚡",
                        country = "USA 🇺🇸",
                    ),
                record =
                    RecordResponse(
                        wins = 5,
                        losses = 3,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.playerName shouldBe "Player ⚡ Lightning ⚡"
        result.country shouldBe "USA 🇺🇸"
    }

    test("toStanding with Pokemon names containing special characters") {
        val response =
            StandingResponse(
                placing = 6,
                player =
                    PlayerResponse(
                        name = "Special Deck User",
                    ),
                deck =
                    DeckResponse(
                        pokemon = listOf("Pikachu ex ⚡", "Type: Null", "Farfetch'd"),
                    ),
                record =
                    RecordResponse(
                        wins = 6,
                        losses = 2,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.deckPokemon shouldBe listOf("Pikachu ex ⚡", "Type: Null", "Farfetch'd")
    }

    test("RecordResponse with all zero values") {
        val record =
            RecordResponse(
                wins = 0,
                losses = 0,
                ties = 0,
            )

        record.wins shouldBe 0
        record.losses shouldBe 0
        record.ties shouldBe 0
    }

    test("RecordResponse with high values") {
        val record =
            RecordResponse(
                wins = 999,
                losses = 888,
                ties = 777,
            )

        record.wins shouldBe 999
        record.losses shouldBe 888
        record.ties shouldBe 777
    }

    test("Standing data class equality works correctly") {
        val standing1 =
            Standing(
                placing = 1,
                playerName = "Player One",
                country = "USA",
                region = "NA",
                deckPokemon = listOf("Pikachu ex"),
                wins = 8,
                losses = 1,
                ties = 0,
            )
        val standing2 =
            Standing(
                placing = 1,
                playerName = "Player One",
                country = "USA",
                region = "NA",
                deckPokemon = listOf("Pikachu ex"),
                wins = 8,
                losses = 1,
                ties = 0,
            )
        val standing3 =
            Standing(
                placing = 2,
                playerName = "Player Two",
                country = "Canada",
                region = "NA",
                deckPokemon = listOf("Mewtwo ex"),
                wins = 7,
                losses = 2,
                ties = 0,
            )

        (standing1 == standing2) shouldBe true
        (standing1 == standing3) shouldBe false
    }

    test("toStanding with only country provided, no region") {
        val response =
            StandingResponse(
                placing = 11,
                player =
                    PlayerResponse(
                        name = "Country Only Player",
                        country = "Australia",
                        region = null,
                    ),
                record =
                    RecordResponse(
                        wins = 5,
                        losses = 4,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.country shouldBe "Australia"
        result.region shouldBe null
    }

    test("toStanding with only region provided, no country") {
        val response =
            StandingResponse(
                placing = 13,
                player =
                    PlayerResponse(
                        name = "Region Only Player",
                        country = null,
                        region = "South America",
                    ),
                record =
                    RecordResponse(
                        wins = 4,
                        losses = 5,
                        ties = 0,
                    ),
            )

        val result = response.toStanding()

        result.country shouldBe null
        result.region shouldBe "South America"
    }

    test("toStanding with single Pokemon in deck") {
        val response =
            StandingResponse(
                placing = 9,
                player =
                    PlayerResponse(
                        name = "Solo Pokemon Player",
                    ),
                deck =
                    DeckResponse(
                        pokemon = listOf("Mewtwo ex"),
                    ),
                record =
                    RecordResponse(
                        wins = 5,
                        losses = 3,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.deckPokemon.size shouldBe 1
        result.deckPokemon[0] shouldBe "Mewtwo ex"
    }

    test("toStanding preserves exact order of Pokemon in deck") {
        val pokemonOrder = listOf("Pikachu ex", "Zapdos ex", "Articuno ex", "Moltres ex")
        val response =
            StandingResponse(
                placing = 4,
                player =
                    PlayerResponse(
                        name = "Order Matters",
                    ),
                deck =
                    DeckResponse(
                        pokemon = pokemonOrder,
                    ),
                record =
                    RecordResponse(
                        wins = 7,
                        losses = 1,
                        ties = 1,
                    ),
            )

        val result = response.toStanding()

        result.deckPokemon shouldBe pokemonOrder
        result.deckPokemon[0] shouldBe "Pikachu ex"
        result.deckPokemon[3] shouldBe "Moltres ex"
    }
})
