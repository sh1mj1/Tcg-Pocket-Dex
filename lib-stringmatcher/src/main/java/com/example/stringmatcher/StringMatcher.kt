package com.example.stringmatcher

interface StringMatcher {
    fun isMatched(
        target: String,
        search: String,
    ): Boolean
}
