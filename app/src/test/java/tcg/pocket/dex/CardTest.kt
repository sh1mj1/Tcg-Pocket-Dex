package tcg.pocket.dex

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class CardTest : BehaviorSpec({
    Given("the basic Pikachu card and raichu card in GENETIC APEX Mewtwo Expansion Pack") {
        val (pikachuCardCode, raichuCardCode) = Pair(fakePikachuCardCode, fakeRaichuCardCode)
        val (pikachuCard, raichuCard) = Pair(fakePikachuCard, fakeRaichuCard)

        When("The basic Pikachu card") {
            Then("price is 35") {
                pikachuCard.price shouldBe 35
            }
            Then("can evolve to Raichu") {
                pikachuCard.evolution.evolveTo shouldContain raichuCardCode
            }
            Then("Raichu is related to Pikachu") {
                pikachuCard.relatedCardCodes shouldContain raichuCardCode
            }
        }

        When("The basic raichu card") {
            Then("price is 150") {
                raichuCard.price shouldBe 150
            }
            Then("can evolve from Pikachu") {
                raichuCard.evolution.evolveFrom shouldContain pikachuCardCode
            }
            Then("Pikachu is related to Raichu") {
                raichuCard.relatedCardCodes shouldContain pikachuCardCode
            }
        }
    }
})
