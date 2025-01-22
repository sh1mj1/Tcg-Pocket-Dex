package tcg.pocket.dex

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec

class DeckCardsTest : BehaviorSpec({
    Given("the cards in deck") {
        When("key cards are not contained in all cards") {
            Then("throw exception") {
                shouldThrow<IllegalArgumentException> {
                    CardDeck(
                        name = "Pikachu ex & Raichu",
                        keyCards =
                            listOf(
                                fakeDoubleRarePikachuExCard,
                                fakeRaichuCard,
                            ),
                        allCards =
                            listOf(
                                fakePikachuCard,
                            ),
                    )
                }
            }
        }
        When("key cards are contained in all cards") {
            Then("no exception") {
                shouldNotThrow<Exception> {
                    CardDeck(
                        name = "Pikachu ex & Raichu",
                        keyCards =
                            listOf(
                                fakeDoubleRarePikachuExCard,
                                fakeRaichuCard,
                            ),
                        allCards =
                            listOf(
                                fakeDoubleRarePikachuExCard,
                                fakeRaichuCard,
                                fakePikachuCard,
                            ),
                    )
                }
            }
        }
    }
})
