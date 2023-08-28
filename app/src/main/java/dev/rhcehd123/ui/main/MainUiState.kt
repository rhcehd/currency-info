package dev.rhcehd123.ui.main

data class MainUiState (
    val remittanceCountry: String,
    val recipientCountry: String,
    val remittance: String,
    val recipient: String,
    val currencyRate: String,
    val lookupTime: String,
) {
    constructor() : this("미국(USD)", "", "", "", "", "")

    fun update(
        remittanceCountry: String? = null,
        recipientCountry: String? = null,
        remittance: String? = null,
        recipient: String? = null,
        currencyRate: String? = null,
        lookupTime: String? = null,
    ): MainUiState {
        return MainUiState(
            remittanceCountry ?: this.remittanceCountry,
            recipientCountry ?: this.recipientCountry,
            remittance ?: this.remittance,
            recipient ?: this.recipient,
            currencyRate ?: this.currencyRate,
            lookupTime ?: this.lookupTime
        )
    }
}