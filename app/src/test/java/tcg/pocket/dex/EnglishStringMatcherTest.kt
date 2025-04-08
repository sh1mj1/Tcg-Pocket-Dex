package tcg.pocket.dex

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class EnglishStringMatcherTest : FunSpec({
    val stringMatcher = EnglishStringMatcher()

    test("exact match") {
        val search = "hello"
        val target = "hello"
        stringMatcher.isMatched(search, target) shouldBe true
    }

    test("case insensitive match") {
        val search = "Hello"
        val target = "hello"
        stringMatcher.isMatched(search, target) shouldBe true
    }

    test("substring match") {
        val search = "lo"
        val target = "hello"
        stringMatcher.isMatched(search, target) shouldBe false
    }

    test("pica ex match") {
        stringMatcher.isMatched(target = "Pica ex", search = "p") shouldBe true
        stringMatcher.isMatched(target = "Pica ex", search = "e") shouldBe false
        stringMatcher.isMatched(target = "Pica ex", search = "ex") shouldBe true
    }

    test("my name match") {
        stringMatcher.isMatched(target = "My name", search = "My") shouldBe true
        stringMatcher.isMatched(target = "My name", search = "m") shouldBe true
        stringMatcher.isMatched(target = "My name", search = "N") shouldBe false
        stringMatcher.isMatched(target = "My name ", search = "Na") shouldBe true
    }
})
