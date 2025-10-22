package tcg.pocket.dex.remote.response

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TournamentResponseTest : FunSpec({

    test("toTournamentId should extract tournament ID correctly") {
        val response =
            TournamentResponse(
                id = "tournament-123",
                name = "Championship",
                date = "2025-10-15",
                game = "POCKET",
                players = 64,
            )

        val result = response.toTournamentId()

        result.id shouldBe "tournament-123"
    }

    test("toTournamentId works with null date") {
        val response =
            TournamentResponse(
                id = "tournament-456",
                name = "Weekly Event",
                date = null,
                game = "POCKET",
                players = 40,
            )

        val result = response.toTournamentId()

        result.id shouldBe "tournament-456"
    }

    test("toTournamentId with empty string ID") {
        val response =
            TournamentResponse(
                id = "",
                name = "Test Tournament",
                date = "2025-10-22",
                game = "POCKET",
                players = 32,
            )

        val result = response.toTournamentId()

        result.id shouldBe ""
    }

    test("toTournamentId with special characters in ID") {
        val response =
            TournamentResponse(
                id = "tournament-2025-Q4-finals_#1",
                name = "Q4 Finals",
                date = "2025-12-31",
                game = "POCKET",
                players = 128,
            )

        val result = response.toTournamentId()

        result.id shouldBe "tournament-2025-Q4-finals_#1"
    }

    test("toTournamentId with unicode characters in tournament name") {
        val response =
            TournamentResponse(
                id = "jp-tournament-001",
                name = "ポケモン大会",
                date = "2025-11-01",
                game = "POCKET",
                players = 256,
            )

        val result = response.toTournamentId()

        result.id shouldBe "jp-tournament-001"
    }

    test("toTournamentId with minimum player count") {
        val response =
            TournamentResponse(
                id = "small-tournament",
                name = "Local Event",
                date = "2025-10-30",
                game = "POCKET",
                players = 1,
            )

        val result = response.toTournamentId()

        result.id shouldBe "small-tournament"
    }

    test("toTournamentId with maximum player count") {
        val response =
            TournamentResponse(
                id = "worlds-2025",
                name = "World Championship 2025",
                date = "2025-08-15",
                game = "POCKET",
                players = 10000,
            )

        val result = response.toTournamentId()

        result.id shouldBe "worlds-2025"
    }

    test("toTournamentId with different game values") {
        val response =
            TournamentResponse(
                id = "tcg-live-tournament",
                name = "TCG Live Event",
                date = "2025-09-01",
                game = "LIVE",
                players = 50,
            )

        val result = response.toTournamentId()

        result.id shouldBe "tcg-live-tournament"
    }

    test("toTournamentId with empty date string") {
        val response =
            TournamentResponse(
                id = "tournament-789",
                name = "TBD Tournament",
                date = "",
                game = "POCKET",
                players = 75,
            )

        val result = response.toTournamentId()

        result.id shouldBe "tournament-789"
    }

    test("toTournamentId with date in different format") {
        val response =
            TournamentResponse(
                id = "tournament-abc",
                name = "Summer Cup",
                date = "15/10/2025",
                game = "POCKET",
                players = 100,
            )

        val result = response.toTournamentId()

        result.id shouldBe "tournament-abc"
    }

    test("toTournamentId preserves only ID and ignores other fields") {
        val response =
            TournamentResponse(
                id = "preserve-id-test",
                name = "This name should not affect result",
                date = "This date should not affect result",
                game = "This game should not affect result",
                players = 999999,
            )

        val result = response.toTournamentId()

        result.id shouldBe "preserve-id-test"
    }

    test("TournamentId data class equality works correctly") {
        val tournamentId1 = TournamentId("same-id")
        val tournamentId2 = TournamentId("same-id")
        val tournamentId3 = TournamentId("different-id")

        (tournamentId1 == tournamentId2) shouldBe true
        (tournamentId1 == tournamentId3) shouldBe false
    }

    test("TournamentResponse with very long ID string") {
        val longId = "tournament-" + "a".repeat(1000)
        val response =
            TournamentResponse(
                id = longId,
                name = "Long ID Test",
                date = "2025-10-22",
                game = "POCKET",
                players = 64,
            )

        val result = response.toTournamentId()

        result.id shouldBe longId
    }

    test("TournamentResponse with numeric string ID") {
        val response =
            TournamentResponse(
                id = "12345678",
                name = "Numeric ID Tournament",
                date = "2025-10-22",
                game = "POCKET",
                players = 80,
            )

        val result = response.toTournamentId()

        result.id shouldBe "12345678"
    }

    test("TournamentResponse with UUID format ID") {
        val uuid = "550e8400-e29b-41d4-a716-446655440000"
        val response =
            TournamentResponse(
                id = uuid,
                name = "UUID Tournament",
                date = "2025-10-22",
                game = "POCKET",
                players = 128,
            )

        val result = response.toTournamentId()

        result.id shouldBe uuid
    }
})
