package tcg.pocket.dex.datasource

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import tcg.pocket.dex.remote.response.TournamentResponse
import tcg.pocket.dex.remote.service.TournamentStatsService

class RemoteTournamentDataSourceTest : BehaviorSpec({
    val mockService = mockk<TournamentStatsService>()
    val dataSource = RemoteTournamentDataSource(mockService)

    beforeEach {
        clearMocks(mockService)
    }

    Given("다양한 참가자 수를 가진 토너먼트들") {
        When("기본 임계값(> 32)으로 필터링할 때") {
            Then("32명보다 많은 참가자를 가진 토너먼트만 반환해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        // excluded
                        TournamentResponse("t1", "Small Event", "2025-01-01", "POCKET", 30),
                        // excluded (boundary)
                        TournamentResponse("t2", "Boundary 32", "2025-01-02", "POCKET", 32),
                        // included (boundary)
                        TournamentResponse("t3", "Boundary 33", "2025-01-03", "POCKET", 33),
                        // included
                        TournamentResponse("t4", "Medium Event", "2025-01-04", "POCKET", 50),
                        // included
                        TournamentResponse("t5", "Large Event", "2025-01-05", "POCKET", 100),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result shouldHaveSize 3
                result.map { it.id } shouldContainExactly listOf("t3", "t4", "t5")
            }
        }

        When("정확히 32명의 참가자를 가진 토너먼트가 있을 때") {
            Then("제외되어야 한다 (경계값 테스트)") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Exactly 32 Players", "2025-01-01", "POCKET", 32),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result.shouldBeEmpty()
                coVerify(exactly = 1) { mockService.fetchTournaments("POCKET") }
            }
        }

        When("정확히 33명의 참가자를 가진 토너먼트가 있을 때") {
            Then("포함되어야 한다 (경계값 테스트)") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Exactly 33 Players", "2025-01-01", "POCKET", 33),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result shouldHaveSize 1
                result.first().id shouldBe "t1"
            }
        }
    }

    Given("표준 범위의 토너먼트들") {
        When("40, 50, 60명의 참가자를 가진 토너먼트들이 있을 때") {
            Then("모두 필터를 통과해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event 40", "2025-01-01", "POCKET", 40),
                        TournamentResponse("t2", "Event 50", "2025-01-02", "POCKET", 50),
                        TournamentResponse("t3", "Event 60", "2025-01-03", "POCKET", 60),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result shouldHaveSize 3
                result.map { it.id } shouldContainExactly listOf("t1", "t2", "t3")
            }
        }
    }

    Given("임계값 미만의 토너먼트들") {
        When("10, 20, 30명의 참가자를 가진 토너먼트들이 있을 때") {
            Then("모두 제외되어야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Small Event 10", "2025-01-01", "POCKET", 10),
                        TournamentResponse("t2", "Small Event 20", "2025-01-02", "POCKET", 20),
                        TournamentResponse("t3", "Small Event 30", "2025-01-03", "POCKET", 30),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result.shouldBeEmpty()
            }
        }

        When("모든 토너먼트가 33명 미만의 참가자를 가질 때") {
            Then("빈 리스트를 반환해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "POCKET", 5),
                        TournamentResponse("t2", "Event 2", "2025-01-02", "POCKET", 15),
                        TournamentResponse("t3", "Event 3", "2025-01-03", "POCKET", 25),
                        TournamentResponse("t4", "Event 4", "2025-01-04", "POCKET", 32),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result.shouldBeEmpty()
            }
        }
    }

    Given("빈 토너먼트 리스트") {
        When("서비스가 빈 리스트를 반환할 때") {
            Then("빈 리스트를 반환해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns emptyList()

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result.shouldBeEmpty()
                coVerify(exactly = 1) { mockService.fetchTournaments("POCKET") }
            }
        }
    }

    Given("혼합된 참가자 수의 토너먼트들") {
        When("임계값 이상과 이하의 토너먼트가 섞여 있을 때") {
            Then("임계값 이상의 토너먼트만 필터링되어야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "POCKET", 5),
                        TournamentResponse("t2", "Event 2", "2025-01-02", "POCKET", 40),
                        TournamentResponse("t3", "Event 3", "2025-01-03", "POCKET", 15),
                        TournamentResponse("t4", "Event 4", "2025-01-04", "POCKET", 60),
                        TournamentResponse("t5", "Event 5", "2025-01-05", "POCKET", 32),
                        TournamentResponse("t6", "Event 6", "2025-01-06", "POCKET", 33),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result shouldHaveSize 3
                result.map { it.id } shouldContainExactly listOf("t2", "t4", "t6")
            }
        }

        When("극단적인 값들이 포함될 때") {
            Then("올바르게 필터링되어야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Very Small", "2025-01-01", "POCKET", 1),
                        TournamentResponse("t2", "Very Large", "2025-01-02", "POCKET", 1000),
                        TournamentResponse("t3", "Zero", "2025-01-03", "POCKET", 0),
                        TournamentResponse("t4", "Mega Event", "2025-01-04", "POCKET", 10000),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result shouldHaveSize 2
                result.map { it.id } shouldContainExactly listOf("t2", "t4")
            }
        }
    }

    Given("기본 파라미터 테스트") {
        When("파라미터를 명시하지 않을 때") {
            Then("game='POCKET'과 minPlayers=32를 기본값으로 사용해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "POCKET", 35),
                        TournamentResponse("t2", "Event 2", "2025-01-02", "POCKET", 40),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                coVerify(exactly = 1) { mockService.fetchTournaments("POCKET") }
                result shouldHaveSize 2
            }
        }
    }

    Given("커스텀 파라미터 테스트") {
        When("다른 game 파라미터를 전달할 때") {
            Then("해당 game으로 서비스를 호출해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("OTHER_GAME") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "OTHER_GAME", 50),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds(game = "OTHER_GAME")

                // Then
                coVerify(exactly = 1) { mockService.fetchTournaments("OTHER_GAME") }
                result shouldHaveSize 1
            }
        }

        When("다른 minPlayers 값을 전달할 때") {
            Then("해당 값으로 필터링해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "POCKET", 40),
                        TournamentResponse("t2", "Event 2", "2025-01-02", "POCKET", 50),
                        TournamentResponse("t3", "Event 3", "2025-01-03", "POCKET", 60),
                    )

                // When - minPlayers를 50으로 설정하면 50보다 큰 것만
                val result = dataSource.getQualifyingTournamentIds(minPlayers = 50)

                // Then
                result shouldHaveSize 1
                result.first().id shouldBe "t3"
            }
        }

        When("minPlayers를 0으로 설정할 때") {
            Then("0보다 큰 모든 토너먼트를 반환해야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "POCKET", 0),
                        TournamentResponse("t2", "Event 2", "2025-01-02", "POCKET", 1),
                        TournamentResponse("t3", "Event 3", "2025-01-03", "POCKET", 10),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds(minPlayers = 0)

                // Then
                result shouldHaveSize 2
                result.map { it.id } shouldContainExactly listOf("t2", "t3")
            }
        }

        When("커스텀 game과 minPlayers를 동시에 전달할 때") {
            Then("두 파라미터 모두 올바르게 적용되어야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("CUSTOM_GAME") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "CUSTOM_GAME", 80),
                        TournamentResponse("t2", "Event 2", "2025-01-02", "CUSTOM_GAME", 100),
                        TournamentResponse("t3", "Event 3", "2025-01-03", "CUSTOM_GAME", 120),
                    )

                // When
                val result =
                    dataSource.getQualifyingTournamentIds(
                        game = "CUSTOM_GAME",
                        minPlayers = 100,
                    )

                // Then
                coVerify(exactly = 1) { mockService.fetchTournaments("CUSTOM_GAME") }
                result shouldHaveSize 1
                result.first().id shouldBe "t3"
            }
        }
    }

    Given("매핑 로직 테스트") {
        When("필터링된 토너먼트들이 TournamentId로 변환될 때") {
            Then("id 값만 포함된 TournamentId 객체로 변환되어야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse(
                            id = "tournament-123",
                            name = "Championship 2025",
                            date = "2025-06-15",
                            game = "POCKET",
                            players = 150,
                        ),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result shouldHaveSize 1
                result.first().id shouldBe "tournament-123"
            }
        }

        When("여러 토너먼트가 매핑될 때") {
            Then("원본 순서를 유지하며 모든 id가 올바르게 매핑되어야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("id-alpha", "Alpha Event", "2025-01-01", "POCKET", 50),
                        TournamentResponse("id-beta", "Beta Event", "2025-01-02", "POCKET", 60),
                        TournamentResponse("id-gamma", "Gamma Event", "2025-01-03", "POCKET", 70),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result.map { it.id } shouldContainExactly listOf("id-alpha", "id-beta", "id-gamma")
            }
        }
    }

    Given("대용량 데이터 테스트") {
        When("많은 수의 토너먼트가 있을 때") {
            Then("효율적으로 필터링해야 한다") {
                // Given
                val largeTournamentList =
                    (1..1000).map { i ->
                        TournamentResponse(
                            id = "t$i",
                            name = "Event $i",
                            date = "2025-01-01",
                            game = "POCKET",
                            players = i % 100,
                        )
                    }
                coEvery { mockService.fetchTournaments("POCKET") } returns largeTournamentList

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                // 33명 이상: 33~99 범위 = 67개 * 10번 반복 = 670개
                result shouldHaveSize 670
            }
        }
    }

    Given("특수한 시나리오") {
        When("모든 토너먼트가 동일한 참가자 수를 가질 때") {
            Then("임계값과의 관계에 따라 모두 포함되거나 모두 제외되어야 한다") {
                // Given - 모두 35명
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event 1", "2025-01-01", "POCKET", 35),
                        TournamentResponse("t2", "Event 2", "2025-01-02", "POCKET", 35),
                        TournamentResponse("t3", "Event 3", "2025-01-03", "POCKET", 35),
                    )

                // When
                val resultIncluded = dataSource.getQualifyingTournamentIds(minPlayers = 32)
                val resultExcluded = dataSource.getQualifyingTournamentIds(minPlayers = 35)

                // Then
                resultIncluded shouldHaveSize 3
                resultExcluded.shouldBeEmpty()
            }
        }

        When("date 필드가 null인 토너먼트가 있을 때") {
            Then("필터링 로직에 영향을 주지 않아야 한다") {
                // Given
                coEvery { mockService.fetchTournaments("POCKET") } returns
                    listOf(
                        TournamentResponse("t1", "Event with date", "2025-01-01", "POCKET", 40),
                        TournamentResponse("t2", "Event without date", null, "POCKET", 50),
                    )

                // When
                val result = dataSource.getQualifyingTournamentIds()

                // Then
                result shouldHaveSize 2
                result.map { it.id } shouldContainExactly listOf("t1", "t2")
            }
        }
    }
})
