package tcg.pocket.dex

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class EnglishStringMatcherTest : FunSpec({
    val stringMatcher = EnglishStringMatcher()

    test("exact match") {
        val search = "hello"
        val target = "hello"
        stringMatcher.isMatched(target = target, search = search) shouldBe true
    }

    test("case insensitive match") {
        val search = "Hello"
        val target = "hello"
        stringMatcher.isMatched(target = target, search = search) shouldBe true
    }

    test("substring match") {
        val search = "lo"
        val target = "hello"
        stringMatcher.isMatched(target = target, search = search) shouldBe false
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

    // 검색 단어가 4글자 이상이라면 target 을 split 햇을 떄 어떤 단어든지 상관 없이 이 부분 포함하는 것이 잇다면 true
    test("sdfasdf") {
        stringMatcher.isMatched(
            target = "My information",
            search = "form",
        )
    }
})
