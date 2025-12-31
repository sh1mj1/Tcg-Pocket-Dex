package tcg.pocket.dex.repo.tournamentstats

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import tcg.pocket.dex.datasource.RemoteTournamentDataSource
import tcg.pocket.dex.remote.response.Standing
import tcg.pocket.dex.remote.response.TournamentId

class DefaultTournamentStatsRepoTest : BehaviorSpec({
    val mockDataSource = mockk<RemoteTournamentDataSource>()
    val repo = DefaultTournamentStatsRepo(mockDataSource)

    beforeEach {
        clearMocks(mockDataSource)
    }

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

    fun createTournamentId(id: String = "tournament-1"): TournamentId = TournamentId(id)

    Given("정상적인 토너먼트 ID와 순위 데이터") {
        When("getDeckStatistics를 호출할 때") {
            Then("올바른 승률과 사용률을 가진 계산된 덱을 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                        createTournamentId("t2"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1", "t2")) } returns
                    listOf(
                        createStanding(
                            placing = 1,
                            playerName = "Player1",
                            deckPokemon = listOf("Pikachu", "Mewtwo"),
                            wins = 6,
                            losses = 1,
                        ),
                        createStanding(
                            placing = 2,
                            playerName = "Player2",
                            deckPokemon = listOf("Mewtwo", "Pikachu"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            placing = 3,
                            playerName = "Player3",
                            deckPokemon = listOf("Charizard"),
                            wins = 4,
                            losses = 3,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 2
                result[0].deckName shouldBe "Mewtwo + Pikachu"
                result[0].appearances shouldBe 2
                result[0].usageShare shouldBe "66.7%"
                result[0].winRate shouldBe "78.6%"
                result[1].deckName shouldBe "Charizard"
                result[1].appearances shouldBe 1
                result[1].usageShare shouldBe "33.3%"
                result[1].winRate shouldBe "57.1%"
            }
        }
    }

    Given("빈 토너먼트 리스트") {
        When("getDeckStatistics를 호출할 때") {
            Then("빈 리스트를 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns emptyList()
                coEvery { mockDataSource.getTop8Standings(emptyList()) } returns emptyList()

                val result = repo.getDeckStatistics()

                result.shouldBeEmpty()
                coVerify(exactly = 1) { mockDataSource.getQualifyingTournamentIds() }
                coVerify(exactly = 1) { mockDataSource.getTop8Standings(emptyList()) }
            }
        }
    }

    Given("토너먼트 ID는 있지만 순위 데이터가 없는 경우") {
        When("getDeckStatistics를 호출할 때") {
            Then("빈 리스트를 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                        createTournamentId("t2"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1", "t2")) } returns emptyList()

                val result = repo.getDeckStatistics()

                result.shouldBeEmpty()
                coVerify(exactly = 1) { mockDataSource.getQualifyingTournamentIds() }
                coVerify(exactly = 1) { mockDataSource.getTop8Standings(listOf("t1", "t2")) }
            }
        }
    }

    Given("모든 순위가 동일한 덱을 사용하는 경우") {
        When("getDeckStatistics를 호출할 때") {
            Then("100% 사용률을 가진 단일 덱을 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            placing = 1,
                            playerName = "Player1",
                            deckPokemon = listOf("Pikachu"),
                            wins = 6,
                            losses = 1,
                        ),
                        createStanding(
                            placing = 2,
                            playerName = "Player2",
                            deckPokemon = listOf("Pikachu"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            placing = 3,
                            playerName = "Player3",
                            deckPokemon = listOf("Pikachu"),
                            wins = 4,
                            losses = 3,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 1
                result[0].deckName shouldBe "Pikachu"
                result[0].appearances shouldBe 3
                result[0].usageShare shouldBe "100.0%"
                result[0].winRate shouldBe "71.4%"
            }
        }
    }

    Given("여러 다른 덱이 있는 순위 데이터") {
        When("getDeckStatistics를 호출할 때") {
            Then("사용률 순으로 정렬된 여러 덱을 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            placing = 1,
                            playerName = "Player1",
                            deckPokemon = listOf("Pikachu", "Mewtwo"),
                            wins = 6,
                            losses = 1,
                        ),
                        createStanding(
                            placing = 2,
                            playerName = "Player2",
                            deckPokemon = listOf("Mewtwo", "Pikachu"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            placing = 3,
                            playerName = "Player3",
                            deckPokemon = listOf("Charizard"),
                            wins = 4,
                            losses = 3,
                        ),
                        createStanding(
                            placing = 4,
                            playerName = "Player4",
                            deckPokemon = listOf("Pikachu", "Mewtwo"),
                            wins = 4,
                            losses = 3,
                        ),
                        createStanding(
                            placing = 5,
                            playerName = "Player5",
                            deckPokemon = listOf("Charizard"),
                            wins = 3,
                            losses = 4,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 2
                result[0].deckName shouldBe "Mewtwo + Pikachu"
                result[0].appearances shouldBe 3
                result[0].usageShare shouldBe "60.0%"
                result[1].deckName shouldBe "Charizard"
                result[1].appearances shouldBe 2
                result[1].usageShare shouldBe "40.0%"
            }
        }
    }

    Given("데이터소스 메서드 호출 검증") {
        When("getDeckStatistics를 호출할 때") {
            Then("올바른 파라미터로 데이터소스 메서드를 호출해야 한다") {
                val tournamentIds =
                    listOf(
                        createTournamentId("t1"),
                        createTournamentId("t2"),
                        createTournamentId("t3"),
                    )
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns tournamentIds
                coEvery { mockDataSource.getTop8Standings(listOf("t1", "t2", "t3")) } returns
                    listOf(
                        createStanding(
                            deckPokemon = listOf("Pikachu"),
                            wins = 5,
                            losses = 2,
                        ),
                    )

                repo.getDeckStatistics()

                coVerify(exactly = 1) { mockDataSource.getQualifyingTournamentIds() }
                coVerify(exactly = 1) { mockDataSource.getTop8Standings(listOf("t1", "t2", "t3")) }
            }
        }
    }

    Given("덱 ID와 이름 매핑 검증") {
        When("getDeckStatistics를 호출할 때") {
            Then("덱 ID와 이름이 올바르게 매핑되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            deckPokemon = listOf("Pikachu", "Raichu"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            deckPokemon = listOf("Raichu", "Pikachu"),
                            wins = 4,
                            losses = 3,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 1
                result[0].deckId shouldBe "Pikachu|Raichu"
                result[0].deckName shouldBe "Pikachu + Raichu"
            }
        }
    }

    Given("승률 계산 검증") {
        When("getDeckStatistics를 호출할 때") {
            Then("승률이 올바르게 계산되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            deckPokemon = listOf("Pikachu"),
                            wins = 10,
                            losses = 5,
                        ),
                        createStanding(
                            deckPokemon = listOf("Pikachu"),
                            wins = 5,
                            losses = 5,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 1
                result[0].winRate shouldBe "60.0%"
            }
        }
    }

    Given("사용률 계산 검증") {
        When("여러 덱이 있을 때") {
            Then("각 덱의 사용률이 올바르게 계산되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(deckPokemon = listOf("Pikachu"), wins = 5, losses = 2),
                        createStanding(deckPokemon = listOf("Pikachu"), wins = 4, losses = 3),
                        createStanding(deckPokemon = listOf("Pikachu"), wins = 6, losses = 1),
                        createStanding(deckPokemon = listOf("Charizard"), wins = 5, losses = 2),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 2
                result[0].deckName shouldBe "Pikachu"
                result[0].usageShare shouldBe "75.0%"
                result[1].deckName shouldBe "Charizard"
                result[1].usageShare shouldBe "25.0%"
            }
        }
    }

    Given("정렬 검증") {
        When("getDeckStatistics를 호출할 때") {
            Then("사용률 내림차순으로 정렬되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(deckPokemon = listOf("Low"), wins = 5, losses = 2),
                        createStanding(deckPokemon = listOf("High"), wins = 5, losses = 2),
                        createStanding(deckPokemon = listOf("High"), wins = 4, losses = 3),
                        createStanding(deckPokemon = listOf("High"), wins = 6, losses = 1),
                        createStanding(deckPokemon = listOf("Medium"), wins = 5, losses = 2),
                        createStanding(deckPokemon = listOf("Medium"), wins = 4, losses = 3),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 3
                result.map { it.deckName } shouldContainExactly listOf("High", "Medium", "Low")
                result[0].usageShare shouldBe "50.0%"
                result[1].usageShare shouldBe "33.3%"
                result[2].usageShare shouldBe "16.7%"
            }
        }
    }

    Given("빈 덱 포켓몬을 가진 순위 데이터") {
        When("getDeckStatistics를 호출할 때") {
            Then("빈 덱 포켓몬은 필터링되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(deckPokemon = emptyList(), wins = 5, losses = 2),
                        createStanding(deckPokemon = listOf("Pikachu"), wins = 4, losses = 3),
                        createStanding(deckPokemon = emptyList(), wins = 6, losses = 1),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 1
                result[0].deckName shouldBe "Pikachu"
                result[0].usageShare shouldBe "100.0%"
            }
        }
    }

    Given("실제 토너먼트 시나리오") {
        When("다양한 덱과 플레이어가 있는 토너먼트 데이터") {
            Then("현실적인 메타 스냅샷을 생성해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("regional-1"),
                        createTournamentId("regional-2"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("regional-1", "regional-2")) } returns
                    listOf(
                        createStanding(
                            placing = 1,
                            playerName = "Player1",
                            deckPokemon = listOf("Pikachu ex", "Mewtwo ex"),
                            wins = 6,
                            losses = 1,
                        ),
                        createStanding(
                            placing = 2,
                            playerName = "Player2",
                            deckPokemon = listOf("Charizard ex"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            placing = 3,
                            playerName = "Player3",
                            deckPokemon = listOf("Mewtwo ex", "Pikachu ex"),
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
                        createStanding(
                            placing = 5,
                            playerName = "Player5",
                            deckPokemon = listOf("Pikachu ex", "Mewtwo ex"),
                            wins = 4,
                            losses = 3,
                        ),
                        createStanding(
                            placing = 6,
                            playerName = "Player6",
                            deckPokemon = listOf("Articuno ex", "Starmie ex"),
                            wins = 3,
                            losses = 4,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 3
                result[0].deckName shouldBe "Mewtwo ex + Pikachu ex"
                result[0].appearances shouldBe 3
                result[0].usageShare shouldBe "50.0%"
                result[1].deckName shouldBe "Charizard ex"
                result[1].appearances shouldBe 2
                result[1].usageShare shouldBe "33.3%"
                result[2].deckName shouldBe "Articuno ex + Starmie ex"
                result[2].appearances shouldBe 1
                result[2].usageShare shouldBe "16.7%"
            }
        }
    }

    Given("단일 토너먼트 ID") {
        When("getDeckStatistics를 호출할 때") {
            Then("올바르게 처리되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("single-tournament"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("single-tournament")) } returns
                    listOf(
                        createStanding(
                            deckPokemon = listOf("Pikachu"),
                            wins = 6,
                            losses = 1,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 1
                result[0].deckName shouldBe "Pikachu"
                coVerify(exactly = 1) { mockDataSource.getTop8Standings(listOf("single-tournament")) }
            }
        }
    }

    Given("복수 토너먼트 ID") {
        When("getDeckStatistics를 호출할 때") {
            Then("모든 토너먼트 ID가 전달되어야 한다") {
                val tournamentIds =
                    listOf(
                        createTournamentId("t1"),
                        createTournamentId("t2"),
                        createTournamentId("t3"),
                        createTournamentId("t4"),
                        createTournamentId("t5"),
                    )
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns tournamentIds
                coEvery { mockDataSource.getTop8Standings(any()) } returns emptyList()

                repo.getDeckStatistics()

                coVerify(exactly = 1) {
                    mockDataSource.getTop8Standings(listOf("t1", "t2", "t3", "t4", "t5"))
                }
            }
        }
    }

    Given("동일한 사용률에서 승률이 다른 덱들") {
        When("getDeckStatistics를 호출할 때") {
            Then("승률 내림차순으로 2차 정렬되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(deckPokemon = listOf("Low WR"), wins = 3, losses = 7),
                        createStanding(deckPokemon = listOf("High WR"), wins = 7, losses = 3),
                        createStanding(deckPokemon = listOf("Medium WR"), wins = 5, losses = 5),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 3
                result.map { it.deckName } shouldContainExactly listOf("High WR", "Medium WR", "Low WR")
                result[0].winRate shouldBe "70.0%"
                result[1].winRate shouldBe "50.0%"
                result[2].winRate shouldBe "30.0%"
            }
        }
    }

    Given("파이프라인 통합 검증") {
        When("전체 파이프라인이 실행될 때") {
            Then("각 단계가 올바르게 연결되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("pipeline-test"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("pipeline-test")) } returns
                    listOf(
                        createStanding(
                            deckPokemon = listOf("Pikachu", "Mewtwo"),
                            wins = 10,
                            losses = 5,
                        ),
                        createStanding(
                            deckPokemon = listOf("Mewtwo", "Pikachu"),
                            wins = 8,
                            losses = 7,
                        ),
                    )

                val result = repo.getDeckStatistics()

                result shouldHaveSize 1
                result[0].deckId shouldBe "Mewtwo|Pikachu"
                result[0].deckName shouldBe "Mewtwo + Pikachu"
                result[0].appearances shouldBe 2
                result[0].winRate shouldBe "60.0%"
                result[0].usageShare shouldBe "100.0%"
            }
        }
    }

    Given("getDeckById with valid deckId") {
        When("유효한 deckId로 getDeckById를 호출하면") {
            Then("해당 덱을 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            placing = 1,
                            playerName = "Player1",
                            deckPokemon = listOf("Pikachu", "Mewtwo"),
                            wins = 6,
                            losses = 1,
                        ),
                        createStanding(
                            placing = 2,
                            playerName = "Player2",
                            deckPokemon = listOf("Mewtwo", "Pikachu"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            placing = 3,
                            playerName = "Player3",
                            deckPokemon = listOf("Charizard"),
                            wins = 4,
                            losses = 3,
                        ),
                    )

                val result = repo.getDeckById("Mewtwo|Pikachu")

                result shouldBe repo.getDeckStatistics().find { it.deckId == "Mewtwo|Pikachu" }
                result?.deckId shouldBe "Mewtwo|Pikachu"
                result?.deckName shouldBe "Mewtwo + Pikachu"
                result?.appearances shouldBe 2
            }
        }
    }

    Given("getDeckById with non-existent deckId") {
        When("존재하지 않는 deckId로 getDeckById를 호출하면") {
            Then("null을 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            deckPokemon = listOf("Pikachu"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            deckPokemon = listOf("Charizard"),
                            wins = 4,
                            losses = 3,
                        ),
                    )

                val result = repo.getDeckById("NonExistent")

                result shouldBe null
            }
        }
    }

    Given("getDeckById with empty deck list") {
        When("빈 리스트에서 getDeckById를 호출하면") {
            Then("null을 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns emptyList()
                coEvery { mockDataSource.getTop8Standings(emptyList()) } returns emptyList()

                val result = repo.getDeckById("AnyDeck")

                result shouldBe null
            }
        }
    }

    Given("getDeckById with multiple decks") {
        When("여러 덱 중에서 특정 덱을 찾으면") {
            Then("정확히 일치하는 덱만 반환해야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            placing = 1,
                            deckPokemon = listOf("Pikachu", "Mewtwo"),
                            wins = 6,
                            losses = 1,
                        ),
                        createStanding(
                            placing = 2,
                            deckPokemon = listOf("Charizard", "Blastoise"),
                            wins = 5,
                            losses = 2,
                        ),
                        createStanding(
                            placing = 3,
                            deckPokemon = listOf("Venusaur"),
                            wins = 4,
                            losses = 3,
                        ),
                        createStanding(
                            placing = 4,
                            deckPokemon = listOf("Mewtwo", "Pikachu"),
                            wins = 4,
                            losses = 3,
                        ),
                    )

                val result = repo.getDeckById("Blastoise|Charizard")

                result?.deckId shouldBe "Blastoise|Charizard"
                result?.deckName shouldBe "Blastoise + Charizard"
                result?.appearances shouldBe 1
            }
        }
    }

    Given("getDeckById verification") {
        When("getDeckById를 호출할 때") {
            Then("getDeckStatistics가 정확히 한 번 호출되어야 한다") {
                coEvery { mockDataSource.getQualifyingTournamentIds() } returns
                    listOf(
                        createTournamentId("t1"),
                    )
                coEvery { mockDataSource.getTop8Standings(listOf("t1")) } returns
                    listOf(
                        createStanding(
                            deckPokemon = listOf("Pikachu"),
                            wins = 5,
                            losses = 2,
                        ),
                    )

                repo.getDeckById("Pikachu")

                coVerify(exactly = 1) { mockDataSource.getQualifyingTournamentIds() }
                coVerify(exactly = 1) { mockDataSource.getTop8Standings(listOf("t1")) }
            }
        }
    }
})
