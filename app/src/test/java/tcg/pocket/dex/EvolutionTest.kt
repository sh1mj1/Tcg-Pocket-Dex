package tcg.pocket.dex

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec

class EvolutionTest : BehaviorSpec({
    Given("A pokemon's evolution stage is 0") {
        When("the evolutionFrom is not empty") {
            Then("throw IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    Card.Pokemon.Evolution(
                        stage = 0,
                        evolveFrom = listOf(fakePikachuCardCode),
                        evolveTo = emptyList(),
                    )
                }
            }
            When("the evolutionFrom is empty") {
                Then("no exception") {
                    shouldNotThrow<Exception> {
                        Card.Pokemon.Evolution(
                            stage = 0,
                            evolveFrom = emptyList(),
                            evolveTo = emptyList(),
                        )
                    }
                }
            }
        }
    }
    Given("A pokemon's evolution stage is greater than 0") {
        When("the evolutionFrom is empty") {
            Then("throw IllegalArgumentException") {
                shouldThrow<IllegalArgumentException> {
                    Card.Pokemon.Evolution(
                        stage = 1,
                        evolveFrom = emptyList(),
                        evolveTo = emptyList(),
                    )
                }
            }
        }
        When("thr evolutionFrom is not empty") {
            Then("no exception") {
                shouldNotThrow<Exception> {
                    Card.Pokemon.Evolution(
                        stage = 1,
                        evolveFrom = listOf(fakePikachuCardCode),
                        evolveTo = emptyList(),
                    )
                }
            }
        }
    }
})
