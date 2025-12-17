package tcg.pocket.dex.datasource

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import tcg.pocket.dex.remote.response.DeckResponse
import tcg.pocket.dex.remote.response.PlayerResponse
import tcg.pocket.dex.remote.response.RecordResponse
import tcg.pocket.dex.remote.response.StandingResponse
import tcg.pocket.dex.remote.service.TournamentStatsService

class RemoteTournamentDataSourceStandingsTest : BehaviorSpec({
    val mockService = mockk<TournamentStatsService>()
    val dataSource = RemoteTournamentDataSource(mockService)

    beforeEach {
        clearMocks(mockService)
    }

    Given("단일 토너먼트의 Top 8 순위 데이터") {
        When("1-8등 순위가 있을 때") {
            Then("모든 8개의 순위를 반환해야 한다") {
                // Given
                val standings =
                    (1..8).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "Player $placing", country = "US", region = "CA"),
                            deck = DeckResponse(icons = listOf("Pikachu", "Mewtwo")),
                            record = RecordResponse(wins = 10 - placing, losses = placing - 1, ties = 0),
                        )
                    }
                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 8
                result.map { it.placing } shouldContainExactlyInAnyOrder (1..8).toList()
                result.all { it.playerName.startsWith("Player") } shouldBe true
                result.all { it.deckPokemon == listOf("Pikachu", "Mewtwo") } shouldBe true
            }
        }
    }

    Given("8등보다 낮은 순위가 포함된 토너먼트") {
        When("1-16등 순위가 있을 때") {
            Then("1-8등만 필터링하여 반환해야 한다") {
                // Given
                val standings =
                    (1..16).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "Player $placing", country = "JP"),
                            deck = DeckResponse(icons = listOf("Charizard")),
                            record = RecordResponse(wins = 16 - placing, losses = placing - 1, ties = 0),
                        )
                    }
                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 8
                result.map { it.placing } shouldContainExactlyInAnyOrder (1..8).toList()
                result.none { it.placing > 8 } shouldBe true
            }
        }
    }

    Given("여러 토너먼트의 일괄 처리") {
        When("3개의 토너먼트 ID가 주어질 때") {
            Then("모든 토너먼트를 조회하고 결과를 집계해야 한다") {
                // Given
                val tournament1Standings =
                    (1..5).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "T1-Player$placing", country = "US"),
                            deck = DeckResponse(icons = listOf("Pikachu")),
                            record = RecordResponse(wins = 6 - placing, losses = 0, ties = 0),
                        )
                    }
                val tournament2Standings =
                    (1..6).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "T2-Player$placing", country = "UK"),
                            deck = DeckResponse(icons = listOf("Mewtwo")),
                            record = RecordResponse(wins = 7 - placing, losses = 0, ties = 0),
                        )
                    }
                val tournament3Standings =
                    (1..8).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "T3-Player$placing", country = "JP"),
                            deck = DeckResponse(icons = listOf("Charizard")),
                            record = RecordResponse(wins = 9 - placing, losses = 0, ties = 0),
                        )
                    }

                coEvery { mockService.fetchStandings("t1") } returns tournament1Standings
                coEvery { mockService.fetchStandings("t2") } returns tournament2Standings
                coEvery { mockService.fetchStandings("t3") } returns tournament3Standings

                // When
                val result = dataSource.getTop8Standings(listOf("t1", "t2", "t3"))

                // Then
                result shouldHaveSize 19 // 5 + 6 + 8
                result.count { it.playerName.startsWith("T1-") } shouldBe 5
                result.count { it.playerName.startsWith("T2-") } shouldBe 6
                result.count { it.playerName.startsWith("T3-") } shouldBe 8
                coVerify(exactly = 1) { mockService.fetchStandings("t1") }
                coVerify(exactly = 1) { mockService.fetchStandings("t2") }
                coVerify(exactly = 1) { mockService.fetchStandings("t3") }
            }
        }
    }

    Given("빈 순위 리스트") {
        When("토너먼트의 순위가 비어있을 때") {
            Then("빈 리스트를 반환해야 한다") {
                // Given
                coEvery { mockService.fetchStandings("tournament-empty") } returns emptyList()

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-empty"))

                // Then
                result.shouldBeEmpty()
                coVerify(exactly = 1) { mockService.fetchStandings("tournament-empty") }
            }
        }

        When("빈 토너먼트 ID 리스트가 주어질 때") {
            Then("빈 리스트를 반환해야 한다") {
                // When
                val result = dataSource.getTop8Standings(emptyList())

                // Then
                result.shouldBeEmpty()
                coVerify(exactly = 0) { mockService.fetchStandings(any()) }
            }
        }
    }

    Given("에러 처리 - 일부 토너먼트 실패") {
        When("3개 중 1개 토너먼트 조회가 실패할 때") {
            Then("성공한 토너먼트의 결과만 반환해야 한다") {
                // Given
                val tournament1Standings =
                    (1..4).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "T1-Player$placing"),
                            deck = DeckResponse(icons = listOf("Pikachu")),
                            record = RecordResponse(wins = 5 - placing, losses = 0, ties = 0),
                        )
                    }
                val tournament3Standings =
                    (1..5).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "T3-Player$placing"),
                            deck = DeckResponse(icons = listOf("Charizard")),
                            record = RecordResponse(wins = 6 - placing, losses = 0, ties = 0),
                        )
                    }

                coEvery { mockService.fetchStandings("t1") } returns tournament1Standings
                coEvery { mockService.fetchStandings("t2") } throws
                    RuntimeException("Network error")
                coEvery { mockService.fetchStandings("t3") } returns tournament3Standings

                // When
                val result = dataSource.getTop8Standings(listOf("t1", "t2", "t3"))

                // Then
                result shouldHaveSize 9 // 4 + 5 (t2 excluded)
                result.count { it.playerName.startsWith("T1-") } shouldBe 4
                result.count { it.playerName.startsWith("T2-") } shouldBe 0
                result.count { it.playerName.startsWith("T3-") } shouldBe 5
            }
        }
    }

    Given("에러 처리 - 모든 토너먼트 실패") {
        When("모든 토너먼트 조회가 실패할 때") {
            Then("예외를 던지지 않고 빈 리스트를 반환해야 한다") {
                // Given
                coEvery { mockService.fetchStandings("t1") } throws
                    RuntimeException("Network error 1")
                coEvery { mockService.fetchStandings("t2") } throws
                    RuntimeException("Network error 2")
                coEvery { mockService.fetchStandings("t3") } throws
                    RuntimeException("Network error 3")

                // When
                val result = dataSource.getTop8Standings(listOf("t1", "t2", "t3"))

                // Then
                result.shouldBeEmpty()
                coVerify(exactly = 1) { mockService.fetchStandings("t1") }
                coVerify(exactly = 1) { mockService.fetchStandings("t2") }
                coVerify(exactly = 1) { mockService.fetchStandings("t3") }
            }
        }
    }

    Given("대용량 일괄 처리 (10개 이상 토너먼트)") {
        When("12개의 토너먼트 ID가 주어질 때") {
            Then("청크로 나누어 처리하고 모든 결과를 반환해야 한다") {
                // Given
                val tournamentIds = (1..12).map { "tournament-$it" }

                tournamentIds.forEach { id ->
                    val standings =
                        listOf(
                            StandingResponse(
                                placing = 1,
                                player = PlayerResponse(name = "Player-$id"),
                                deck = DeckResponse(icons = listOf("Pokemon-$id")),
                                record = RecordResponse(wins = 10, losses = 0, ties = 0),
                            ),
                        )
                    coEvery { mockService.fetchStandings(id) } returns standings
                }

                // When
                val result = dataSource.getTop8Standings(tournamentIds)

                // Then
                result shouldHaveSize 12
                tournamentIds.forEach { id ->
                    coVerify(exactly = 1) { mockService.fetchStandings(id) }
                }
            }
        }
    }

    Given("경계값 테스트 - 정확히 8등") {
        When("8등 순위가 포함될 때") {
            Then("8등 순위를 포함해야 한다") {
                // Given
                val standings =
                    listOf(
                        StandingResponse(
                            placing = 8,
                            player = PlayerResponse(name = "Boundary Player 8"),
                            deck = DeckResponse(icons = listOf("Pikachu")),
                            record = RecordResponse(wins = 5, losses = 3, ties = 0),
                        ),
                    )
                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 1
                result.first().placing shouldBe 8
                result.first().playerName shouldBe "Boundary Player 8"
            }
        }
    }

    Given("경계값 테스트 - 정확히 9등") {
        When("9등 순위가 포함될 때") {
            Then("9등 순위를 제외해야 한다") {
                // Given
                val standings =
                    listOf(
                        StandingResponse(
                            placing = 9,
                            player = PlayerResponse(name = "Boundary Player 9"),
                            deck = DeckResponse(icons = listOf("Pikachu")),
                            record = RecordResponse(wins = 5, losses = 4, ties = 0),
                        ),
                    )
                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result.shouldBeEmpty()
            }
        }
    }

    Given("서비스 호출 검증") {
        When("여러 토너먼트 ID가 주어질 때") {
            Then("각 ID에 대해 정확히 한 번씩만 fetchStandings를 호출해야 한다") {
                // Given
                val tournamentIds = listOf("t1", "t2", "t3", "t4", "t5")

                tournamentIds.forEach { id ->
                    coEvery { mockService.fetchStandings(id) } returns emptyList()
                }

                // When
                dataSource.getTop8Standings(tournamentIds)

                // Then
                tournamentIds.forEach { id ->
                    coVerify(exactly = 1) { mockService.fetchStandings(id) }
                }
            }
        }
    }

    Given("도메인 매핑 검증") {
        When("StandingResponse 데이터가 주어질 때") {
            Then("Standing 도메인 모델로 올바르게 변환되어야 한다") {
                // Given
                val standingResponse =
                    StandingResponse(
                        placing = 1,
                        player =
                            PlayerResponse(
                                name = "Champion Player",
                                country = "US",
                                region = "California",
                            ),
                        deck =
                            DeckResponse(
                                icons = listOf("Pikachu ex", "Zapdos ex", "Electrode"),
                            ),
                        record =
                            RecordResponse(
                                wins = 10,
                                losses = 1,
                                ties = 0,
                            ),
                    )

                coEvery { mockService.fetchStandings("tournament-1") } returns listOf(standingResponse)

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 1
                val standing = result.first()
                standing.placing shouldBe 1
                standing.playerName shouldBe "Champion Player"
                standing.country shouldBe "US"
                standing.region shouldBe "California"
                standing.deckPokemon shouldContainExactlyInAnyOrder
                    listOf("Pikachu ex", "Zapdos ex", "Electrode")
                standing.wins shouldBe 10
                standing.losses shouldBe 1
                standing.ties shouldBe 0
            }
        }

        When("deck이 null인 StandingResponse가 주어질 때") {
            Then("빈 pokemon 리스트로 변환되어야 한다") {
                // Given
                val standingResponse =
                    StandingResponse(
                        placing = 1,
                        player = PlayerResponse(name = "Player without deck"),
                        deck = null,
                        record = RecordResponse(wins = 5, losses = 2, ties = 1),
                    )

                coEvery { mockService.fetchStandings("tournament-1") } returns listOf(standingResponse)

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 1
                result.first().deckPokemon.shouldBeEmpty()
            }
        }

        When("player 정보가 선택적 필드(country, region)를 포함하지 않을 때") {
            Then("null 값으로 올바르게 매핑되어야 한다") {
                // Given
                val standingResponse =
                    StandingResponse(
                        placing = 3,
                        player =
                            PlayerResponse(
                                name = "Anonymous Player",
                                country = null,
                                region = null,
                            ),
                        deck = DeckResponse(icons = listOf("Mewtwo")),
                        record = RecordResponse(wins = 7, losses = 3, ties = 0),
                    )

                coEvery { mockService.fetchStandings("tournament-1") } returns listOf(standingResponse)

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 1
                val standing = result.first()
                standing.playerName shouldBe "Anonymous Player"
                standing.country shouldBe null
                standing.region shouldBe null
            }
        }
    }

    Given("혼합된 순위 시나리오") {
        When("Top 8 이내와 이외의 순위가 섞여 있을 때") {
            Then("Top 8만 필터링되어야 한다") {
                // Given
                val standings =
                    listOf(
                        StandingResponse(
                            placing = 1,
                            player = PlayerResponse(name = "1st"),
                            deck = DeckResponse(icons = listOf("A")),
                            record = RecordResponse(10, 0, 0),
                        ),
                        StandingResponse(
                            placing = 5,
                            player = PlayerResponse(name = "5th"),
                            deck = DeckResponse(icons = listOf("B")),
                            record = RecordResponse(7, 3, 0),
                        ),
                        StandingResponse(
                            placing = 8,
                            player = PlayerResponse(name = "8th"),
                            deck = DeckResponse(icons = listOf("C")),
                            record = RecordResponse(6, 4, 0),
                        ),
                        StandingResponse(
                            placing = 9,
                            player = PlayerResponse(name = "9th"),
                            deck = DeckResponse(icons = listOf("D")),
                            record = RecordResponse(5, 5, 0),
                        ),
                        StandingResponse(
                            placing = 12,
                            player = PlayerResponse(name = "12th"),
                            deck = DeckResponse(icons = listOf("E")),
                            record = RecordResponse(4, 6, 0),
                        ),
                        StandingResponse(
                            placing = 16,
                            player = PlayerResponse(name = "16th"),
                            deck = DeckResponse(icons = listOf("F")),
                            record = RecordResponse(3, 7, 0),
                        ),
                    )

                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 3
                result.map { it.placing } shouldContainExactlyInAnyOrder listOf(1, 5, 8)
                result.map { it.playerName } shouldContainExactlyInAnyOrder listOf("1st", "5th", "8th")
            }
        }
    }

    Given("청킹 동작 검증") {
        When("정확히 5개 토너먼트 (청크 크기)가 주어질 때") {
            Then("하나의 청크로 처리되어야 한다") {
                // Given
                val tournamentIds = (1..5).map { "tournament-$it" }

                tournamentIds.forEach { id ->
                    coEvery { mockService.fetchStandings(id) } returns
                        listOf(
                            StandingResponse(
                                placing = 1,
                                player = PlayerResponse(name = "Player-$id"),
                                deck = DeckResponse(icons = emptyList()),
                                record = RecordResponse(5, 0, 0),
                            ),
                        )
                }

                // When
                val result = dataSource.getTop8Standings(tournamentIds)

                // Then
                result shouldHaveSize 5
            }
        }

        When("6개 토너먼트가 주어질 때") {
            Then("두 개의 청크로 처리되어야 한다") {
                // Given
                val tournamentIds = (1..6).map { "tournament-$it" }

                tournamentIds.forEach { id ->
                    coEvery { mockService.fetchStandings(id) } returns
                        listOf(
                            StandingResponse(
                                placing = 1,
                                player = PlayerResponse(name = "Player-$id"),
                                deck = DeckResponse(icons = emptyList()),
                                record = RecordResponse(5, 0, 0),
                            ),
                        )
                }

                // When
                val result = dataSource.getTop8Standings(tournamentIds)

                // Then
                result shouldHaveSize 6
            }
        }
    }

    Given("극단적인 순위 값") {
        When("매우 낮은 순위 (100등 이상)가 포함될 때") {
            Then("모두 필터링되어야 한다") {
                // Given
                val standings =
                    (100..150).map { placing ->
                        StandingResponse(
                            placing = placing,
                            player = PlayerResponse(name = "Player $placing"),
                            deck = DeckResponse(icons = emptyList()),
                            record = RecordResponse(0, 10, 0),
                        )
                    }
                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result.shouldBeEmpty()
            }
        }

        When("placing이 0인 순위가 있을 때") {
            Then("포함되어야 한다 (필터는 <= 8 이므로)") {
                // Given
                val standings =
                    listOf(
                        StandingResponse(
                            placing = 0,
                            player = PlayerResponse(name = "Zero Placing Player"),
                            deck = DeckResponse(icons = emptyList()),
                            record = RecordResponse(0, 0, 0),
                        ),
                    )
                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 1
                result.first().placing shouldBe 0
            }
        }

        When("placing이 음수인 순위가 있을 때") {
            Then("포함되어야 한다 (필터는 <= 8 이므로)") {
                // Given
                val standings =
                    listOf(
                        StandingResponse(
                            placing = -1,
                            player = PlayerResponse(name = "Negative Placing Player"),
                            deck = DeckResponse(icons = emptyList()),
                            record = RecordResponse(0, 0, 0),
                        ),
                    )
                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 1
                result.first().placing shouldBe -1
            }
        }
    }

    Given("다양한 예외 타입") {
        When("서로 다른 예외가 발생할 때") {
            Then("모든 예외를 처리하고 실패한 요청은 건너뛰어야 한다") {
                // Given
                coEvery { mockService.fetchStandings("t1") } throws
                    IllegalArgumentException("Invalid argument")
                coEvery { mockService.fetchStandings("t2") } throws
                    NullPointerException("Null value")
                coEvery { mockService.fetchStandings("t3") } throws
                    RuntimeException("Runtime error")
                coEvery { mockService.fetchStandings("t4") } returns
                    listOf(
                        StandingResponse(
                            placing = 1,
                            player = PlayerResponse(name = "Success"),
                            deck = DeckResponse(icons = emptyList()),
                            record = RecordResponse(5, 0, 0),
                        ),
                    )

                // When
                val result = dataSource.getTop8Standings(listOf("t1", "t2", "t3", "t4"))

                // Then
                result shouldHaveSize 1
                result.first().playerName shouldBe "Success"
            }
        }
    }

    Given("record 필드의 다양한 값") {
        When("wins, losses, ties가 다양한 값을 가질 때") {
            Then("모든 값이 올바르게 매핑되어야 한다") {
                // Given
                val standings =
                    listOf(
                        StandingResponse(
                            placing = 1,
                            player = PlayerResponse(name = "Perfect Record"),
                            deck = DeckResponse(icons = emptyList()),
                            record = RecordResponse(wins = 10, losses = 0, ties = 0),
                        ),
                        StandingResponse(
                            placing = 2,
                            player = PlayerResponse(name = "With Ties"),
                            deck = DeckResponse(icons = emptyList()),
                            record = RecordResponse(wins = 8, losses = 1, ties = 1),
                        ),
                        StandingResponse(
                            placing = 3,
                            player = PlayerResponse(name = "Many Losses"),
                            deck = DeckResponse(icons = emptyList()),
                            record = RecordResponse(wins = 5, losses = 5, ties = 0),
                        ),
                    )

                coEvery { mockService.fetchStandings("tournament-1") } returns standings

                // When
                val result = dataSource.getTop8Standings(listOf("tournament-1"))

                // Then
                result shouldHaveSize 3

                val perfectRecord = result.find { it.playerName == "Perfect Record" }!!
                perfectRecord.wins shouldBe 10
                perfectRecord.losses shouldBe 0
                perfectRecord.ties shouldBe 0

                val withTies = result.find { it.playerName == "With Ties" }!!
                withTies.wins shouldBe 8
                withTies.losses shouldBe 1
                withTies.ties shouldBe 1

                val manyLosses = result.find { it.playerName == "Many Losses" }!!
                manyLosses.wins shouldBe 5
                manyLosses.losses shouldBe 5
                manyLosses.ties shouldBe 0
            }
        }
    }
})
