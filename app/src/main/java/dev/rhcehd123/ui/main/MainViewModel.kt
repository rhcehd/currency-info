package dev.rhcehd123.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rhcehd123.data.repository.CurrencyRepository
import dev.rhcehd123.data.repository.FakeLocalRepository
import dev.rhcehd123.util.Util
import dev.rhcehd123.util.Util.toFormattedString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    fakeLocalRepository: FakeLocalRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    var uiState = _uiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    val recipientList = fakeLocalRepository.getRecipientList()

    init {
        initializeUiState()
    }

    private fun initializeUiState() {
        if(!currencyRepository.isInitialized()) {
            viewModelScope.launch {
                _toastEvent.emit("환율 정보를 불러오는 중입니다")
                delay(2000)
                initializeUiState()
            }
        } else {
            val recipientCountry = recipientList[0]
            setRecipientCountry(recipientCountry)
        }
    }

    fun setRemittance(remittance: String) {
        val remittanceInt = try {
            /*val temp = remittance.toInt()
            if(temp > 99999) {
                99999
            } else {
                temp
            }*/
            remittance.toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val remittanceCountryCode = Util.parseCountryCodeFromName(_uiState.value.remittanceCountry)
        val recipientCountryCode = Util.parseCountryCodeFromName(_uiState.value.recipientCountry)
        val currencyRate = currencyRepository.getCurrencyRate(recipientCountryCode)
        val lookupTime = currencyRepository.currencyLookupTime
        val recipient = "${(remittanceInt * currencyRate).toFormattedString()} $recipientCountryCode / $remittanceCountryCode"

        _uiState.value = _uiState.value.update(
            remittance = remittance,
            recipient = recipient,
            lookupTime = lookupTime
        )
    }

    fun setRecipientCountry(recipientCountry: String) {
        val remittanceCountryCode = Util.parseCountryCodeFromName(_uiState.value.remittanceCountry)
        val recipientCountryCode = Util.parseCountryCodeFromName(recipientCountry)
        val currencyRate = currencyRepository.getCurrencyRate(recipientCountryCode)
        val lookupTime = currencyRepository.currencyLookupTime
        val remittanceInt = try {
            _uiState.value.remittance.toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val currencyRateString = "${currencyRate.toFormattedString()} $recipientCountryCode"
        val recipient = "${(remittanceInt * currencyRate).toFormattedString()} $recipientCountryCode / $remittanceCountryCode"

        _uiState.value = _uiState.value.update(
            recipientCountry = recipientCountry,
            currencyRate = currencyRateString,
            lookupTime = lookupTime,
            recipient = recipient
        )
    }
}