package tcg.pocket.dex

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith

class DeckStatsCalculatorTest : BehaviorSpec({

    // Helper function to create DeckStats easily
    fun createDeckStats(
        id: String,
        name: String,
        count: Int,
        wins: Int,
        losses: Int,
        iconUrls: List<String> = emptyList(),
    ): DeckStats =
        DeckStats(
            deckId = id,
            deckName = name,
            count = count,
            totalWins = wins,
            totalLosses = losses,
            iconUrls = iconUrls,
        )

    Given("win rate calculation") {
        When("deck has 50% win rate (10 wins, 10 losses)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 10, 10, 10),
                )

            Then("win rate should be formatted as 50.0%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "50.0%"
            }
        }

        When("deck has 75% win rate (30 wins, 10 losses)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 40, 30, 10),
                )

            Then("win rate should be formatted as 75.0%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "75.0%"
            }
        }

        When("deck has 33.3% win rate (10 wins, 20 losses)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 30, 10, 20),
                )

            Then("win rate should be formatted as 33.3%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "33.3%"
            }
        }

        When("deck has 100% win rate (20 wins, 0 losses)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 20, 20, 0),
                )

            Then("win rate should be formatted as 100.0%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "100.0%"
            }
        }

        When("deck has 0% win rate (0 wins, 20 losses)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 20, 0, 20),
                )

            Then("win rate should be formatted as 0.0%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "0.0%"
            }
        }

        When("deck has no games played (0 wins, 0 losses)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 10, 0, 0),
                )

            Then("win rate should be formatted as 0.0%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "0.0%"
            }
        }

        When("deck has win rate requiring rounding (20 wins, 10 losses = 66.666%)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 30, 20, 10),
                )

            Then("win rate should be rounded to 66.7%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "66.7%"
            }
        }

        When("deck has win rate requiring rounding down (10 wins, 21 losses = 32.258%)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 31, 10, 21),
                )

            Then("win rate should be rounded to 32.3%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].winRate shouldBe "32.3%"
            }
        }
    }

    Given("usage share calculation") {
        When("single deck with 100% usage") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 10, 5, 5),
                )

            Then("usage share should be 100.0%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].usageShare shouldBe "100.0%"
                result[0].appearances shouldBe 10
            }
        }

        When("two decks with equal usage (5 each, total 10)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 5, 3, 2),
                    "deck2" to createDeckStats("deck2", "Deck 2", 5, 4, 1),
                )

            Then("each should have 50.0% usage share") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 2
                result.forEach { deck ->
                    deck.usageShare shouldBe "50.0%"
                    deck.appearances shouldBe 5
                }
            }
        }

        When("deck with one-third usage (10 of 30)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 10, 5, 5),
                    "deck2" to createDeckStats("deck2", "Deck 2", 20, 10, 10),
                )

            Then("usage shares should be 33.3% and 66.7%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 2
                result[0].usageShare shouldBe "66.7%"
                result[0].appearances shouldBe 20
                result[1].usageShare shouldBe "33.3%"
                result[1].appearances shouldBe 10
            }
        }

        When("deck with very low usage (1 of 1000)") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 1, 1, 0),
                    "deck2" to createDeckStats("deck2", "Deck 2", 999, 500, 499),
                )

            Then("usage shares should be 0.1% and 99.9%") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 2
                result[0].usageShare shouldBe "99.9%"
                result[1].usageShare shouldBe "0.1%"
            }
        }

        When("multiple decks with different shares") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 50, 25, 25),
                    "deck2" to createDeckStats("deck2", "Deck 2", 30, 15, 15),
                    "deck3" to createDeckStats("deck3", "Deck 3", 20, 10, 10),
                )

            Then("usage shares should sum to 100% and be correctly calculated") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3
                result[0].usageShare shouldBe "50.0%"
                result[0].appearances shouldBe 50
                result[1].usageShare shouldBe "30.0%"
                result[1].appearances shouldBe 30
                result[2].usageShare shouldBe "20.0%"
                result[2].appearances shouldBe 20
            }
        }
    }

    Given("percentage formatting") {
        When("value requires one decimal place rounding") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 333, 167, 166),
                    "deck2" to createDeckStats("deck2", "Deck 2", 667, 333, 334),
                )

            Then("all percentages should have exactly one decimal place") {
                val result = deckStats.toCalculatedDecks()
                result.forEach { deck ->
                    deck.winRate shouldEndWith "%"
                    deck.usageShare shouldEndWith "%"
                    deck.winRate.count { it == '.' } shouldBe 1
                    deck.usageShare.count { it == '.' } shouldBe 1
                }
            }
        }

        When("value is whole number") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 10, 5, 5),
                )

            Then("should still show .0 decimal") {
                val result = deckStats.toCalculatedDecks()
                result[0].winRate shouldBe "50.0%"
                result[0].usageShare shouldBe "100.0%"
            }
        }

        When("all percentages are calculated") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 10, 5, 5),
                )

            Then("all should include % symbol") {
                val result = deckStats.toCalculatedDecks()
                result[0].winRate shouldEndWith "%"
                result[0].usageShare shouldEndWith "%"
            }
        }
    }

    Given("sorting behavior") {
        When("decks have different usage shares") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Low Usage", 10, 5, 5),
                    "deck2" to createDeckStats("deck2", "High Usage", 50, 25, 25),
                    "deck3" to createDeckStats("deck3", "Medium Usage", 30, 15, 15),
                )

            Then("should sort by usage share descending") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3
                result[0].deckName shouldBe "High Usage"
                result[0].usageShare shouldBe "55.6%"
                result[1].deckName shouldBe "Medium Usage"
                result[1].usageShare shouldBe "33.3%"
                result[2].deckName shouldBe "Low Usage"
                result[2].usageShare shouldBe "11.1%"
            }
        }

        When("decks have same usage share but different win rates") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Low WR", 10, 3, 7),
                    "deck2" to createDeckStats("deck2", "High WR", 10, 7, 3),
                    "deck3" to createDeckStats("deck3", "Medium WR", 10, 5, 5),
                )

            Then("should sort by win rate descending as secondary sort") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3
                result[0].deckName shouldBe "High WR"
                result[0].winRate shouldBe "70.0%"
                result[1].deckName shouldBe "Medium WR"
                result[1].winRate shouldBe "50.0%"
                result[2].deckName shouldBe "Low WR"
                result[2].winRate shouldBe "30.0%"
            }
        }

        When("decks have different usage and win rates combined") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "High Usage Low WR", 50, 15, 35),
                    "deck2" to createDeckStats("deck2", "Low Usage High WR", 10, 9, 1),
                    "deck3" to createDeckStats("deck3", "Medium Usage Medium WR", 30, 15, 15),
                )

            Then("should prioritize usage share over win rate") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3
                result[0].deckName shouldBe "High Usage Low WR"
                result[0].usageShare shouldBe "55.6%"
                result[1].deckName shouldBe "Medium Usage Medium WR"
                result[1].usageShare shouldBe "33.3%"
                result[2].deckName shouldBe "Low Usage High WR"
                result[2].usageShare shouldBe "11.1%"
            }
        }

        When("all decks have same usage and win rate") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck A", 10, 5, 5),
                    "deck2" to createDeckStats("deck2", "Deck B", 10, 5, 5),
                    "deck3" to createDeckStats("deck3", "Deck C", 10, 5, 5),
                )

            Then("order should be stable") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3
                result.forEach { deck ->
                    deck.usageShare shouldBe "33.3%"
                    deck.winRate shouldBe "50.0%"
                }
            }
        }

        When("all decks have same win rate but different usage") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Usage 10", 10, 5, 5),
                    "deck2" to createDeckStats("deck2", "Usage 50", 50, 25, 25),
                    "deck3" to createDeckStats("deck3", "Usage 30", 30, 15, 15),
                )

            Then("should sort only by usage share descending") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3
                result.forEach { it.winRate shouldBe "50.0%" }
                result[0].deckName shouldBe "Usage 50"
                result[1].deckName shouldBe "Usage 30"
                result[2].deckName shouldBe "Usage 10"
            }
        }
    }

    Given("edge cases") {
        When("map is empty") {
            val deckStats = emptyMap<String, DeckStats>()

            Then("should return empty list") {
                val result = deckStats.toCalculatedDecks()
                result.shouldBeEmpty()
            }
        }

        When("single deck in map") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Only Deck", 10, 7, 3),
                )

            Then("should return list with one properly calculated deck") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].deckName shouldBe "Only Deck"
                result[0].winRate shouldBe "70.0%"
                result[0].usageShare shouldBe "100.0%"
                result[0].appearances shouldBe 10
            }
        }

        When("deck has zero count but positive wins/losses") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 0, 5, 5),
                    "deck2" to createDeckStats("deck2", "Deck 2", 10, 5, 5),
                )

            Then("should handle zero count gracefully") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 2
                result[0].appearances shouldBe 10
                result[1].appearances shouldBe 0
            }
        }

        When("all decks have zero count") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Deck 1", 0, 0, 0),
                    "deck2" to createDeckStats("deck2", "Deck 2", 0, 0, 0),
                )

            Then("should return empty list") {
                val result = deckStats.toCalculatedDecks()
                result.shouldBeEmpty()
            }
        }

        When("large dataset with 100+ decks") {
            val deckStats =
                (1..100).associate { i ->
                    "deck$i" to createDeckStats("deck$i", "Deck $i", i, i / 2, i / 2)
                }

            Then("should calculate and sort all decks correctly") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 100

                // Verify descending order by usage share
                val usageShares =
                    result.map {
                        it.usageShare.removeSuffix("%").toDouble()
                    }
                usageShares shouldBe usageShares.sortedDescending()

                // Verify all have percentage format
                result.forEach { deck ->
                    deck.winRate shouldEndWith "%"
                    deck.usageShare shouldEndWith "%"
                }
            }
        }
    }

    Given("real-world tournament scenarios") {
        When("realistic tournament meta with top decks") {
            val deckStats =
                mapOf(
                    "mewtwo_gardevoir" to
                        createDeckStats(
                            "mewtwo_gardevoir",
                            "Mewtwo + Gardevoir",
                            120,
                            75,
                            45,
                        ),
                    "pikachu_ex" to
                        createDeckStats(
                            "pikachu_ex",
                            "Pikachu EX",
                            95,
                            60,
                            35,
                        ),
                    "charizard_moltres" to
                        createDeckStats(
                            "charizard_moltres",
                            "Charizard + Moltres",
                            85,
                            48,
                            37,
                        ),
                    "starmie_ex" to
                        createDeckStats(
                            "starmie_ex",
                            "Starmie EX",
                            50,
                            32,
                            18,
                        ),
                    "meowth_persian" to
                        createDeckStats(
                            "meowth_persian",
                            "Meowth + Persian",
                            30,
                            15,
                            15,
                        ),
                )

            Then("should produce realistic meta snapshot") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 5

                // Most popular deck first
                result[0].deckName shouldBe "Mewtwo + Gardevoir"
                result[0].usageShare shouldBe "31.6%"
                result[0].winRate shouldBe "62.5%"

                // Verify total usage sums to approximately 100% (accounting for rounding)
                val totalUsage =
                    result.sumOf {
                        it.usageShare.removeSuffix("%").toDouble()
                    }
                totalUsage shouldBe (100.0 plusOrMinus 0.5)

                // Verify sorted by usage descending
                result[0].appearances shouldBe 120
                result[1].appearances shouldBe 95
                result[2].appearances shouldBe 85
                result[3].appearances shouldBe 50
                result[4].appearances shouldBe 30
            }
        }

        When("meta dominated by one deck") {
            val deckStats =
                mapOf(
                    "dominant" to createDeckStats("dominant", "Meta King", 200, 140, 60),
                    "tier2_1" to createDeckStats("tier2_1", "Tier 2 Deck A", 30, 15, 15),
                    "tier2_2" to createDeckStats("tier2_2", "Tier 2 Deck B", 20, 10, 10),
                )

            Then("should show clear dominance in usage and win rate") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3

                result[0].deckName shouldBe "Meta King"
                result[0].usageShare shouldBe "80.0%"
                result[0].winRate shouldBe "70.0%"

                result[1].usageShare shouldBe "12.0%"
                result[2].usageShare shouldBe "8.0%"
            }
        }

        When("balanced meta with many viable decks") {
            val deckStats =
                (1..10).associate { i ->
                    "deck$i" to
                        createDeckStats(
                            "deck$i",
                            "Viable Deck $i",
                            count = 50,
                            // Win rates vary 46%-54%
                            wins = 25 + (i % 5) - 2,
                            losses = 25 - (i % 5) + 2,
                        )
                }

            Then("should show balanced usage shares") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 10

                // All should have same usage share
                result.forEach { deck ->
                    deck.usageShare shouldBe "10.0%"
                    deck.appearances shouldBe 50
                }

                // Should be sorted by win rate descending
                val winRates =
                    result.map {
                        it.winRate.removeSuffix("%").toDouble()
                    }
                winRates shouldBe winRates.sortedDescending()
            }
        }

        When("underdog deck with high win rate but low usage") {
            val deckStats =
                mapOf(
                    "popular_mediocre" to
                        createDeckStats(
                            "popular_mediocre",
                            "Popular Mediocre",
                            100,
                            50,
                            50,
                        ),
                    "underdog_strong" to
                        createDeckStats(
                            "underdog_strong",
                            "Hidden Gem",
                            10,
                            9,
                            1,
                        ),
                )

            Then("should prioritize usage in sorting despite higher win rate") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 2

                // Higher usage comes first despite lower win rate
                result[0].deckName shouldBe "Popular Mediocre"
                result[0].usageShare shouldBe "90.9%"
                result[0].winRate shouldBe "50.0%"

                result[1].deckName shouldBe "Hidden Gem"
                result[1].usageShare shouldBe "9.1%"
                result[1].winRate shouldBe "90.0%"
            }
        }
    }

    Given("data integrity verification") {
        When("calculating deck statistics") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Test Deck 1", 25, 15, 10),
                    "deck2" to createDeckStats("deck2", "Test Deck 2", 75, 40, 35),
                )

            Then("deck IDs and names should be preserved") {
                val result = deckStats.toCalculatedDecks()
                result[0].deckId shouldBe "deck2"
                result[0].deckName shouldBe "Test Deck 2"
                result[1].deckId shouldBe "deck1"
                result[1].deckName shouldBe "Test Deck 1"
            }
        }

        When("converting to CalculatedDeck") {
            val deckStats =
                mapOf(
                    "deck1" to createDeckStats("deck1", "Test", 50, 30, 20),
                )

            Then("all fields should be properly populated") {
                val result = deckStats.toCalculatedDecks()
                result[0].deckId shouldBe "deck1"
                result[0].deckName shouldBe "Test"
                result[0].appearances shouldBe 50
                result[0].winRate shouldBe "60.0%"
                result[0].usageShare shouldBe "100.0%"
            }
        }
    }

    Given("iconUrls preservation") {
        When("DeckStats has empty iconUrls") {
            val deckStats =
                mapOf(
                    "deck1" to
                        createDeckStats(
                            "deck1",
                            "Deck Without Icons",
                            10,
                            5,
                            5,
                            iconUrls = emptyList(),
                        ),
                )

            Then("CalculatedDeck should have empty iconUrls") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].iconUrls.shouldBeEmpty()
            }
        }

        When("DeckStats has single iconUrl") {
            val deckStats =
                mapOf(
                    "deck1" to
                        createDeckStats(
                            "deck1",
                            "Pikachu",
                            10,
                            5,
                            5,
                            iconUrls = listOf("Pikachu"),
                        ),
                )

            Then("CalculatedDeck should preserve single iconUrl") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].iconUrls shouldHaveSize 1
                result[0].iconUrls[0] shouldBe "Pikachu"
            }
        }

        When("DeckStats has multiple iconUrls") {
            val deckStats =
                mapOf(
                    "deck1" to
                        createDeckStats(
                            "deck1",
                            "Pikachu + Mewtwo",
                            10,
                            5,
                            5,
                            iconUrls = listOf("Pikachu", "Mewtwo"),
                        ),
                )

            Then("CalculatedDeck should preserve all iconUrls in order") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].iconUrls shouldHaveSize 2
                result[0].iconUrls[0] shouldBe "Pikachu"
                result[0].iconUrls[1] shouldBe "Mewtwo"
            }
        }

        When("DeckStats has three iconUrls") {
            val deckStats =
                mapOf(
                    "deck1" to
                        createDeckStats(
                            "deck1",
                            "Pikachu + Mewtwo + Charizard",
                            10,
                            5,
                            5,
                            iconUrls = listOf("Pikachu", "Mewtwo", "Charizard"),
                        ),
                )

            Then("CalculatedDeck should preserve all three iconUrls in order") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1
                result[0].iconUrls shouldHaveSize 3
                result[0].iconUrls[0] shouldBe "Pikachu"
                result[0].iconUrls[1] shouldBe "Mewtwo"
                result[0].iconUrls[2] shouldBe "Charizard"
            }
        }

        When("multiple decks with different iconUrls") {
            val deckStats =
                mapOf(
                    "deck1" to
                        createDeckStats(
                            "deck1",
                            "Pikachu",
                            50,
                            25,
                            25,
                            iconUrls = listOf("Pikachu"),
                        ),
                    "deck2" to
                        createDeckStats(
                            "deck2",
                            "Mewtwo + Gardevoir",
                            30,
                            20,
                            10,
                            iconUrls = listOf("Mewtwo", "Gardevoir"),
                        ),
                    "deck3" to
                        createDeckStats(
                            "deck3",
                            "No Icons",
                            20,
                            10,
                            10,
                            iconUrls = emptyList(),
                        ),
                )

            Then("each CalculatedDeck should have correct iconUrls") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 3

                val pikachuDeck = result.find { it.deckId == "deck1" }!!
                pikachuDeck.iconUrls shouldHaveSize 1
                pikachuDeck.iconUrls[0] shouldBe "Pikachu"

                val mewtwoDeck = result.find { it.deckId == "deck2" }!!
                mewtwoDeck.iconUrls shouldHaveSize 2
                mewtwoDeck.iconUrls[0] shouldBe "Mewtwo"
                mewtwoDeck.iconUrls[1] shouldBe "Gardevoir"

                val noIconsDeck = result.find { it.deckId == "deck3" }!!
                noIconsDeck.iconUrls.shouldBeEmpty()
            }
        }

        When("iconUrls are preserved through sorting") {
            val deckStats =
                mapOf(
                    "deck1" to
                        createDeckStats(
                            "deck1",
                            "Low Usage",
                            10,
                            5,
                            5,
                            iconUrls = listOf("Pokemon A"),
                        ),
                    "deck2" to
                        createDeckStats(
                            "deck2",
                            "High Usage",
                            50,
                            25,
                            25,
                            iconUrls = listOf("Pokemon B", "Pokemon C"),
                        ),
                )

            Then("iconUrls should remain correct after sorting by usage") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 2

                result[0].deckName shouldBe "High Usage"
                result[0].iconUrls shouldHaveSize 2
                result[0].iconUrls[0] shouldBe "Pokemon B"
                result[0].iconUrls[1] shouldBe "Pokemon C"

                result[1].deckName shouldBe "Low Usage"
                result[1].iconUrls shouldHaveSize 1
                result[1].iconUrls[0] shouldBe "Pokemon A"
            }
        }

        When("iconUrls with real Pokemon names") {
            val deckStats =
                mapOf(
                    "mewtwo_gardevoir" to
                        createDeckStats(
                            "mewtwo_gardevoir",
                            "Mewtwo ex + Gardevoir",
                            100,
                            70,
                            30,
                            iconUrls = listOf("Mewtwo ex", "Gardevoir"),
                        ),
                    "pikachu_ex" to
                        createDeckStats(
                            "pikachu_ex",
                            "Pikachu ex",
                            80,
                            55,
                            25,
                            iconUrls = listOf("Pikachu ex"),
                        ),
                )

            Then("iconUrls should be preserved with real Pokemon names") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 2

                val mewtwoDeck = result.find { it.deckId == "mewtwo_gardevoir" }!!
                mewtwoDeck.iconUrls shouldHaveSize 2
                mewtwoDeck.iconUrls[0] shouldBe "Mewtwo ex"
                mewtwoDeck.iconUrls[1] shouldBe "Gardevoir"

                val pikachuDeck = result.find { it.deckId == "pikachu_ex" }!!
                pikachuDeck.iconUrls shouldHaveSize 1
                pikachuDeck.iconUrls[0] shouldBe "Pikachu ex"
            }
        }
    }

    Given("empty map with iconUrls field") {
        When("converting empty map") {
            val deckStats = emptyMap<String, DeckStats>()

            Then("should return empty list") {
                val result = deckStats.toCalculatedDecks()
                result.shouldBeEmpty()
            }
        }
    }

    Given("iconUrls field integration with all other fields") {
        When("converting DeckStats with all fields populated") {
            val deckStats =
                mapOf(
                    "deck1" to
                        createDeckStats(
                            id = "Pikachu|Mewtwo",
                            name = "Pikachu + Mewtwo",
                            count = 50,
                            wins = 30,
                            losses = 20,
                            iconUrls = listOf("Pikachu", "Mewtwo"),
                        ),
                )

            Then("all fields including iconUrls should be correctly populated") {
                val result = deckStats.toCalculatedDecks()
                result shouldHaveSize 1

                with(result[0]) {
                    deckId shouldBe "Pikachu|Mewtwo"
                    deckName shouldBe "Pikachu + Mewtwo"
                    winRate shouldBe "60.0%"
                    usageShare shouldBe "100.0%"
                    appearances shouldBe 50
                    iconUrls shouldHaveSize 2
                    iconUrls[0] shouldBe "Pikachu"
                    iconUrls[1] shouldBe "Mewtwo"
                }
            }
        }
    }
})
