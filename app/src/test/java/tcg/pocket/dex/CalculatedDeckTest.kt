package tcg.pocket.dex

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain

class CalculatedDeckTest : BehaviorSpec({
    Given("Valid CalculatedDeck parameters") {
        val deckId = "pikachu-mewtwo-v1"
        val deckName = "Pikachu + Mewtwo"
        val winRate = "50.5%"
        val usageShare = "25.0%"
        val appearances = 100

        When("Creating a CalculatedDeck with all valid fields") {
            val deck =
                CalculatedDeck(
                    deckId = deckId,
                    deckName = deckName,
                    winRate = winRate,
                    usageShare = usageShare,
                    appearances = appearances,
                )

            Then("All properties should be accessible") {
                deck.deckId shouldBe deckId
                deck.deckName shouldBe deckName
                deck.winRate shouldBe winRate
                deck.usageShare shouldBe usageShare
                deck.appearances shouldBe appearances
            }

            Then("Should be a valid object") {
                shouldNotThrow<Exception> {
                    deck.toString()
                }
            }
        }

        When("Creating a CalculatedDeck with zero appearances") {
            Then("Should succeed without exception") {
                shouldNotThrow<IllegalArgumentException> {
                    CalculatedDeck(
                        deckId = "test-deck",
                        deckName = "Test Deck",
                        winRate = "0.0%",
                        usageShare = "0.0%",
                        appearances = 0,
                    )
                }
            }
        }

        When("Creating a CalculatedDeck with large appearance numbers") {
            Then("Should handle 1000+ appearances correctly") {
                val deck =
                    CalculatedDeck(
                        deckId = "popular-deck",
                        deckName = "Popular Deck",
                        winRate = "55.5%",
                        usageShare = "40.0%",
                        appearances = 1500,
                    )

                deck.appearances shouldBe 1500
            }

            Then("Should handle maximum Int value") {
                val deck =
                    CalculatedDeck(
                        deckId = "max-deck",
                        deckName = "Max Deck",
                        winRate = "100.0%",
                        usageShare = "100.0%",
                        appearances = Int.MAX_VALUE,
                    )

                deck.appearances shouldBe Int.MAX_VALUE
            }
        }
    }

    Given("Invalid CalculatedDeck parameters") {
        When("Appearances is negative") {
            Then("Should throw IllegalArgumentException") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        CalculatedDeck(
                            deckId = "invalid-deck",
                            deckName = "Invalid Deck",
                            winRate = "50.0%",
                            usageShare = "25.0%",
                            appearances = -1,
                        )
                    }

                exception.message shouldContain "Appearances must be non-negative"
                exception.message shouldContain "-1"
            }
        }

        When("Appearances is negative with larger value") {
            Then("Should throw with exact error message") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        CalculatedDeck(
                            deckId = "test",
                            deckName = "test",
                            winRate = "0%",
                            usageShare = "0%",
                            appearances = -100,
                        )
                    }

                exception.message shouldBe "Appearances must be non-negative, but was -100"
            }
        }

        When("Appearances is at boundary: -1") {
            Then("Should throw IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    CalculatedDeck(
                        deckId = "boundary",
                        deckName = "Boundary",
                        winRate = "0%",
                        usageShare = "0%",
                        appearances = -1,
                    )
                }
            }
        }

        When("Appearances is at boundary: 0") {
            Then("Should not throw exception") {
                shouldNotThrow<IllegalArgumentException> {
                    CalculatedDeck(
                        deckId = "boundary",
                        deckName = "Boundary",
                        winRate = "0%",
                        usageShare = "0%",
                        appearances = 0,
                    )
                }
            }
        }
    }

    Given("CalculatedDeck data class behavior") {
        val originalDeck =
            CalculatedDeck(
                deckId = "original",
                deckName = "Original Deck",
                winRate = "60.0%",
                usageShare = "30.0%",
                appearances = 200,
            )

        When("Using copy to create a new instance") {
            val copiedDeck =
                originalDeck.copy(
                    deckName = "Modified Deck",
                    appearances = 300,
                )

            Then("Should create a new instance with changed values") {
                copiedDeck.deckId shouldBe "original"
                copiedDeck.deckName shouldBe "Modified Deck"
                copiedDeck.winRate shouldBe "60.0%"
                copiedDeck.usageShare shouldBe "30.0%"
                copiedDeck.appearances shouldBe 300
            }

            Then("Original should remain unchanged") {
                originalDeck.deckName shouldBe "Original Deck"
                originalDeck.appearances shouldBe 200
            }

            Then("Should be a different instance") {
                copiedDeck shouldNotBe originalDeck
            }
        }

        When("Comparing two identical decks") {
            val deck1 =
                CalculatedDeck(
                    deckId = "same",
                    deckName = "Same Deck",
                    winRate = "50.0%",
                    usageShare = "25.0%",
                    appearances = 100,
                )
            val deck2 =
                CalculatedDeck(
                    deckId = "same",
                    deckName = "Same Deck",
                    winRate = "50.0%",
                    usageShare = "25.0%",
                    appearances = 100,
                )

            Then("Should be equal") {
                deck1 shouldBe deck2
            }

            Then("Should have same hashCode") {
                deck1.hashCode() shouldBe deck2.hashCode()
            }
        }

        When("Comparing two different decks") {
            val deck1 =
                CalculatedDeck(
                    deckId = "deck1",
                    deckName = "Deck One",
                    winRate = "50.0%",
                    usageShare = "25.0%",
                    appearances = 100,
                )
            val deck2 =
                CalculatedDeck(
                    deckId = "deck2",
                    deckName = "Deck Two",
                    winRate = "60.0%",
                    usageShare = "30.0%",
                    appearances = 150,
                )

            Then("Should not be equal") {
                deck1 shouldNotBe deck2
            }
        }
    }

    Given("Edge cases for string fields") {
        When("DeckId is an empty string") {
            Then("Should succeed without exception") {
                val deck =
                    CalculatedDeck(
                        deckId = "",
                        deckName = "Empty ID Deck",
                        winRate = "50.0%",
                        usageShare = "25.0%",
                        appearances = 50,
                    )

                deck.deckId shouldBe ""
            }
        }

        When("DeckName is an empty string") {
            Then("Should succeed without exception") {
                val deck =
                    CalculatedDeck(
                        deckId = "empty-name",
                        deckName = "",
                        winRate = "50.0%",
                        usageShare = "25.0%",
                        appearances = 50,
                    )

                deck.deckName shouldBe ""
            }
        }

        When("DeckName contains special characters") {
            Then("Should handle special characters correctly") {
                val specialName = "Pikachu⚡ + Mewtwo🌟 (Gen 1) - 50%+ Win Rate!"
                val deck =
                    CalculatedDeck(
                        deckId = "special-chars",
                        deckName = specialName,
                        winRate = "52.5%",
                        usageShare = "28.0%",
                        appearances = 75,
                    )

                deck.deckName shouldBe specialName
            }
        }

        When("DeckName is very long") {
            Then("Should handle long strings correctly") {
                val longName = "A".repeat(1000)
                val deck =
                    CalculatedDeck(
                        deckId = "long-name",
                        deckName = longName,
                        winRate = "50.0%",
                        usageShare = "25.0%",
                        appearances = 100,
                    )

                deck.deckName shouldBe longName
                deck.deckName.length shouldBe 1000
            }
        }

        When("Percentage strings contain special formats") {
            Then("Should accept any string format") {
                val deck =
                    CalculatedDeck(
                        deckId = "special-format",
                        deckName = "Special Format",
                        winRate = "50.123456%",
                        usageShare = "~25%",
                        appearances = 100,
                    )

                deck.winRate shouldBe "50.123456%"
                deck.usageShare shouldBe "~25%"
            }
        }

        When("Percentage strings are empty") {
            Then("Should accept empty strings") {
                val deck =
                    CalculatedDeck(
                        deckId = "empty-percentages",
                        deckName = "Empty Percentages",
                        winRate = "",
                        usageShare = "",
                        appearances = 100,
                    )

                deck.winRate shouldBe ""
                deck.usageShare shouldBe ""
            }
        }
    }

    Given("Data class toString and structural equality") {
        When("Converting to string") {
            val deck =
                CalculatedDeck(
                    deckId = "test-deck",
                    deckName = "Test Deck",
                    winRate = "50.0%",
                    usageShare = "25.0%",
                    appearances = 100,
                )

            Then("Should contain all property values") {
                val stringRepresentation = deck.toString()
                stringRepresentation shouldContain "test-deck"
                stringRepresentation shouldContain "Test Deck"
                stringRepresentation shouldContain "50.0%"
                stringRepresentation shouldContain "25.0%"
                stringRepresentation shouldContain "100"
            }
        }

        When("Using component functions") {
            val deck =
                CalculatedDeck(
                    deckId = "component-test",
                    deckName = "Component Test",
                    winRate = "55.0%",
                    usageShare = "30.0%",
                    appearances = 150,
                )

            Then("Should destructure correctly") {
                val (deckId, deckName, winRate, usageShare, appearances) = deck

                deckId shouldBe "component-test"
                deckName shouldBe "Component Test"
                winRate shouldBe "55.0%"
                usageShare shouldBe "30.0%"
                appearances shouldBe 150
            }
        }
    }

    Given("Real-world usage scenarios") {
        When("Creating a deck with typical tournament data") {
            val deck =
                CalculatedDeck(
                    deckId = "pikachu-ex-mewtwo",
                    deckName = "Pikachu ex + Mewtwo ex",
                    winRate = "54.7%",
                    usageShare = "32.1%",
                    appearances = 456,
                )

            Then("Should represent valid tournament statistics") {
                deck.deckId shouldBe "pikachu-ex-mewtwo"
                deck.deckName shouldBe "Pikachu ex + Mewtwo ex"
                deck.winRate shouldBe "54.7%"
                deck.usageShare shouldBe "32.1%"
                deck.appearances shouldBe 456
            }
        }

        When("Creating a deck with perfect win rate") {
            val deck =
                CalculatedDeck(
                    deckId = "perfect-deck",
                    deckName = "Perfect Deck",
                    winRate = "100.0%",
                    usageShare = "5.0%",
                    appearances = 10,
                )

            Then("Should handle perfect win rate correctly") {
                deck.winRate shouldBe "100.0%"
                deck.appearances shouldBe 10
            }
        }

        When("Creating a deck with zero win rate") {
            val deck =
                CalculatedDeck(
                    deckId = "zero-wins",
                    deckName = "Zero Wins Deck",
                    winRate = "0.0%",
                    usageShare = "1.0%",
                    appearances = 20,
                )

            Then("Should handle zero win rate correctly") {
                deck.winRate shouldBe "0.0%"
                deck.appearances shouldBe 20
            }
        }
    }

    Given("pokemonNames computed property") {
        When("deckId에 단일 포켓몬이 있을 때") {
            val deck =
                CalculatedDeck(
                    deckId = "Pikachu",
                    deckName = "Pikachu Deck",
                    winRate = "50.0%",
                    usageShare = "10.0%",
                    appearances = 50,
                )

            Then("포켓몬 이름 리스트를 반환해야 한다") {
                deck.pokemonNames shouldBe listOf("Pikachu")
            }
        }

        When("deckId에 여러 포켓몬이 파이프로 구분되어 있을 때") {
            val deck =
                CalculatedDeck(
                    deckId = "Pikachu|Mewtwo|Charizard",
                    deckName = "Multi Pokemon Deck",
                    winRate = "55.0%",
                    usageShare = "25.0%",
                    appearances = 100,
                )

            Then("모든 포켓몬 이름을 파싱해야 한다") {
                deck.pokemonNames shouldBe listOf("Pikachu", "Mewtwo", "Charizard")
            }
        }

        When("deckId가 빈 문자열일 때") {
            val deck =
                CalculatedDeck(
                    deckId = "",
                    deckName = "Empty Deck",
                    winRate = "0.0%",
                    usageShare = "0.0%",
                    appearances = 0,
                )

            Then("빈 리스트를 반환해야 한다") {
                deck.pokemonNames shouldBe emptyList()
            }
        }

        When("deckId에 빈 문자열 항목이 포함되어 있을 때") {
            val deck =
                CalculatedDeck(
                    deckId = "Pikachu||Mewtwo",
                    deckName = "Deck with Empty Entry",
                    winRate = "50.0%",
                    usageShare = "15.0%",
                    appearances = 75,
                )

            Then("빈 문자열은 필터링되어야 한다") {
                deck.pokemonNames shouldBe listOf("Pikachu", "Mewtwo")
            }
        }

        When("deckId가 파이프만 있을 때") {
            val deck =
                CalculatedDeck(
                    deckId = "|||",
                    deckName = "Only Pipes",
                    winRate = "0.0%",
                    usageShare = "0.0%",
                    appearances = 0,
                )

            Then("빈 리스트를 반환해야 한다") {
                deck.pokemonNames shouldBe emptyList()
            }
        }

        When("deckId에 단일 파이프가 있을 때") {
            val deck =
                CalculatedDeck(
                    deckId = "|",
                    deckName = "Single Pipe",
                    winRate = "0.0%",
                    usageShare = "0.0%",
                    appearances = 0,
                )

            Then("빈 리스트를 반환해야 한다") {
                deck.pokemonNames shouldBe emptyList()
            }
        }

        When("deckId에 앞뒤로 빈 문자열이 있을 때") {
            val deck =
                CalculatedDeck(
                    deckId = "|Pikachu|Mewtwo|",
                    deckName = "Leading and Trailing Pipes",
                    winRate = "50.0%",
                    usageShare = "20.0%",
                    appearances = 80,
                )

            Then("앞뒤 빈 문자열은 필터링되어야 한다") {
                deck.pokemonNames shouldBe listOf("Pikachu", "Mewtwo")
            }
        }

        When("실제 덱 ID 형식을 사용할 때") {
            val deck =
                CalculatedDeck(
                    deckId = "Pikachu ex|Mewtwo ex",
                    deckName = "Pikachu ex + Mewtwo ex",
                    winRate = "54.7%",
                    usageShare = "32.1%",
                    appearances = 456,
                )

            Then("ex 포함된 이름도 정확히 파싱해야 한다") {
                deck.pokemonNames shouldBe listOf("Pikachu ex", "Mewtwo ex")
            }
        }
    }
})
