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
import tcg.pocket.dex.remote.response.DeckResponse
import tcg.pocket.dex.remote.response.PlayerResponse
import tcg.pocket.dex.remote.response.RecordResponse
import tcg.pocket.dex.remote.response.StandingResponse

class DefaultTournamentStatsServiceStandingsTest : BehaviorSpec({

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

    Given("fetchStandings 메서드") {
        When("tournament ID로 요청할 때") {
            Then("올바른 URL 경로로 요청되어야 한다") {
                // Given
                val tournamentId = "tournament-path-test"
                val responseBody = "[]"
                server.enqueue(
                    MockResponse()
                        .setBody(responseBody)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json"),
                )

                // When
                service.fetchStandings(tournamentId)

                // Then
                val request = server.takeRequest()
                val path = request.path ?: ""
                path.contains("tournaments") shouldBe true
                path.contains(tournamentId) shouldBe true
                path.contains("standings") shouldBe true
            }
        }

        When("성공적인 응답을 받을 때") {
            Then("순위표를 반환해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "John Doe",
                                    "country": "US",
                                    "region": "North America"
                                },
                                "deck": {
                                    "icons": ["Pikachu ex", "Mewtwo ex"]
                                },
                                "record": {
                                    "wins": 5,
                                    "losses": 0,
                                    "ties": 0
                                }
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
                val result = service.fetchStandings("tournament-123")

                // Then
                val expected =
                    listOf(
                        StandingResponse(
                            placing = 1,
                            player =
                                PlayerResponse(
                                    name = "John Doe",
                                    country = "US",
                                    region = "North America",
                                ),
                            deck = DeckResponse(icons = listOf("Pikachu ex", "Mewtwo ex")),
                            record =
                                RecordResponse(
                                    wins = 5,
                                    losses = 0,
                                    ties = 0,
                                ),
                        ),
                    )
                result shouldBe expected
            }
        }

        When("빈 순위표 응답을 받을 때") {
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
                val result = service.fetchStandings("tournament-456")

                // Then
                result.shouldBeEmpty()
            }
        }

        When("Top 8 순위 응답을 받을 때") {
            Then("모든 순위를 올바르게 파싱해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "Player One",
                                    "country": "US",
                                    "region": "North America"
                                },
                                "deck": {
                                    "icons": ["Pikachu ex", "Zapdos ex"]
                                },
                                "record": {
                                    "wins": 6,
                                    "losses": 0,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 2,
                                "player": {
                                    "name": "Player Two",
                                    "country": "JP",
                                    "region": "Asia"
                                },
                                "deck": {
                                    "icons": ["Mewtwo ex", "Gardevoir"]
                                },
                                "record": {
                                    "wins": 5,
                                    "losses": 1,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 3,
                                "player": {
                                    "name": "Player Three",
                                    "country": "UK",
                                    "region": "Europe"
                                },
                                "deck": {
                                    "icons": ["Charizard ex", "Moltres ex"]
                                },
                                "record": {
                                    "wins": 5,
                                    "losses": 1,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 4,
                                "player": {
                                    "name": "Player Four",
                                    "country": "CA",
                                    "region": "North America"
                                },
                                "deck": {
                                    "icons": ["Starmie ex", "Articuno ex"]
                                },
                                "record": {
                                    "wins": 4,
                                    "losses": 2,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 5,
                                "player": {
                                    "name": "Player Five",
                                    "country": "DE",
                                    "region": "Europe"
                                },
                                "deck": {
                                    "icons": ["Venusaur ex", "Lilligant"]
                                },
                                "record": {
                                    "wins": 4,
                                    "losses": 2,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 6,
                                "player": {
                                    "name": "Player Six",
                                    "country": "FR",
                                    "region": "Europe"
                                },
                                "deck": {
                                    "icons": ["Blastoise ex", "Starmie ex"]
                                },
                                "record": {
                                    "wins": 4,
                                    "losses": 2,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 7,
                                "player": {
                                    "name": "Player Seven",
                                    "country": "AU",
                                    "region": "Oceania"
                                },
                                "deck": {
                                    "icons": ["Marowak ex", "Sandslash"]
                                },
                                "record": {
                                    "wins": 3,
                                    "losses": 3,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 8,
                                "player": {
                                    "name": "Player Eight",
                                    "country": "BR",
                                    "region": "South America"
                                },
                                "deck": {
                                    "icons": ["Gengar ex", "Haunter"]
                                },
                                "record": {
                                    "wins": 3,
                                    "losses": 3,
                                    "ties": 0
                                }
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
                val result = service.fetchStandings("tournament-789")

                // Then
                result shouldHaveSize 8
                result[0].placing shouldBe 1
                result[0].player.name shouldBe "Player One"
                result[0].deck?.icons shouldBe listOf("Pikachu ex", "Zapdos ex")
                result[0].record.wins shouldBe 6
                result[0].record.losses shouldBe 0

                result[7].placing shouldBe 8
                result[7].player.name shouldBe "Player Eight"
                result[7].deck?.icons shouldBe listOf("Gengar ex", "Haunter")
                result[7].record.wins shouldBe 3
                result[7].record.losses shouldBe 3
            }
        }

        When("null 선택적 필드를 포함한 응답을 받을 때") {
            Then("null을 올바르게 처리해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "Anonymous Player"
                                },
                                "deck": null,
                                "record": {
                                    "wins": 4,
                                    "losses": 1,
                                    "ties": 0
                                }
                            },
                            {
                                "placing": 2,
                                "player": {
                                    "name": "Another Player",
                                    "country": null,
                                    "region": null
                                },
                                "deck": {
                                    "icons": null
                                },
                                "record": {
                                    "wins": 3,
                                    "losses": 2,
                                    "ties": 0
                                }
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
                val result = service.fetchStandings("tournament-nulltest")

                // Then
                result shouldHaveSize 2
                result[0].player.name shouldBe "Anonymous Player"
                result[0].player.country shouldBe null
                result[0].player.region shouldBe null
                result[0].deck shouldBe null

                result[1].player.name shouldBe "Another Player"
                result[1].player.country shouldBe null
                result[1].player.region shouldBe null
                result[1].deck?.icons shouldBe null
            }
        }

        When("ties가 있는 순위 응답을 받을 때") {
            Then("무승부를 올바르게 파싱해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "Player with Ties",
                                    "country": "US",
                                    "region": "North America"
                                },
                                "deck": {
                                    "icons": ["Pikachu ex"]
                                },
                                "record": {
                                    "wins": 4,
                                    "losses": 0,
                                    "ties": 2
                                }
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
                val result = service.fetchStandings("tournament-ties")

                // Then
                result shouldHaveSize 1
                result[0].record.wins shouldBe 4
                result[0].record.losses shouldBe 0
                result[0].record.ties shouldBe 2
            }
        }

        When("HTTP 500 에러를 받을 때") {
            Then("예외를 던져야 한다") {
                // Given
                server.enqueue(MockResponse().setResponseCode(500))

                // When & Then
                try {
                    service.fetchStandings("tournament-error")
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
                    service.fetchStandings("tournament-notfound")
                    throw AssertionError("예외가 발생해야 하지만 발생하지 않았습니다.")
                } catch (_: Exception) {
                    // 예외가 잡히면 테스트 통과
                    true shouldBe true
                }
            }
        }

        When("빈 덱 리스트를 포함한 응답을 받을 때") {
            Then("빈 포켓몬 리스트를 올바르게 처리해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "Player with Empty Deck",
                                    "country": "US",
                                    "region": "North America"
                                },
                                "deck": {
                                    "icons": []
                                },
                                "record": {
                                    "wins": 5,
                                    "losses": 0,
                                    "ties": 0
                                }
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
                val result = service.fetchStandings("tournament-emptydeck")

                // Then
                result shouldHaveSize 1
                result[0].deck?.icons shouldBe emptyList()
            }
        }

        When("여러 포켓몬을 포함한 덱 응답을 받을 때") {
            Then("모든 포켓몬을 올바르게 파싱해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "Player with Full Deck",
                                    "country": "JP",
                                    "region": "Asia"
                                },
                                "deck": {
                                    "icons": [
                                        "Pikachu ex",
                                        "Zapdos ex",
                                        "Mewtwo ex",
                                        "Gardevoir",
                                        "Articuno ex"
                                    ]
                                },
                                "record": {
                                    "wins": 6,
                                    "losses": 0,
                                    "ties": 0
                                }
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
                val result = service.fetchStandings("tournament-fulldeck")

                // Then
                result shouldHaveSize 1
                result[0].deck?.icons shouldBe
                    listOf(
                        "Pikachu ex",
                        "Zapdos ex",
                        "Mewtwo ex",
                        "Gardevoir",
                        "Articuno ex",
                    )
            }
        }

        When("완벽한 기록(무패)을 포함한 응답을 받을 때") {
            Then("기록을 올바르게 파싱해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "Undefeated Player",
                                    "country": "KR",
                                    "region": "Asia"
                                },
                                "deck": {
                                    "icons": ["Mewtwo ex", "Gardevoir"]
                                },
                                "record": {
                                    "wins": 7,
                                    "losses": 0,
                                    "ties": 0
                                }
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
                val result = service.fetchStandings("tournament-undefeated")

                // Then
                result shouldHaveSize 1
                result[0].record.wins shouldBe 7
                result[0].record.losses shouldBe 0
                result[0].record.ties shouldBe 0
            }
        }

        When("모든 필드가 완전한 순위 응답을 받을 때") {
            Then("모든 필드를 올바르게 파싱해야 한다") {
                // Given
                val responseBody =
                    """
                    [
                            {
                                "placing": 1,
                                "player": {
                                    "name": "Complete Player",
                                    "country": "US",
                                    "region": "North America"
                                },
                                "deck": {
                                    "icons": ["Charizard ex", "Moltres ex"]
                                },
                                "record": {
                                    "wins": 5,
                                    "losses": 1,
                                    "ties": 1
                                }
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
                val result = service.fetchStandings("tournament-complete")

                // Then
                result shouldHaveSize 1
                val standing = result[0]
                standing.placing shouldBe 1
                standing.player.name shouldBe "Complete Player"
                standing.player.country shouldBe "US"
                standing.player.region shouldBe "North America"
                standing.deck?.icons shouldBe listOf("Charizard ex", "Moltres ex")
                standing.record.wins shouldBe 5
                standing.record.losses shouldBe 1
                standing.record.ties shouldBe 1
            }
        }
    }
})
