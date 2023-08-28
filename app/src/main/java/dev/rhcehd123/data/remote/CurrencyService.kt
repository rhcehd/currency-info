package dev.rhcehd123.data.remote

import dev.rhcehd123.data.remote.response.GetCurrencyResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    companion object {
        const val ACCESS_KEY = "fO4ExQqBBLOrz82BFYkk4ivpfVDxeCUG"
    }

    /*// example query using the "live" endpoint:

    https://api.currencylayer.com/live

    ? access_key = YOUR_ACCESS_KEY
    & source = EUR
    & currencies = USD,GBP,AUD

    // API returns most recent exchange rates for
    // USD, GBP and AUD, all relative to EUR*/

    @GET("currency_data/live")
    fun getCurrency(@Query("apikey") apiKey: String): Flowable<GetCurrencyResponse>
}