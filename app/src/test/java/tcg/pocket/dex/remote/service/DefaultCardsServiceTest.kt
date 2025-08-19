package tcg.pocket.dex.remote.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import tcg.pocket.dex.remote.response.BriefCardResponse

class DefaultCardsServiceTest : BehaviorSpec({

    lateinit var server: MockWebServer
    lateinit var service: CardsService

    beforeSpec {
        server = MockWebServer()
        server.start()
        val client = HttpClient(CIO) {
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
        service = DefaultCardsService(client, server.url("/").toString())
    }

    afterSpec {
        server.shutdown()
    }

    Given("briefCards 메서드") {
        When("성공적인 응답을 받을 때") {
            Then("카드 목록을 반환해야 한다") {
                // Given
                val responseBody = """
                    [
                        {
                            "id": "sv3-1",
                            "localId": "1",
                            "name": "Bulbasaur",
                            "image": "https://assets.tcgdex.net/en/sv/sv3/1"
                        },
                        {
                            "id": "sv3-2",
                            "localId": "2",
                            "name": "Ivysaur",
                            "image": "https://assets.tcgdex.net/en/sv/sv3/2"
                        }
                    ]
                """.trimIndent()
                server.enqueue(MockResponse().setBody(responseBody).setResponseCode(200))

                // When
                val result = service.briefCards()

                // Then
                val expected = listOf(
                    BriefCardResponse("sv3-1", "1", "Bulbasaur", "https://assets.tcgdex.net/en/sv/sv3/1"),
                    BriefCardResponse("sv3-2", "2", "Ivysaur", "https://assets.tcgdex.net/en/sv/sv3/2")
                )
                result shouldBe expected
            }
        }

        When("에러 응답을 받을 때") {
            Then("예외를 던져야 한다") {
                // Given
                server.enqueue(MockResponse().setResponseCode(500))

                // When & Then
                // Ktor는 에러에 따라 다른 예외를 던지므로, 일반적인 예외를 잡습니다.
                try {
                    service.briefCards()
                    throw AssertionError("예외가 발생해야 하지만 발생하지 않았습니다.")
                } catch (e: Exception) {
                    // 예외가 잡히면 테스트 통과
                    true shouldBe true
                }
            }
        }
    }
})