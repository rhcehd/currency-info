package dev.rhcehd123.data.repository

import dev.rhcehd123.data.remote.CurrencyService
import dev.rhcehd123.util.Util
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CurrencyRepository(
    private val currencyService: CurrencyService,
    private val compositeDisposable: CompositeDisposable
) {
    private val currencyData = mutableMapOf<String, Double>()
    var currencyLookupTime = ""
        private set
    private var lastRefreshTimeMillis = 0L

    fun isInitialized() = currencyData.isNotEmpty()

    fun startIntervalRefresh() {
        val currentTimeMillis = System.currentTimeMillis()
        val initialDelay = if(currentTimeMillis - lastRefreshTimeMillis > 600000) {
            0
        } else {
            600000 - (currentTimeMillis - lastRefreshTimeMillis)
        }
        Flowable.interval(initialDelay, 600000, TimeUnit.MILLISECONDS)
            .concatMap {
                currencyService.getCurrency(CurrencyService.ACCESS_KEY)
                    .retry(5)
            }
            .subscribe({ response ->
                currencyData.clear()
                currencyData.putAll(response.quotes)
                currencyLookupTime = Util.formatDate(Calendar.getInstance().timeInMillis)
                lastRefreshTimeMillis = System.currentTimeMillis()
            }, { t ->
                t.printStackTrace()
            }, {
                // do nothing
            }, compositeDisposable)
    }

    fun stopIntervalRefresh() {

    }

    fun getCurrencyData() {
        if(currencyData.isNotEmpty()) {
            return
        }
        currencyService.getCurrency(CurrencyService.ACCESS_KEY)
            .retry(5)
            .subscribe({ response ->
                currencyData.clear()
                currencyData.putAll(response.quotes)
                currencyLookupTime = Util.formatDate(Calendar.getInstance().timeInMillis)
                lastRefreshTimeMillis = System.currentTimeMillis()
            }, { t ->
                t.printStackTrace()
            }, {
                // do nothing
            }, compositeDisposable)
    }

    fun getDummyData() {
        currencyData.clear()
        currencyData.putAll(
            mutableMapOf(
                "USDKRW" to 1000.0,
                "USDJPY" to 2000.0,
                "USDPHP" to 3000.0
            )
        )
        currencyLookupTime = Util.formatDate(Calendar.getInstance().timeInMillis)
        lastRefreshTimeMillis = System.currentTimeMillis()
    }

    fun getCurrencyRate(key: String): Double = currencyData["USD$key"] ?: 0.0
}