package tcg.pocket.dex

import com.example.stringmatcher.RabinKarpMatcher
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RabinKarpMatcherTest : FunSpec({
    val matcher = RabinKarpMatcher()

    test("Exact match") {
        matcher.contains(text = "hello", pattern = "hello") shouldBe true
    }

    test("Substring match") {
        matcher.contains(text = "hello world", pattern = "world") shouldBe true
    }

    test("Non-matching substring") {
        matcher.contains(text = "hello world", pattern = "worlds") shouldBe false
    }

    test("Empty pattern") {
        matcher.contains(text = "hello", pattern = "") shouldBe true
    }

    test("Empty text") {
        matcher.contains(text = "", pattern = "hello") shouldBe false
    }

    test("Pattern longer than text") {
        matcher.contains(text = "hi", pattern = "hello") shouldBe false
    }

    test("Multiple occurrences") {
        matcher.contains(text = "banana", pattern = "ana") shouldBe true
    }

    test("Case sensitivity") {
        matcher.contains(text = "Hello", pattern = "hello") shouldBe false
    }

    test("Whitespace in text") {
        matcher.contains(text = "Hello World", pattern = "World") shouldBe true
    }

    test("Special characters") {
        matcher.contains(text = "Hello, World!", pattern = "World!") shouldBe true
    }

    test("Numeric pattern") {
        matcher.contains(text = "1234567890", pattern = "456") shouldBe true
    }

    test("Long pattern in large text") {
        val text = "a".repeat(100000) + "hello"
        matcher.contains(text = text, pattern = "hello") shouldBe true
    }
})
