package dev.rhcehd123.data.remote.response

data class GetCurrencyResponse(
    val quotes: Map<String, Double>,
    val source: String,
    val success: Boolean,
    val timestamp: Int
)