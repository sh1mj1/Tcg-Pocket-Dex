package com.example.lib_stringmatcher

interface StringMatcher {
    fun isMatched(
        target: String,
        search: String,
    ): Boolean
}
