package com.example.stringmatcher

class RabinKarpMatcher {
    private val base = 256
    private val prime = 101

    fun contains(
        text: String,
        pattern: String,
    ): Boolean {
        val patternLength = pattern.length
        val textLength = text.length

        if (patternLength == 0) return true
        if (patternLength > textLength) return false

        var currentPatternHash = 0
        var currentTextHash = 0
        var hashFactor = 1

        repeat(patternLength - 1) { hashFactor = (hashFactor * base) % prime }

        (0 until patternLength).forEach { i ->
            currentPatternHash = (base * currentPatternHash + pattern[i].code) % prime
            currentTextHash = (base * currentTextHash + text[i].code) % prime
        }

        (0..(textLength - patternLength)).forEach { i ->
            if (currentPatternHash == currentTextHash) {
                if (text.substring(i, i + patternLength) == pattern) {
                    return true
                }
            }
            if (i < textLength - patternLength) {
                currentTextHash =
                    (base * (currentTextHash - text[i].code * hashFactor) + text[i + patternLength].code) % prime
                if (currentTextHash < 0) currentTextHash += prime
            }
        }
        return false
    }
}
