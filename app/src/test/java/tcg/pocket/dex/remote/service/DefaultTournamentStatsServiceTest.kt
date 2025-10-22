package tcg.pocket.dex.remote.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import tcg.pocket.dex.remote.response.TournamentResponse

class DefaultTournamentStatsServiceTest : BehaviorSpec({

    lateinit var server: MockWebServer
    lateinit var service: TournamentStatsService

    beforeSpec {
        server = MockWebServer()
        server.start()
        val client =
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
            }
        service = DefaultTournamentStatsService(client, server.url("/").toString())
    }

    afterSpec {
        server.shutdown()
    }

    Given("fetchTournaments 메서드") {
        When("성공적인 응답을 받을 때") {
            Then("토너먼트 목록을 반환해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                        {
                            "id": "tournament-123",
                            "name": "Pokemon TCG Pocket Championship",
                            "date": "2025-10-15",
                            "game": "POCKET",
                            "players": 64
                        },
                        {
                            "id": "tournament-456",
                            "name": "Pokemon TCG Pocket Regionals",
                            "date": "2025-10-20",
                            "game": "POCKET",
                            "players": 128
                        }
                    ]
                    """.trimIndent()
                server.enqueue(
                    MockResponse()
                        .setBody(responseBody)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json"),
                )

                // When
                val result = service.fetchTournaments("POCKET")

                // Then
                val expected =
                    listOf(
                        TournamentResponse(
                            id = "tournament-123",
                            name = "Pokemon TCG Pocket Championship",
                            date = "2025-10-15",
                            game = "POCKET",
                            players = 64,
                        ),
                        TournamentResponse(
                            id = "tournament-456",
                            name = "Pokemon TCG Pocket Regionals",
                            date = "2025-10-20",
                            game = "POCKET",
                            players = 128,
                        ),
                    )
                result shouldBe expected
            }
        }

        When("빈 배열 응답을 받을 때") {
            Then("빈 리스트를 반환해야 한다") {
                // Given
                val responseBody = "[]"
                server.enqueue(
                    MockResponse()
                        .setBody(responseBody)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json"),
                )

                // When
                val result = service.fetchTournaments("POCKET")

                // Then
                result.shouldBeEmpty()
            }
        }

        When("단일 토너먼트 응답을 받을 때") {
            Then("하나의 토너먼트를 반환해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                        {
                            "id": "tournament-789",
                            "name": "Pokemon TCG Pocket Winter Cup",
                            "date": "2025-12-01",
                            "game": "POCKET",
                            "players": 32
                        }
                    ]
                    """.trimIndent()
                server.enqueue(
                    MockResponse()
                        .setBody(responseBody)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json"),
                )

                // When
                val result = service.fetchTournaments("POCKET")

                // Then
                result shouldHaveSize 1
                result[0].id shouldBe "tournament-789"
                result[0].name shouldBe "Pokemon TCG Pocket Winter Cup"
                result[0].date shouldBe "2025-12-01"
                result[0].game shouldBe "POCKET"
                result[0].players shouldBe 32
            }
        }

        When("여러 토너먼트 응답을 받을 때") {
            Then("모든 토너먼트를 반환해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                        {
                            "id": "tournament-001",
                            "name": "Spring Championship",
                            "date": "2025-03-15",
                            "game": "POCKET",
                            "players": 64
                        },
                        {
                            "id": "tournament-002",
                            "name": "Summer Championship",
                            "date": "2025-06-15",
                            "game": "POCKET",
                            "players": 128
                        },
                        {
                            "id": "tournament-003",
                            "name": "Fall Championship",
                            "date": "2025-09-15",
                            "game": "POCKET",
                            "players": 96
                        }
                    ]
                    """.trimIndent()
                server.enqueue(
                    MockResponse()
                        .setBody(responseBody)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json"),
                )

                // When
                val result = service.fetchTournaments("POCKET")

                // Then
                result shouldHaveSize 3
                result[0].id shouldBe "tournament-001"
                result[1].id shouldBe "tournament-002"
                result[2].id shouldBe "tournament-003"
            }
        }

        When("date 필드가 null인 토너먼트 응답을 받을 때") {
            Then("date가 null인 토너먼트를 반환해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                        {
                            "id": "tournament-999",
                            "name": "Upcoming Tournament",
                            "game": "POCKET",
                            "players": 50
                        }
                    ]
                    """.trimIndent()
                server.enqueue(
                    MockResponse()
                        .setBody(responseBody)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json"),
                )

                // When
                val result = service.fetchTournaments("POCKET")

                // Then
                result shouldHaveSize 1
                result[0].id shouldBe "tournament-999"
                result[0].date shouldBe null
            }
        }

        When("HTTP 500 에러를 받을 때") {
            Then("예외를 던져야 한다") {
                // Given
                server.enqueue(MockResponse().setResponseCode(500))

                // When & Then
                try {
                    service.fetchTournaments("POCKET")
                    throw AssertionError("예외가 발생해야 하지만 발생하지 않았습니다.")
                } catch (_: Exception) {
                    // 예외가 잡히면 테스트 통과
                    true shouldBe true
                }
            }
        }

        When("HTTP 404 에러를 받을 때") {
            Then("예외를 던져야 한다") {
                // Given
                server.enqueue(MockResponse().setResponseCode(404))

                // When & Then
                try {
                    service.fetchTournaments("POCKET")
                    throw AssertionError("예외가 발생해야 하지만 발생하지 않았습니다.")
                } catch (_: Exception) {
                    // 예외가 잡히면 테스트 통과
                    true shouldBe true
                }
            }
        }

        When("game 파라미터를 전달할 때") {
            Then("쿼리 파라미터가 올바르게 전송되어야 한다") {
                // Given
                val responseBody = "[]"
                server.enqueue(
                    MockResponse()
                        .setBody(responseBody)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json"),
                )

                // When
                service.fetchTournaments("POCKET")

                // Then
                val request = server.takeRequest()
                val path = request.path ?: ""
                path.contains("tournaments") shouldBe true
                path.contains("game=POCKET") shouldBe true
            }
        }
    }
})
