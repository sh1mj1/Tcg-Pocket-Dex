package tcg.pocket.dex.repo.decks

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.repo.tournamentstats.TournamentStatsRepo

class DefaultDecksRepoTest : BehaviorSpec({
    val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
    val repo = DefaultDecksRepo(mockTournamentStatsRepo)

    beforeEach {
        clearMocks(mockTournamentStatsRepo)
    }

    fun createCalculatedDeck(
        deckId: String = "Pikachu",
        deckName: String = "Pikachu",
        winRate: String = "50.0%",
        usageShare: String = "100.0%",
        appearances: Int = 1,
        iconUrls: List<String> = emptyList(),
    ): CalculatedDeck =
        CalculatedDeck(
            deckId = deckId,
            deckName = deckName,
            winRate = winRate,
            usageShare = usageShare,
            appearances = appearances,
            iconUrls = iconUrls,
        )

    Given("여러 덱이 서로 다른 appearances를 가진 경우") {
        When("allTierDecks를 호출할 때") {
            Then("appearances 내림차순으로 정렬되고 올바른 순위가 부여되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Deck1",
                            deckName = "Deck One",
                            appearances = 100,
                        ),
                        createCalculatedDeck(
                            deckId = "Deck2",
                            deckName = "Deck Two",
                            appearances = 50,
                        ),
                        createCalculatedDeck(
                            deckId = "Deck3",
                            deckName = "Deck Three",
                            appearances = 200,
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 3
                result[0].simple.deckName shouldBe "Deck Three"
                result[0].simple.rank shouldBe 1
                result[1].simple.deckName shouldBe "Deck One"
                result[1].simple.rank shouldBe 2
                result[2].simple.deckName shouldBe "Deck Two"
                result[2].simple.rank shouldBe 3
            }
        }
    }

    Given("단일 덱") {
        When("allTierDecks를 호출할 때") {
            Then("rank가 1이어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Pikachu",
                            deckName = "Pikachu",
                            appearances = 42,
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.rank shouldBe 1
                result[0].simple.deckName shouldBe "Pikachu"
            }
        }
    }

    Given("모든 필드 매핑 검증") {
        When("allTierDecks를 호출할 때") {
            Then("CalculatedDeck의 모든 필드가 DeckInformation에 올바르게 매핑되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Mewtwo|Pikachu",
                            deckName = "Mewtwo + Pikachu",
                            winRate = "75.5%",
                            usageShare = "42.3%",
                            appearances = 150,
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                with(result[0].simple) {
                    deckId shouldBe "Mewtwo|Pikachu"
                    deckName shouldBe "Mewtwo + Pikachu"
                    winRate shouldBe "75.5%"
                    share shouldBe "42.3%"
                    rank shouldBe 1
                }
            }
        }
    }

    Given("파이프로 구분된 덱 ID") {
        When("iconUrls가 있을 때") {
            Then("iconUrls를 사용해야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Mewtwo|Pikachu",
                            deckName = "Mewtwo + Pikachu",
                            iconUrls = listOf("http://example.com/mewtwo.png", "http://example.com/pikachu.png"),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls shouldHaveSize 2
                result[0].simple.representativePokemonImageUrls[0] shouldBe "http://example.com/mewtwo.png"
                result[0].simple.representativePokemonImageUrls[1] shouldBe "http://example.com/pikachu.png"
            }
        }

        When("iconUrls가 비어있을 때") {
            Then("플레이스홀더 URL을 사용해야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Mewtwo|Pikachu",
                            deckName = "Mewtwo + Pikachu",
                            iconUrls = emptyList(),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls shouldHaveSize 2
                result[0].simple.representativePokemonImageUrls.forEach { url ->
                    url shouldBe "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
                }
            }
        }
    }

    Given("3개의 포켓몬이 있는 덱 ID") {
        When("iconUrls가 3개일 때") {
            Then("처음 2개의 iconUrls만 사용되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Charizard|Mewtwo|Pikachu",
                            deckName = "Charizard + Mewtwo + Pikachu",
                            iconUrls = listOf("http://example.com/char.png", "http://example.com/mew.png", "http://example.com/pika.png"),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls shouldHaveSize 2
                result[0].simple.representativePokemonImageUrls[0] shouldBe "http://example.com/char.png"
                result[0].simple.representativePokemonImageUrls[1] shouldBe "http://example.com/mew.png"
            }
        }

        When("iconUrls가 없을 때") {
            Then("플레이스홀더 URL 2개가 생성되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Charizard|Mewtwo|Pikachu",
                            deckName = "Charizard + Mewtwo + Pikachu",
                            iconUrls = emptyList(),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls shouldHaveSize 2
                result[0].simple.representativePokemonImageUrls.forEach { url ->
                    url shouldBe "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
                }
            }
        }
    }

    Given("단일 포켓몬 덱 ID") {
        When("iconUrl이 1개 있을 때") {
            Then("해당 iconUrl을 사용해야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Pikachu",
                            deckName = "Pikachu",
                            iconUrls = listOf("http://example.com/pikachu.png"),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls[0] shouldBe "http://example.com/pikachu.png"
            }
        }

        When("iconUrls가 비어있을 때") {
            Then("1개의 플레이스홀더 URL이 생성되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Pikachu",
                            deckName = "Pikachu",
                            iconUrls = emptyList(),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls[0] shouldBe
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
            }
        }
    }

    Given("이미지 URL 형식 검증") {
        When("iconUrls가 제공될 때") {
            Then("iconUrls를 그대로 사용해야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Mewtwo|Pikachu|Charizard",
                            deckName = "Mewtwo + Pikachu + Charizard",
                            iconUrls = listOf("http://api.example.com/mewtwo.png", "http://api.example.com/pikachu.png"),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls[0] shouldBe "http://api.example.com/mewtwo.png"
                result[0].simple.representativePokemonImageUrls[1] shouldBe "http://api.example.com/pikachu.png"
            }
        }

        When("iconUrls가 없을 때") {
            Then("플레이스홀더 URL이 'sprites/pokemon'을 포함해야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Mewtwo|Pikachu|Charizard",
                            deckName = "Mewtwo + Pikachu + Charizard",
                            iconUrls = emptyList(),
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.representativePokemonImageUrls.forEach { url ->
                    url.contains("sprites/pokemon") shouldBe true
                }
            }
        }
    }

    Given("detail 정보 임시 값 검증") {
        When("allTierDecks를 호출할 때") {
            Then("cost는 0, pokemonTypes는 빈 리스트, description은 빈 문자열이어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Pikachu",
                            deckName = "Pikachu",
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                with(result[0].detail) {
                    cost shouldBe 0
                    pokemonTypes.shouldBeEmpty()
                    description shouldBe ""
                }
            }
        }
    }

    Given("빈 리스트") {
        When("TournamentStatsRepo가 빈 리스트를 반환할 때") {
            Then("빈 리스트를 반환해야 한다") {
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns emptyList()

                val result = repo.allTierDecks()

                result.shouldBeEmpty()
            }
        }
    }

    Given("동일한 appearances를 가진 덱들") {
        When("allTierDecks를 호출할 때") {
            Then("안정적인 정렬 순서를 유지해야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "First",
                            deckName = "First Deck",
                            appearances = 100,
                        ),
                        createCalculatedDeck(
                            deckId = "Second",
                            deckName = "Second Deck",
                            appearances = 100,
                        ),
                        createCalculatedDeck(
                            deckId = "Third",
                            deckName = "Third Deck",
                            appearances = 100,
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 3
                result[0].simple.rank shouldBe 1
                result[1].simple.rank shouldBe 2
                result[2].simple.rank shouldBe 3
                result.map { it.simple.deckName } shouldContainExactly
                    listOf("First Deck", "Second Deck", "Third Deck")
            }
        }
    }

    Given("대규모 데이터셋 (10개 이상의 덱)") {
        When("allTierDecks를 호출할 때") {
            Then("올바르게 정렬되고 순위가 부여되어야 한다") {
                val decks =
                    (1..15).map { i ->
                        createCalculatedDeck(
                            deckId = "Deck$i",
                            deckName = "Deck $i",
                            appearances = 150 - (i * 10),
                        )
                    }
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 15
                result.forEachIndexed { index, deckInfo ->
                    deckInfo.simple.rank shouldBe (index + 1)
                }
                result[0].simple.deckName shouldBe "Deck 1"
                result[0].simple.rank shouldBe 1
                result[14].simple.deckName shouldBe "Deck 15"
                result[14].simple.rank shouldBe 15
            }
        }
    }

    Given("실제 토너먼트 시나리오 - 혼합된 appearances") {
        When("allTierDecks를 호출할 때") {
            Then("실제 메타 스냅샷처럼 정렬되고 순위가 부여되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Pikachu ex|Zapdos ex",
                            deckName = "Pikachu ex + Zapdos ex",
                            winRate = "65.2%",
                            usageShare = "28.5%",
                            appearances = 42,
                        ),
                        createCalculatedDeck(
                            deckId = "Mewtwo ex|Gardevoir",
                            deckName = "Mewtwo ex + Gardevoir",
                            winRate = "71.8%",
                            usageShare = "35.2%",
                            appearances = 55,
                        ),
                        createCalculatedDeck(
                            deckId = "Charizard ex|Moltres ex",
                            deckName = "Charizard ex + Moltres ex",
                            winRate = "58.3%",
                            usageShare = "22.1%",
                            appearances = 38,
                        ),
                        createCalculatedDeck(
                            deckId = "Starmie ex|Articuno ex",
                            deckName = "Starmie ex + Articuno ex",
                            winRate = "54.7%",
                            usageShare = "14.2%",
                            appearances = 18,
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 4
                result[0].simple.deckName shouldBe "Mewtwo ex + Gardevoir"
                result[0].simple.rank shouldBe 1
                result[1].simple.deckName shouldBe "Pikachu ex + Zapdos ex"
                result[1].simple.rank shouldBe 2
                result[2].simple.deckName shouldBe "Charizard ex + Moltres ex"
                result[2].simple.rank shouldBe 3
                result[3].simple.deckName shouldBe "Starmie ex + Articuno ex"
                result[3].simple.rank shouldBe 4
            }
        }
    }

    Given("정렬 검증 - appearances 순서 확인") {
        When("allTierDecks를 호출할 때") {
            Then("appearances가 내림차순으로 정렬되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(deckId = "D1", deckName = "Deck 1", appearances = 10),
                        createCalculatedDeck(deckId = "D2", deckName = "Deck 2", appearances = 50),
                        createCalculatedDeck(deckId = "D3", deckName = "Deck 3", appearances = 30),
                        createCalculatedDeck(deckId = "D4", deckName = "Deck 4", appearances = 40),
                        createCalculatedDeck(deckId = "D5", deckName = "Deck 5", appearances = 20),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 5
                result.map { it.simple.deckName } shouldContainExactly
                    listOf("Deck 2", "Deck 4", "Deck 3", "Deck 5", "Deck 1")
                result.map { it.simple.rank } shouldContainExactly listOf(1, 2, 3, 4, 5)
            }
        }
    }

    Given("덱 ID와 이름 매핑 확인") {
        When("복잡한 덱 ID를 처리할 때") {
            Then("deckId와 deckName이 정확히 유지되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Pikachu ex|Mewtwo ex|Gardevoir",
                            deckName = "Pikachu ex + Mewtwo ex + Gardevoir",
                            appearances = 100,
                        ),
                        createCalculatedDeck(
                            deckId = "Charizard ex",
                            deckName = "Charizard ex",
                            appearances = 50,
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 2
                result[0].simple.deckId shouldBe "Pikachu ex|Mewtwo ex|Gardevoir"
                result[0].simple.deckName shouldBe "Pikachu ex + Mewtwo ex + Gardevoir"
                result[1].simple.deckId shouldBe "Charizard ex"
                result[1].simple.deckName shouldBe "Charizard ex"
            }
        }
    }

    Given("winRate와 usageShare(share) 매핑 확인") {
        When("allTierDecks를 호출할 때") {
            Then("winRate와 usageShare가 올바른 필드로 매핑되어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(
                            deckId = "Test",
                            deckName = "Test Deck",
                            winRate = "80.5%",
                            usageShare = "45.3%",
                            appearances = 100,
                        ),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 1
                result[0].simple.winRate shouldBe "80.5%"
                result[0].simple.share shouldBe "45.3%"
            }
        }
    }

    Given("순위 계산 검증 (index + 1)") {
        When("다양한 크기의 리스트를 처리할 때") {
            Then("rank가 정확히 index + 1이어야 한다") {
                val decks =
                    listOf(
                        createCalculatedDeck(deckId = "R1", deckName = "Rank 1", appearances = 500),
                        createCalculatedDeck(deckId = "R2", deckName = "Rank 2", appearances = 400),
                        createCalculatedDeck(deckId = "R3", deckName = "Rank 3", appearances = 300),
                        createCalculatedDeck(deckId = "R4", deckName = "Rank 4", appearances = 200),
                        createCalculatedDeck(deckId = "R5", deckName = "Rank 5", appearances = 100),
                    )
                coEvery { mockTournamentStatsRepo.getDeckStatistics() } returns decks

                val result = repo.allTierDecks()

                result shouldHaveSize 5
                result.forEachIndexed { index, deckInfo ->
                    deckInfo.simple.rank shouldBe (index + 1)
                }
            }
        }
    }
})
