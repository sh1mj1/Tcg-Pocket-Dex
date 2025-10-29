package tcg.pocket.dex

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DeckStatsTest : BehaviorSpec({
    Given("a DeckStats with valid positive values") {
        val deckStats =
            DeckStats(
                deckId = "mewtwo|pikachu",
                deckName = "Mewtwo + Pikachu",
                count = 10,
                totalWins = 7,
                totalLosses = 3,
            )

        When("creating with positive count, wins, and losses") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "mewtwo|pikachu",
                        deckName = "Mewtwo + Pikachu",
                        count = 10,
                        totalWins = 7,
                        totalLosses = 3,
                    )
                }
            }

            Then("should have correct property values") {
                deckStats.deckId shouldBe "mewtwo|pikachu"
                deckStats.deckName shouldBe "Mewtwo + Pikachu"
                deckStats.count shouldBe 10
                deckStats.totalWins shouldBe 7
                deckStats.totalLosses shouldBe 3
            }

            Then("should calculate correct win rate") {
                deckStats.winRate shouldBe 0.7
            }
        }
    }

    Given("a DeckStats with zero values") {
        When("count is zero") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "pikachu",
                        deckName = "Pikachu",
                        count = 0,
                        totalWins = 5,
                        totalLosses = 5,
                    )
                }
            }
        }

        When("totalWins is zero") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "mewtwo",
                        deckName = "Mewtwo",
                        count = 3,
                        totalWins = 0,
                        totalLosses = 10,
                    )
                }
            }
        }

        When("totalLosses is zero") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "charizard",
                        deckName = "Charizard",
                        count = 5,
                        totalWins = 10,
                        totalLosses = 0,
                    )
                }
            }
        }

        When("all values are zero") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "blastoise",
                        deckName = "Blastoise",
                        count = 0,
                        totalWins = 0,
                        totalLosses = 0,
                    )
                }
            }
        }
    }

    Given("a DeckStats with large values") {
        val largeStats =
            DeckStats(
                deckId = "popular|deck",
                deckName = "Popular Deck",
                count = 1000,
                totalWins = 5000,
                totalLosses = 3000,
            )

        When("creating with large values") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "popular|deck",
                        deckName = "Popular Deck",
                        count = 1000,
                        totalWins = 5000,
                        totalLosses = 3000,
                    )
                }
            }

            Then("should calculate correct win rate") {
                largeStats.winRate shouldBe 0.625
            }
        }
    }

    Given("DeckStats with invalid count value") {
        When("count is negative") {
            Then("should throw IllegalArgumentException") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        DeckStats(
                            deckId = "mewtwo",
                            deckName = "Mewtwo",
                            count = -1,
                            totalWins = 5,
                            totalLosses = 3,
                        )
                    }
                exception.message shouldBe "count must be non-negative, but was -1"
            }
        }

        When("count is very negative") {
            Then("should throw IllegalArgumentException") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        DeckStats(
                            deckId = "pikachu",
                            deckName = "Pikachu",
                            count = -999,
                            totalWins = 0,
                            totalLosses = 0,
                        )
                    }
                exception.message shouldBe "count must be non-negative, but was -999"
            }
        }
    }

    Given("DeckStats with invalid totalWins value") {
        When("totalWins is negative") {
            Then("should throw IllegalArgumentException") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        DeckStats(
                            deckId = "mewtwo",
                            deckName = "Mewtwo",
                            count = 5,
                            totalWins = -1,
                            totalLosses = 3,
                        )
                    }
                exception.message shouldBe "totalWins must be non-negative, but was -1"
            }
        }

        When("totalWins is very negative") {
            Then("should throw IllegalArgumentException") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        DeckStats(
                            deckId = "charizard",
                            deckName = "Charizard",
                            count = 10,
                            totalWins = -100,
                            totalLosses = 50,
                        )
                    }
                exception.message shouldBe "totalWins must be non-negative, but was -100"
            }
        }
    }

    Given("DeckStats with invalid totalLosses value") {
        When("totalLosses is negative") {
            Then("should throw IllegalArgumentException") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        DeckStats(
                            deckId = "mewtwo",
                            deckName = "Mewtwo",
                            count = 5,
                            totalWins = 7,
                            totalLosses = -1,
                        )
                    }
                exception.message shouldBe "totalLosses must be non-negative, but was -1"
            }
        }

        When("totalLosses is very negative") {
            Then("should throw IllegalArgumentException") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        DeckStats(
                            deckId = "blastoise",
                            deckName = "Blastoise",
                            count = 8,
                            totalWins = 20,
                            totalLosses = -50,
                        )
                    }
                exception.message shouldBe "totalLosses must be non-negative, but was -50"
            }
        }
    }

    Given("DeckStats with multiple invalid values") {
        When("count and totalWins are negative") {
            Then("should throw IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    DeckStats(
                        deckId = "mewtwo",
                        deckName = "Mewtwo",
                        count = -1,
                        totalWins = -5,
                        totalLosses = 3,
                    )
                }
            }
        }

        When("count and totalLosses are negative") {
            Then("should throw IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    DeckStats(
                        deckId = "pikachu",
                        deckName = "Pikachu",
                        count = -2,
                        totalWins = 7,
                        totalLosses = -3,
                    )
                }
            }
        }

        When("totalWins and totalLosses are negative") {
            Then("should throw IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    DeckStats(
                        deckId = "charizard",
                        deckName = "Charizard",
                        count = 10,
                        totalWins = -5,
                        totalLosses = -8,
                    )
                }
            }
        }

        When("all values are negative") {
            Then("should throw IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    DeckStats(
                        deckId = "blastoise",
                        deckName = "Blastoise",
                        count = -1,
                        totalWins = -1,
                        totalLosses = -1,
                    )
                }
            }
        }
    }

    Given("calculating winRate") {
        When("there are both wins and losses") {
            val stats =
                DeckStats(
                    deckId = "test",
                    deckName = "Test",
                    count = 1,
                    totalWins = 7,
                    totalLosses = 3,
                )

            Then("should calculate correct win rate") {
                stats.winRate shouldBe 0.7
            }
        }

        When("there are only wins") {
            val stats =
                DeckStats(
                    deckId = "test",
                    deckName = "Test",
                    count = 1,
                    totalWins = 10,
                    totalLosses = 0,
                )

            Then("should return 1.0 win rate") {
                stats.winRate shouldBe 1.0
            }
        }

        When("there are only losses") {
            val stats =
                DeckStats(
                    deckId = "test",
                    deckName = "Test",
                    count = 1,
                    totalWins = 0,
                    totalLosses = 10,
                )

            Then("should return 0.0 win rate") {
                stats.winRate shouldBe 0.0
            }
        }

        When("there are no games played") {
            val stats =
                DeckStats(
                    deckId = "test",
                    deckName = "Test",
                    count = 0,
                    totalWins = 0,
                    totalLosses = 0,
                )

            Then("should return 0.0 to avoid division by zero") {
                stats.winRate shouldBe 0.0
            }
        }

        When("win rate has decimal precision") {
            val stats =
                DeckStats(
                    deckId = "test",
                    deckName = "Test",
                    count = 1,
                    totalWins = 2,
                    totalLosses = 3,
                )

            Then("should calculate precise win rate") {
                stats.winRate shouldBe 0.4
            }
        }

        When("win rate requires rounding") {
            val stats =
                DeckStats(
                    deckId = "test",
                    deckName = "Test",
                    count = 1,
                    totalWins = 1,
                    totalLosses = 3,
                )

            Then("should calculate precise win rate") {
                stats.winRate shouldBe 0.25
            }
        }
    }

    Given("DeckStats with edge case strings") {
        When("deckId and deckName are empty") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "",
                        deckName = "",
                        count = 5,
                        totalWins = 3,
                        totalLosses = 2,
                    )
                }
            }
        }

        When("deckName is very long") {
            val longName = "A".repeat(1000)
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "long",
                        deckName = longName,
                        count = 1,
                        totalWins = 1,
                        totalLosses = 1,
                    )
                }
            }
        }

        When("deckId contains special characters") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "mewtwo|pikachu-ex+special@#$%",
                        deckName = "Special Characters!",
                        count = 3,
                        totalWins = 2,
                        totalLosses = 1,
                    )
                }
            }
        }

        When("deckName contains unicode characters") {
            Then("should create successfully") {
                shouldNotThrow<Exception> {
                    DeckStats(
                        deckId = "unicode",
                        deckName = "ピカチュウ + ミュウツー",
                        count = 7,
                        totalWins = 4,
                        totalLosses = 3,
                    )
                }
            }
        }
    }
})
