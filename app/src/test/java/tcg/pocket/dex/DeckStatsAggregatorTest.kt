package tcg.pocket.dex

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.shouldBe
import tcg.pocket.dex.remote.response.Standing
import io.kotest.matchers.maps.shouldBeEmpty as mapsShouldBeEmpty
import io.kotest.matchers.maps.shouldHaveSize as mapsShouldHaveSize

/**
 * Comprehensive test suite for DeckStatsAggregator.
 * Tests aggregation logic, deck identification, win/loss accumulation, and edge cases.
 */
class DeckStatsAggregatorTest : BehaviorSpec({

    // ============================================================================
    // Test Data Builders
    // ============================================================================

    fun createStanding(
        placing: Int = 1,
        playerName: String = "Player",
        deckPokemon: List<String> = listOf("Pikachu"),
        wins: Int = 5,
        losses: Int = 2,
        ties: Int = 0,
        country: String? = null,
        region: String? = null,
    ): Standing =
        Standing(
            placing = placing,
            playerName = playerName,
            country = country,
            region = region,
            deckPokemon = deckPokemon,
            wins = wins,
            losses = losses,
            ties = ties,
        )

    // ============================================================================
    // Basic Aggregation Tests
    // ============================================================================

    Given("a list of standings with a single deck") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 5, losses = 2),
            )

        When("aggregating the standings") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should create a single DeckStats entry") {
                result.mapsShouldHaveSize(1)
            }

            Then("should have the correct deckId") {
                result.keys.first() shouldBe "Pikachu"
            }

            Then("should have the correct deckName") {
                result.values.first().deckName shouldBe "Pikachu"
            }

            Then("should have count of 1") {
                result.values.first().count shouldBe 1
            }

            Then("should have correct wins") {
                result.values.first().totalWins shouldBe 5
            }

            Then("should have correct losses") {
                result.values.first().totalLosses shouldBe 2
            }

            Then("should calculate correct win rate") {
                result.values.first().winRate shouldBeExactly (5.0 / 7.0)
            }
        }
    }

    Given("multiple standings with the same deck") {
        val standings =
            listOf(
                createStanding(
                    playerName = "Player1",
                    deckPokemon = listOf("Pikachu", "Mewtwo"),
                    wins = 5,
                    losses = 2,
                ),
                createStanding(
                    playerName = "Player2",
                    deckPokemon = listOf("Mewtwo", "Pikachu"),
                    wins = 3,
                    losses = 4,
                ),
                createStanding(
                    playerName = "Player3",
                    deckPokemon = listOf("Pikachu", "Mewtwo"),
                    wins = 6,
                    losses = 1,
                ),
            )

        When("aggregating the standings") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should create a single DeckStats entry") {
                result.mapsShouldHaveSize(1)
            }

            Then("should have count of 3") {
                result.values.first().count shouldBe 3
            }

            Then("should sum total wins correctly") {
                result.values.first().totalWins shouldBe 14
            }

            Then("should sum total losses correctly") {
                result.values.first().totalLosses shouldBe 7
            }

            Then("deckId should be sorted") {
                result.keys.first() shouldBe "Mewtwo|Pikachu"
            }

            Then("deckName should be sorted with + separator") {
                result.values.first().deckName shouldBe "Mewtwo + Pikachu"
            }
        }
    }

    Given("multiple standings with different decks") {
        val standings =
            listOf(
                createStanding(
                    playerName = "Player1",
                    deckPokemon = listOf("Pikachu", "Raichu"),
                    wins = 5,
                    losses = 2,
                ),
                createStanding(
                    playerName = "Player2",
                    deckPokemon = listOf("Mewtwo", "Alakazam"),
                    wins = 3,
                    losses = 4,
                ),
                createStanding(
                    playerName = "Player3",
                    deckPokemon = listOf("Charizard"),
                    wins = 6,
                    losses = 1,
                ),
            )

        When("aggregating the standings") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should create three DeckStats entries") {
                result.mapsShouldHaveSize(3)
            }

            Then("all decks should have count of 1") {
                result.values.all { it.count == 1 } shouldBe true
            }

            Then("should have correct deckIds") {
                result.keys shouldContainExactly
                    setOf(
                        "Pikachu|Raichu",
                        "Alakazam|Mewtwo",
                        "Charizard",
                    )
            }

            Then("Pikachu + Raichu deck should have correct stats") {
                val pikachuStats = result["Pikachu|Raichu"]!!
                pikachuStats.deckName shouldBe "Pikachu + Raichu"
                pikachuStats.totalWins shouldBe 5
                pikachuStats.totalLosses shouldBe 2
            }

            Then("Mewtwo + Alakazam deck should have correct stats") {
                val mewtwoStats = result["Alakazam|Mewtwo"]!!
                mewtwoStats.deckName shouldBe "Alakazam + Mewtwo"
                mewtwoStats.totalWins shouldBe 3
                mewtwoStats.totalLosses shouldBe 4
            }

            Then("Charizard deck should have correct stats") {
                val charizardStats = result["Charizard"]!!
                charizardStats.deckName shouldBe "Charizard"
                charizardStats.totalWins shouldBe 6
                charizardStats.totalLosses shouldBe 1
            }
        }
    }

    // ============================================================================
    // Deck Identification Tests
    // ============================================================================

    Given("standings with same Pokemon in different order") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo", "Charizard"), wins = 5),
                createStanding(deckPokemon = listOf("Charizard", "Pikachu", "Mewtwo"), wins = 3),
                createStanding(deckPokemon = listOf("Mewtwo", "Charizard", "Pikachu"), wins = 4),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should recognize as same deck") {
                result.mapsShouldHaveSize(1)
            }

            Then("deckId should be consistently sorted") {
                result.keys.first() shouldBe "Charizard|Mewtwo|Pikachu"
            }

            Then("deckName should be consistently sorted") {
                result.values.first().deckName shouldBe "Charizard + Mewtwo + Pikachu"
            }

            Then("should aggregate all three standings") {
                result.values.first().count shouldBe 3
                result.values.first().totalWins shouldBe 12
            }
        }
    }

    Given("standings with different Pokemon") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo"), wins = 5),
                createStanding(deckPokemon = listOf("Pikachu", "Charizard"), wins = 3),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should create different deckIds") {
                result.mapsShouldHaveSize(2)
                result.keys shouldContainExactly setOf("Mewtwo|Pikachu", "Charizard|Pikachu")
            }
        }
    }

    Given("deck with single Pokemon") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 5, losses = 2),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("deckId should not have separator") {
                result.keys.first() shouldBe "Pikachu"
            }

            Then("deckName should not have separator") {
                result.values.first().deckName shouldBe "Pikachu"
            }
        }
    }

    Given("deck with many Pokemon") {
        val standings =
            listOf(
                createStanding(
                    deckPokemon = listOf("Pikachu", "Raichu", "Mewtwo", "Alakazam", "Charizard"),
                    wins = 5,
                    losses = 2,
                ),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("deckId should be properly formatted with all Pokemon sorted") {
                result.keys.first() shouldBe "Alakazam|Charizard|Mewtwo|Pikachu|Raichu"
            }

            Then("deckName should be properly formatted with all Pokemon sorted") {
                result.values.first().deckName shouldBe "Alakazam + Charizard + Mewtwo + Pikachu + Raichu"
            }
        }
    }

    // ============================================================================
    // Win/Loss Accumulation Tests
    // ============================================================================

    Given("standings with various win/loss combinations") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 10, losses = 2),
                createStanding(deckPokemon = listOf("Pikachu"), wins = 8, losses = 4),
                createStanding(deckPokemon = listOf("Pikachu"), wins = 0, losses = 7),
                createStanding(deckPokemon = listOf("Pikachu"), wins = 5, losses = 0),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("count should equal number of standings") {
                result.values.first().count shouldBe 4
            }

            Then("totalWins should be sum of all wins") {
                result.values.first().totalWins shouldBe 23
            }

            Then("totalLosses should be sum of all losses") {
                result.values.first().totalLosses shouldBe 13
            }

            Then("win rate should be correctly calculated") {
                result.values.first().winRate shouldBeExactly (23.0 / 36.0)
            }
        }
    }

    Given("standings with zero wins and losses") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 0, losses = 0),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should handle zero games correctly") {
                result.values.first().totalWins shouldBe 0
                result.values.first().totalLosses shouldBe 0
                result.values.first().winRate shouldBeExactly 0.0
            }
        }
    }

    Given("standings with very high wins") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 100, losses = 5),
                createStanding(deckPokemon = listOf("Pikachu"), wins = 150, losses = 10),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should correctly accumulate large values") {
                result.values.first().totalWins shouldBe 250
                result.values.first().totalLosses shouldBe 15
            }
        }
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    Given("empty standings list") {
        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(emptyList())

            Then("should return empty map") {
                result.mapsShouldBeEmpty()
            }
        }
    }

    Given("standings with empty deckPokemon") {
        val standings =
            listOf(
                createStanding(deckPokemon = emptyList(), wins = 5, losses = 2),
                createStanding(deckPokemon = listOf("Pikachu"), wins = 3, losses = 4),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should filter out empty deckPokemon") {
                result.mapsShouldHaveSize(1)
                result.keys.first() shouldBe "Pikachu"
            }
        }
    }

    Given("multiple standings with empty deckPokemon") {
        val standings =
            listOf(
                createStanding(deckPokemon = emptyList(), wins = 5, losses = 2),
                createStanding(deckPokemon = emptyList(), wins = 3, losses = 4),
                createStanding(deckPokemon = emptyList(), wins = 6, losses = 1),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should return empty map") {
                result.mapsShouldBeEmpty()
            }
        }
    }

    Given("duplicate standings (same deck, same player)") {
        val standings =
            listOf(
                createStanding(playerName = "Player1", deckPokemon = listOf("Pikachu"), wins = 5, losses = 2),
                createStanding(playerName = "Player1", deckPokemon = listOf("Pikachu"), wins = 5, losses = 2),
                createStanding(playerName = "Player1", deckPokemon = listOf("Pikachu"), wins = 5, losses = 2),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should still aggregate all standings") {
                result.mapsShouldHaveSize(1)
                result.values.first().count shouldBe 3
                result.values.first().totalWins shouldBe 15
                result.values.first().totalLosses shouldBe 6
            }
        }
    }

    // ============================================================================
    // Boundary Values
    // ============================================================================

    Given("large dataset with 50+ standings") {
        val standings =
            (1..50).map { i ->
                createStanding(
                    playerName = "Player$i",
                    deckPokemon = listOf("Pikachu", "Mewtwo"),
                    wins = i,
                    losses = 50 - i,
                )
            }

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should create single deck entry") {
                result.mapsShouldHaveSize(1)
            }

            Then("should have count of 50") {
                result.values.first().count shouldBe 50
            }

            Then("should correctly sum all wins") {
                // Sum of 1 to 50 = 50 * 51 / 2 = 1275
                result.values.first().totalWins shouldBe 1275
            }

            Then("should correctly sum all losses") {
                // Sum of 49 to 0 = 50 * 49 / 2 = 1225
                result.values.first().totalLosses shouldBe 1225
            }
        }
    }

    Given("mixed deck sizes in large dataset") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 5),
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo"), wins = 4),
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo", "Charizard"), wins = 3),
                createStanding(deckPokemon = listOf("Pikachu"), wins = 6),
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo"), wins = 7),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should create three distinct deck entries") {
                result.mapsShouldHaveSize(3)
            }

            Then("Pikachu deck should have 2 standings") {
                result["Pikachu"]!!.count shouldBe 2
                result["Pikachu"]!!.totalWins shouldBe 11
            }

            Then("Pikachu + Mewtwo deck should have 2 standings") {
                result["Mewtwo|Pikachu"]!!.count shouldBe 2
                result["Mewtwo|Pikachu"]!!.totalWins shouldBe 11
            }

            Then("Pikachu + Mewtwo + Charizard deck should have 1 standing") {
                result["Charizard|Mewtwo|Pikachu"]!!.count shouldBe 1
                result["Charizard|Mewtwo|Pikachu"]!!.totalWins shouldBe 3
            }
        }
    }

    // ============================================================================
    // Null Safety Tests
    // ============================================================================

    Given("standings with null country and region") {
        val standings =
            listOf(
                createStanding(
                    deckPokemon = listOf("Pikachu"),
                    wins = 5,
                    losses = 2,
                    country = null,
                    region = null,
                ),
                createStanding(
                    deckPokemon = listOf("Pikachu"),
                    wins = 3,
                    losses = 4,
                    country = "USA",
                    region = "NA",
                ),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should not affect aggregation") {
                result.mapsShouldHaveSize(1)
                result.values.first().count shouldBe 2
                result.values.first().totalWins shouldBe 8
                result.values.first().totalLosses shouldBe 6
            }
        }
    }

    Given("mixed standings with various null values") {
        val standings =
            listOf(
                createStanding(
                    deckPokemon = listOf("Pikachu", "Mewtwo"),
                    wins = 5,
                    losses = 2,
                    country = "USA",
                    region = "NA",
                ),
                createStanding(
                    deckPokemon = listOf("Mewtwo", "Pikachu"),
                    wins = 3,
                    losses = 4,
                    country = null,
                    region = "EU",
                ),
                createStanding(
                    deckPokemon = listOf("Pikachu", "Mewtwo"),
                    wins = 6,
                    losses = 1,
                    country = "Japan",
                    region = null,
                ),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should aggregate correctly regardless of null values") {
                result.mapsShouldHaveSize(1)
                result.values.first().count shouldBe 3
                result.values.first().totalWins shouldBe 14
                result.values.first().totalLosses shouldBe 7
            }
        }
    }

    // ============================================================================
    // Complex Scenario Tests
    // ============================================================================

    Given("real-world tournament scenario with multiple decks and players") {
        val standings =
            listOf(
                // Pikachu ex + Mewtwo ex deck (3 players)
                createStanding(
                    placing = 1,
                    playerName = "Player1",
                    deckPokemon = listOf("Pikachu ex", "Mewtwo ex"),
                    wins = 6,
                    losses = 1,
                ),
                createStanding(
                    placing = 3,
                    playerName = "Player3",
                    deckPokemon = listOf("Mewtwo ex", "Pikachu ex"),
                    wins = 5,
                    losses = 2,
                ),
                createStanding(
                    placing = 5,
                    playerName = "Player5",
                    deckPokemon = listOf("Pikachu ex", "Mewtwo ex"),
                    wins = 4,
                    losses = 3,
                ),
                // Charizard ex deck (2 players)
                createStanding(
                    placing = 2,
                    playerName = "Player2",
                    deckPokemon = listOf("Charizard ex"),
                    wins = 5,
                    losses = 2,
                ),
                createStanding(
                    placing = 4,
                    playerName = "Player4",
                    deckPokemon = listOf("Charizard ex"),
                    wins = 4,
                    losses = 3,
                ),
                // Articuno ex + Starmie ex deck (1 player)
                createStanding(
                    placing = 6,
                    playerName = "Player6",
                    deckPokemon = listOf("Articuno ex", "Starmie ex"),
                    wins = 3,
                    losses = 4,
                ),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should create three deck entries") {
                result.mapsShouldHaveSize(3)
            }

            Then("Pikachu ex + Mewtwo ex should be most popular") {
                val pikachuMewtwo = result["Mewtwo ex|Pikachu ex"]!!
                pikachuMewtwo.count shouldBe 3
                pikachuMewtwo.totalWins shouldBe 15
                pikachuMewtwo.totalLosses shouldBe 6
                pikachuMewtwo.deckName shouldBe "Mewtwo ex + Pikachu ex"
            }

            Then("Charizard ex should have second most appearances") {
                val charizard = result["Charizard ex"]!!
                charizard.count shouldBe 2
                charizard.totalWins shouldBe 9
                charizard.totalLosses shouldBe 5
                charizard.deckName shouldBe "Charizard ex"
            }

            Then("Articuno ex + Starmie ex should have single appearance") {
                val articunoStarmie = result["Articuno ex|Starmie ex"]!!
                articunoStarmie.count shouldBe 1
                articunoStarmie.totalWins shouldBe 3
                articunoStarmie.totalLosses shouldBe 4
                articunoStarmie.deckName shouldBe "Articuno ex + Starmie ex"
            }
        }
    }

    Given("standings with ties value (should not affect aggregation)") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 5, losses = 2, ties = 1),
                createStanding(deckPokemon = listOf("Pikachu"), wins = 3, losses = 4, ties = 2),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should aggregate normally (ties are ignored in DeckStats)") {
                result.mapsShouldHaveSize(1)
                result.values.first().count shouldBe 2
                result.values.first().totalWins shouldBe 8
                result.values.first().totalLosses shouldBe 6
            }
        }
    }

    // ============================================================================
    // Icon URL Capture Tests
    // ============================================================================

    Given("single standing with deckPokemon") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo"), wins = 5, losses = 2),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("iconUrls should be captured from first standing's deckPokemon") {
                result.mapsShouldHaveSize(1)
                val deckStats = result.values.first()
                deckStats.iconUrls shouldContainExactly listOf("Pikachu", "Mewtwo")
            }
        }
    }

    Given("multiple standings with same deck but different deckPokemon order") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo"), wins = 5, losses = 2),
                createStanding(deckPokemon = listOf("Mewtwo", "Pikachu"), wins = 3, losses = 4),
                createStanding(deckPokemon = listOf("Pikachu", "Mewtwo"), wins = 4, losses = 3),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("iconUrls should be captured from first standing only") {
                result.mapsShouldHaveSize(1)
                val deckStats = result.values.first()
                deckStats.iconUrls shouldContainExactly listOf("Pikachu", "Mewtwo")
            }
        }
    }

    Given("standings with empty deckPokemon") {
        val standings =
            listOf(
                createStanding(deckPokemon = emptyList(), wins = 5, losses = 2),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("should filter out standings with empty deckPokemon") {
                result.mapsShouldBeEmpty()
            }
        }
    }

    Given("multiple decks with different deckPokemon") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu", "Raichu"), wins = 5, losses = 2),
                createStanding(deckPokemon = listOf("Mewtwo", "Alakazam"), wins = 3, losses = 4),
                createStanding(deckPokemon = listOf("Charizard"), wins = 6, losses = 1),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("each deck should have correct iconUrls") {
                result.mapsShouldHaveSize(3)

                val pikachuDeck = result["Pikachu|Raichu"]!!
                pikachuDeck.iconUrls shouldContainExactly listOf("Pikachu", "Raichu")

                val mewtwoDeck = result["Alakazam|Mewtwo"]!!
                mewtwoDeck.iconUrls shouldContainExactly listOf("Mewtwo", "Alakazam")

                val charizardDeck = result["Charizard"]!!
                charizardDeck.iconUrls shouldContainExactly listOf("Charizard")
            }
        }
    }

    Given("single Pokemon deck") {
        val standings =
            listOf(
                createStanding(deckPokemon = listOf("Pikachu"), wins = 5, losses = 2),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("iconUrls should contain single Pokemon") {
                result.mapsShouldHaveSize(1)
                val deckStats = result.values.first()
                deckStats.iconUrls shouldContainExactly listOf("Pikachu")
            }
        }
    }

    Given("three Pokemon deck") {
        val standings =
            listOf(
                createStanding(
                    deckPokemon = listOf("Pikachu", "Mewtwo", "Charizard"),
                    wins = 5,
                    losses = 2,
                ),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("iconUrls should contain all three Pokemon") {
                result.mapsShouldHaveSize(1)
                val deckStats = result.values.first()
                deckStats.iconUrls shouldContainExactly listOf("Pikachu", "Mewtwo", "Charizard")
            }
        }
    }

    Given("multiple standings with same deck, first standing used for iconUrls") {
        val standings =
            listOf(
                createStanding(
                    playerName = "Player1",
                    deckPokemon = listOf("Pikachu ex", "Zapdos ex"),
                    wins = 6,
                    losses = 1,
                ),
                createStanding(
                    playerName = "Player2",
                    deckPokemon = listOf("Zapdos ex", "Pikachu ex"),
                    wins = 5,
                    losses = 2,
                ),
                createStanding(
                    playerName = "Player3",
                    deckPokemon = listOf("Pikachu ex", "Zapdos ex"),
                    wins = 4,
                    losses = 3,
                ),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("iconUrls should match first standing's deckPokemon order") {
                result.mapsShouldHaveSize(1)
                val deckStats = result.values.first()
                deckStats.iconUrls shouldContainExactly listOf("Pikachu ex", "Zapdos ex")
            }
        }
    }

    Given("real-world scenario with mixed deck sizes and icon URLs") {
        val standings =
            listOf(
                createStanding(
                    deckPokemon = listOf("Mewtwo ex", "Gardevoir"),
                    wins = 6,
                    losses = 1,
                ),
                createStanding(
                    deckPokemon = listOf("Charizard ex"),
                    wins = 5,
                    losses = 2,
                ),
                createStanding(
                    deckPokemon = listOf("Pikachu ex", "Zapdos ex", "Magneton"),
                    wins = 4,
                    losses = 3,
                ),
            )

        When("aggregating") {
            val result = DeckStatsAggregator.aggregate(standings)

            Then("each deck should preserve iconUrls correctly") {
                result.mapsShouldHaveSize(3)

                val mewtwoDeck = result["Gardevoir|Mewtwo ex"]!!
                mewtwoDeck.iconUrls shouldContainExactly listOf("Mewtwo ex", "Gardevoir")

                val charizardDeck = result["Charizard ex"]!!
                charizardDeck.iconUrls shouldContainExactly listOf("Charizard ex")

                val pikachuDeck = result["Magneton|Pikachu ex|Zapdos ex"]!!
                pikachuDeck.iconUrls shouldContainExactly listOf("Pikachu ex", "Zapdos ex", "Magneton")
            }
        }
    }
})
